import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

final class PersistenciaPrestamos {
    static final String PRESTAMOS = "prestamos.txt";
    static final String DETALLES = "detalles.txt";
    static final String PENALIZACIONES = "penalizaciones.txt";

    private PersistenciaPrestamos() {
    }

    static void cargar(Path directorio, Biblioteca biblioteca) throws IOException {
        Map<Integer, Usuario> usuarios = new HashMap<>();
        Map<Integer, Ejemplar> ejemplares = new HashMap<>();
        for (Usuario usuario : biblioteca.getUsuarios()) {
            usuarios.put(usuario.getIdUsuario(), usuario);
        }
        for (Libro libro : biblioteca.getLibros()) {
            for (Ejemplar ejemplar : libro.getEjemplares()) {
                ejemplares.put(ejemplar.getIdEjemplar(), ejemplar);
            }
        }

        Map<Integer, Prestamo> prestamos = cargarPrestamos(directorio, biblioteca, usuarios);
        Map<Integer, DetallePrestamo> detalles = cargarDetalles(directorio, biblioteca, prestamos, usuarios, ejemplares);
        cargarPenalizaciones(directorio, biblioteca, detalles);
        for (Prestamo prestamo : prestamos.values()) {
            if (prestamo.getDetalles().isEmpty()) {
                throw new IOException("prestamos.txt: el préstamo " + prestamo.getIdPrestamo() + " no tiene detalles.");
            }
            biblioteca.incorporarPrestamo(prestamo);
        }
    }

