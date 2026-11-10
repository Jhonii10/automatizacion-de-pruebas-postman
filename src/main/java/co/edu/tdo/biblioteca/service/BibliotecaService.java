package co.edu.tdo.biblioteca.service;

import co.edu.tdo.biblioteca.exception.ApiException;
import co.edu.tdo.biblioteca.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/** Lógica de negocio con almacenamiento en memoria. Métodos sincronizados para mantener el stock consistente. */
@Service
public class BibliotecaService {

    private final Map<Long, Libro> libros = new LinkedHashMap<>();
    private final Map<Long, Prestamo> prestamos = new LinkedHashMap<>();
    private final AtomicLong secLibros = new AtomicLong();
    private final AtomicLong secPrestamos = new AtomicLong();

    private final int diasPrestamo;
    private final int maxPrestamosUsuario;

    public BibliotecaService(@Value("${biblioteca.dias-prestamo}") int diasPrestamo,
                             @Value("${biblioteca.max-prestamos-usuario}") int maxPrestamosUsuario) {
        this.diasPrestamo = diasPrestamo;
        this.maxPrestamosUsuario = maxPrestamosUsuario;
        // Datos semilla para exploración manual
        crearLibro(new LibroRequest("9780132350884", "Clean Code", "Robert C. Martin", 2008, 3));
        crearLibro(new LibroRequest("9780201616224", "The Pragmatic Programmer", "Andrew Hunt", 1999, 2));
        crearLibro(new LibroRequest("9780321125217", "Domain-Driven Design", "Eric Evans", 2003, 1));
    }

    // ---------- Libros ----------

    public synchronized Libro crearLibro(LibroRequest r) {
        if (r.anio() > Year.now().getValue()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Datos inválidos",
                    Map.of("anio", "El año no puede ser posterior al actual"));
        }
        boolean duplicado = libros.values().stream().anyMatch(l -> l.isbn().equals(r.isbn()));
        if (duplicado) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un libro con ISBN " + r.isbn(),
                    Map.of("isbn", "ISBN duplicado"));
        }
        long id = secLibros.incrementAndGet();
        Libro libro = new Libro(id, r.isbn(), r.titulo().trim(), r.autor().trim(), r.anio(), r.ejemplares(), r.ejemplares());
        libros.put(id, libro);
        return libro;
    }

    public synchronized Libro consultarLibro(Long id) {
        return Optional.ofNullable(libros.get(id)).orElseThrow(() -> ApiException.noEncontrado("Libro", id));
    }

    public synchronized List<Libro> listarLibros(String autor) {
        return libros.values().stream()
                .filter(l -> autor == null || l.autor().toLowerCase().contains(autor.toLowerCase()))
                .toList();
    }

    // ---------- Préstamos ----------

    public synchronized Prestamo prestar(PrestamoRequest r) {
        Libro libro = consultarLibro(r.libroId());
        String usuario = r.usuario().trim();

        long activos = prestamos.values().stream()
                .filter(p -> p.usuario().equalsIgnoreCase(usuario) && p.estado() == EstadoPrestamo.ACTIVO)
                .count();
        if (activos >= maxPrestamosUsuario) {
            throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "El usuario " + usuario + " alcanzó el máximo de " + maxPrestamosUsuario + " préstamos activos");
        }
        if (libro.disponibles() <= 0) {
            throw new ApiException(HttpStatus.CONFLICT, "Sin ejemplares disponibles del libro " + libro.id());
        }

        libros.put(libro.id(), libro.conDisponibles(libro.disponibles() - 1));
        LocalDate hoy = LocalDate.now();
        Prestamo p = new Prestamo(secPrestamos.incrementAndGet(), libro.id(), usuario, hoy,
                hoy.plusDays(diasPrestamo), null, EstadoPrestamo.ACTIVO);
        prestamos.put(p.id(), p);
        return p;
    }

    public synchronized Prestamo devolver(Long prestamoId) {
        Prestamo p = Optional.ofNullable(prestamos.get(prestamoId))
                .orElseThrow(() -> ApiException.noEncontrado("Préstamo", prestamoId));
        if (p.estado() == EstadoPrestamo.DEVUELTO) {
            throw new ApiException(HttpStatus.CONFLICT, "El préstamo " + prestamoId + " ya fue devuelto");
        }
        Libro libro = libros.get(p.libroId());
        libros.put(libro.id(), libro.conDisponibles(libro.disponibles() + 1));
        Prestamo devuelto = p.devolver(LocalDate.now());
        prestamos.put(devuelto.id(), devuelto);
        return devuelto;
    }

    public synchronized List<Prestamo> listarPrestamos(String usuario) {
        return prestamos.values().stream()
                .filter(p -> usuario == null || p.usuario().equalsIgnoreCase(usuario))
                .toList();
    }
}
