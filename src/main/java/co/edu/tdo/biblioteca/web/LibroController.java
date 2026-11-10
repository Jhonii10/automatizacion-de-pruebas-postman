package co.edu.tdo.biblioteca.web;

import co.edu.tdo.biblioteca.model.Libro;
import co.edu.tdo.biblioteca.model.LibroRequest;
import co.edu.tdo.biblioteca.service.BibliotecaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final BibliotecaService service;

    public LibroController(BibliotecaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Libro> listar(@RequestParam(required = false) String autor) {
        return service.listarLibros(autor);
    }

    @GetMapping("/{id}")
    public Libro consultar(@PathVariable Long id) {
        return service.consultarLibro(id);
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@Valid @RequestBody LibroRequest request) {
        Libro libro = service.crearLibro(request);
        return ResponseEntity.created(URI.create("/api/libros/" + libro.id())).body(libro);
    }
}