    private static Map<Integer, Prestamo> cargarPrestamos(Path directorio, Biblioteca biblioteca,
            Map<Integer, Usuario> usuarios) throws IOException {
        Map<Integer, Prestamo> prestamos = new LinkedHashMap<>();
        Set<Integer> ids = new HashSet<>();
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            ids.add(prestamo.getIdPrestamo());
        }
        ArchivoTexto.leer(directorio.resolve(PRESTAMOS), 4, campos -> {
            int id = Integer.parseInt(campos[0]);
            if (!ids.add(id)) {
                throw new IllegalArgumentException("ID de préstamo repetido: " + id + ".");
            }
            Lector lector = usuario(usuarios, campos[2], Lector.class);
            Administrador admin = usuario(usuarios, campos[3], Administrador.class);
            prestamos.put(id, new Prestamo(id, fecha(campos[1]), lector, admin));
        });
        return prestamos;
    }

    private static Map<Integer, DetallePrestamo> cargarDetalles(Path directorio, Biblioteca biblioteca,
            Map<Integer, Prestamo> prestamos, Map<Integer, Usuario> usuarios,
            Map<Integer, Ejemplar> ejemplares) throws IOException {
        Map<Integer, DetallePrestamo> detalles = new HashMap<>();
        Set<Integer> ids = new HashSet<>();
        Set<Ejemplar> pendientes = new HashSet<>();
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                ids.add(detalle.getIdDetalle());
                if (detalle.getFechaDevolucion() == null) {
                    pendientes.add(detalle.getEjemplar());
                }
            }
        }
        ArchivoTexto.leer(directorio.resolve(DETALLES), 7, campos -> {
            int id = Integer.parseInt(campos[0]);
            if (!ids.add(id)) {
                throw new IllegalArgumentException("ID de detalle repetido: " + id + ".");
            }
            Prestamo prestamo = ArchivoTexto.buscar(prestamos, campos[1], "préstamo de la carga");
            Ejemplar ejemplar = ArchivoTexto.buscar(ejemplares, campos[2], "ejemplar");
            LocalDate limite = fecha(campos[3]);
            if (limite.isBefore(prestamo.getFechaPrestamo())) {
                throw new IllegalArgumentException("La fecha límite es anterior al préstamo.");
            }
            DetallePrestamo detalle = new DetallePrestamo(id, ejemplar, limite);
            if (campos[4].isEmpty()) {
                if (!campos[5].isEmpty() || !campos[6].isEmpty()
                        || !pendientes.add(ejemplar) || !"Prestado".equals(ejemplar.getEstado())) {
                    throw new IllegalArgumentException("Estado o devolución incoherente para el ejemplar " + campos[2] + ".");
                }
            } else {
                LocalDate devolucion = fecha(campos[4]);
                if (devolucion.isBefore(prestamo.getFechaPrestamo())) {
                    throw new IllegalArgumentException("La devolución es anterior al préstamo.");
                }
                Administrador admin = usuario(usuarios, campos[5], Administrador.class);
                detalle.registrarDevolucion(devolucion, admin, campos[6]);
            }
            detalles.put(id, detalle);
            prestamo.agregarDetalle(detalle);
        });
        return detalles;
    }

    private static void cargarPenalizaciones(Path directorio, Biblioteca biblioteca,
            Map<Integer, DetallePrestamo> detalles) throws IOException {
        Set<Integer> ids = new HashSet<>();
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                if (detalle.getPenalizacion() != null) {
                    ids.add(detalle.getPenalizacion().getIdPenalizacion());
                }
            }
        }
        ArchivoTexto.leer(directorio.resolve(PENALIZACIONES), 6, campos -> {
            int id = Integer.parseInt(campos[0]);
            DetallePrestamo detalle = ArchivoTexto.buscar(detalles, campos[1], "detalle de la carga");
            int dias = Integer.parseInt(campos[3]);
            BigDecimal monto = new BigDecimal(campos[4]);
            if (!ids.add(id) || detalle.getPenalizacion() != null) {
                throw new IllegalArgumentException("Penalización repetida.");
            }
            if (detalle.getFechaDevolucion() == null || dias <= 0 || monto.signum() < 0) {
                throw new IllegalArgumentException("La penalización requiere una devolución, días positivos y un monto no negativo.");
            }
            detalle.setPenalizacion(new Penalizacion(id, campos[2], dias, monto, ArchivoTexto.booleano(campos[5])));
        });
    }

    private static <T extends Usuario> T usuario(Map<Integer, Usuario> usuarios, String id, Class<T> rol) {
        Usuario usuario = ArchivoTexto.buscar(usuarios, id, "usuario");
        if (!rol.isInstance(usuario)) {
            throw new IllegalArgumentException("El usuario " + id + " debe ser " + rol.getSimpleName() + ".");
        }
        return rol.cast(usuario);
    }

    private static LocalDate fecha(String campo) {
        try {
            return LocalDate.parse(campo);
        } catch (DateTimeException error) {
            throw new IllegalArgumentException("Fecha inválida: " + campo + ".", error);
        }
    }

    static void guardar(Path directorio, Biblioteca biblioteca) throws IOException {
        ArchivoTexto.escribir(directorio.resolve(PRESTAMOS), salida -> {
            for (Prestamo prestamo : biblioteca.getPrestamos()) {
                ArchivoTexto.fila(salida, prestamo.getIdPrestamo(), prestamo.getFechaPrestamo(),
                        prestamo.getLector().getIdUsuario(), prestamo.getRegistradoPor().getIdUsuario());
            }
        });
        ArchivoTexto.escribir(directorio.resolve(DETALLES), salida -> {
            for (Prestamo prestamo : biblioteca.getPrestamos()) {
                for (DetallePrestamo detalle : prestamo.getDetalles()) {
                    LocalDate devolucion = detalle.getFechaDevolucion();
                    ArchivoTexto.fila(salida, detalle.getIdDetalle(), prestamo.getIdPrestamo(),
                            detalle.getEjemplar().getIdEjemplar(), detalle.getFechaLimite(),
                            devolucion == null ? "" : devolucion,
                            devolucion == null ? "" : detalle.getDevueltoPor().getIdUsuario(),
                            detalle.getObservacion());
                }
            }
        });
        ArchivoTexto.escribir(directorio.resolve(PENALIZACIONES), salida -> {
            for (Prestamo prestamo : biblioteca.getPrestamos()) {
                for (DetallePrestamo detalle : prestamo.getDetalles()) {
                    Penalizacion penalizacion = detalle.getPenalizacion();
                    if (penalizacion != null) {
                        ArchivoTexto.fila(salida, penalizacion.getIdPenalizacion(), detalle.getIdDetalle(),
                                penalizacion.getMotivo(), penalizacion.getDiasRetraso(), penalizacion.getMonto(),
                                penalizacion.getResuelta());
                    }
                }
            }
        });
    }
}
