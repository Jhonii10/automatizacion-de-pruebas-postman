package co.edu.tdo.biblioteca.model;

import java.time.LocalDate;

public record Prestamo(Long id, Long libroId, String usuario, LocalDate fechaPrestamo,
                       LocalDate fechaLimite, LocalDate fechaDevolucion, EstadoPrestamo estado) {

    public Prestamo devolver(LocalDate fecha) {
        return new Prestamo(id, libroId, usuario, fechaPrestamo, fechaLimite, fecha, EstadoPrestamo.DEVUELTO);
    }
}
