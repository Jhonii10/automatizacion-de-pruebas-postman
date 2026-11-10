package co.edu.tdo.biblioteca.web;

import co.edu.tdo.biblioteca.model.Prestamo;
import co.edu.tdo.biblioteca.model.PrestamoRequest;
import co.edu.tdo.biblioteca.service.BibliotecaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final BibliotecaService service;

    public PrestamoController(BibliotecaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prestamo> listar(@RequestParam(required = false) String usuario) {
        return service.listarPrestamos(usuario);
    }

    @PostMapping
    public ResponseEntity<Prestamo> prestar(@Valid @RequestBody PrestamoRequest request) {
        Prestamo p = service.prestar(request);
        return ResponseEntity.created(URI.create("/api/prestamos/" + p.id())).body(p);
    }

    @PutMapping("/{id}/devolucion")
    public Prestamo devolver(@PathVariable Long id) {
        return service.devolver(id);
    }
}
