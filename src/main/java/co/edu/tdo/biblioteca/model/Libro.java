package co.edu.tdo.biblioteca.model;

public record Libro(Long id, String isbn, String titulo, String autor, Integer anio,
                    Integer ejemplares, Integer disponibles) {

    public Libro conDisponibles(int nuevos) {
        return new Libro(id, isbn, titulo, autor, anio, ejemplares, nuevos);
    }
}
