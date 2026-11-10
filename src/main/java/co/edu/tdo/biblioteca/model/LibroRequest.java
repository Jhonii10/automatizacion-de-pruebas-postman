package co.edu.tdo.biblioteca.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LibroRequest(
        @NotBlank(message = "El ISBN es obligatorio")
        @Pattern(regexp = "\\d{13}", message = "El ISBN debe tener exactamente 13 dígitos")
        String isbn,

        @NotBlank(message = "El título es obligatorio")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        String autor,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 1450, message = "El año mínimo es 1450")
        Integer anio,

        @NotNull(message = "Los ejemplares son obligatorios")
        @Min(value = 0, message = "Los ejemplares no pueden ser negativos")
        @Max(value = 100, message = "Máximo 100 ejemplares por título")
        Integer ejemplares
) {
}
