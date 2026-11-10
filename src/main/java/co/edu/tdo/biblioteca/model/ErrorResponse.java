package co.edu.tdo.biblioteca.model;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(int status, String error, String mensaje, Map<String, String> campos, Instant timestamp) {

    public static ErrorResponse of(int status, String error, String mensaje, Map<String, String> campos) {
        return new ErrorResponse(status, error, mensaje, campos == null ? Map.of() : campos, Instant.now());
    }
}
