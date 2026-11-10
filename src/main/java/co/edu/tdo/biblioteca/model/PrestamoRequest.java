package co.edu.tdo.biblioteca.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrestamoRequest(
        @NotNull(message = "El libro es obligatorio") Long libroId,
        @NotBlank(message = "El usuario es obligatorio") String usuario
) {
}
