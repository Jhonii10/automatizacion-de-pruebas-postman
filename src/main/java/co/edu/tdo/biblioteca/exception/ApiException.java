package co.edu.tdo.biblioteca.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

/** Excepción de negocio con el código HTTP y, opcionalmente, el campo afectado. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final Map<String, String> campos;

    public ApiException(HttpStatus status, String mensaje) {
        this(status, mensaje, Map.of());
    }

    public ApiException(HttpStatus status, String mensaje, Map<String, String> campos) {
        super(mensaje);
        this.status = status;
        this.campos = campos;
    }

    public HttpStatus getStatus() { return status; }
    public Map<String, String> getCampos() { return campos; }

    public static ApiException noEncontrado(String recurso, Long id) {
        return new ApiException(HttpStatus.NOT_FOUND, recurso + " con id " + id + " no existe");
    }
}
