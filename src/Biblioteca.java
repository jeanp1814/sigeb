import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class Biblioteca {
    private String nombre;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Libro> libros;
    private ArrayList<Prestamo> prestamos;
    private PoliticaPrestamo politica;

    public Biblioteca(String nombre, PoliticaPrestamo politica) {
        this.nombre = nombre;
        this.politica = politica;
        usuarios = new ArrayList<>();
        libros = new ArrayList<>();
        prestamos = new ArrayList<>();
    }

    public void registrarUsuario(Usuario usuario) {
        for (Usuario actual : usuarios) {
            if (actual.getIdUsuario() == usuario.getIdUsuario() || actual.getDocumento().equals(usuario.getDocumento())) {
                System.out.println("El ID o documento ya esta registrado.");
                return;
            }
        }

        usuarios.add(usuario);
    }

    public void actualizarUsuario(Usuario usuario) {
        for (Usuario actual : usuarios) {
            if (actual.getIdUsuario() == usuario.getIdUsuario()) {
                actual.actualizarDatos(
                        usuario.getNombres(),
                        usuario.getApellidos(),
                        usuario.getCorreo()
                );
                return;
            }
        }

        System.out.println("Usuario no registrado.");
    }

    public void desactivarUsuario(int id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getIdUsuario() == id) {
                usuario.desactivar();
                return;
            }
        }

        System.out.println("Usuario no registrado.");
    }

    public void registrarLibro(Libro libro) {
        for (Libro actual : libros) {
            if (actual.getIdLibro() == libro.getIdLibro() || actual.getIsbn().equals(libro.getIsbn())) {
                System.out.println("El ID o ISBN ya esta registrado.");
                return;
            }
        }

        libros.add(libro);
    }

    public void actualizarLibro(Libro libro) {
        for (Libro actual : libros) {
            if (actual.getIdLibro() == libro.getIdLibro()) {
                actual.actualizarDatos(libro.getTitulo(),libro.getEditorial(),libro.getAñoPublicacion());
                return;
            }
        }

        System.out.println("Libro no registrado.");
    }

    public void registrarEjemplar(Libro libro, Ejemplar ejemplar) {
        if (!libros.contains(libro)) {
            System.out.println("Libro no registrado.");
            return;
        }

        for (Libro actual : libros) {
            for (Ejemplar copia : actual.getEjemplares()) {
                if (copia.getIdEjemplar() == ejemplar.getIdEjemplar() || copia.getCodigoInventario().equals(ejemplar.getCodigoInventario())) {
                    System.out.println("El ejemplar ya esta registrado.");
                    return;
                }
            }
        }

        libro.agregarEjemplar(ejemplar);
    }

    public void darDeBajaEjemplar(Ejemplar ejemplar) {
        boolean registrado = false;

        for (Libro libro : libros) {
            if (libro.getEjemplares().contains(ejemplar)) {
                registrado = true;
            }
        }

        if (!registrado) {
            System.out.println("Ejemplar no registrado.");
            return;
        }

        for (Prestamo prestamo : prestamos) {
            for (DetallePrestamo detalle : prestamo.obtenerPendientes()) {
                if (detalle.getEjemplar() == ejemplar) {
                    System.out.println("El ejemplar tiene un prestamo pendiente.");
                    return;
                }
            }
        }

        ejemplar.cambiarEstado("Baja");
    }

    public ArrayList<Libro> buscarLibros(String campo, String texto) {
        ArrayList<Libro> resultados = new ArrayList<>();
        String consulta = texto.toLowerCase(Locale.ROOT);
        String tipo = campo.toLowerCase(Locale.ROOT);

        if (!tipo.equals("titulo") && !tipo.equals("isbn") && !tipo.equals("editorial") && !tipo.equals("autor") && !tipo.equals("categoria")) {
            System.out.println("Campo de busqueda no valido.");
            return null;
        }

        for (Libro libro : libros) {
            String valor = "";

            switch (tipo) {
                case "titulo":
                    valor = libro.getTitulo();
                    break;
                case "isbn":
                    valor = libro.getIsbn();
                    break;
                case "editorial":
                    valor = libro.getEditorial();
                    break;
                case "categoria":
                    valor = libro.getCategoria().getNombre();
                    break;
                case "autor":
                    for (Autor autor : libro.getAutores()) {
                        valor += autor.getNombreCompleto() + " ";
                    }
                    break;
            }

            if (valor.toLowerCase(Locale.ROOT).contains(consulta)) {
                resultados.add(libro);
            }
        }

        return resultados;
    }

    public Prestamo registrarPrestamo(Lector lector, ArrayList<Ejemplar> ejemplares, Administrador admin) {
        if (admin == null || !usuarios.contains(admin) || !admin.getActivo()) {
            System.out.println("El administrador debe estar registrado y activo.");
            return null;
        }

        if (!usuarios.contains(lector) || !lector.getActivo()) {
            System.out.println("El lector debe estar registrado y activo.");
            return null;
        }

        if (ejemplares == null || ejemplares.isEmpty()) {
            System.out.println("Selecciona al menos un ejemplar.");
            return null;
        }

        int activos = 0;

        for (Prestamo prestamo : consultarHistorial(lector)) {
            activos += prestamo.obtenerPendientes().size();
        }

        if (activos + ejemplares.size() > politica.getMaximoEjemplaresActivos()) {
            System.out.println("Se supera el maximo de ejemplares activos.");
            return null;
        }

        if (politica.getBloquearPorPenalizacion()) {
            for (Penalizacion penalizacion : consultarPenalizaciones(lector)) {
                if (!penalizacion.getResuelta()) {
                    System.out.println("El lector tiene penalizaciones pendientes.");
                    return null;
                }
            }
        }

        ArrayList<Ejemplar> seleccionados = new ArrayList<>();

        for (Ejemplar ejemplar : ejemplares) {
            boolean registrado = false;

            for (Libro libro : libros) {
                if (libro.getEjemplares().contains(ejemplar)) {
                    registrado = true;
                }
            }

            if (ejemplar == null || !registrado || !ejemplar.estaDisponible() || seleccionados.contains(ejemplar)) {
                System.out.println("Ejemplar no registrado, repetido o no disponible.");
                return null;
            }

            seleccionados.add(ejemplar);
        }

        LocalDate fecha = LocalDate.now();

        Prestamo prestamo = new Prestamo(prestamos.size() + 1, fecha, lector, admin);

        int idDetalle = 1;

        for (Prestamo anterior : prestamos) {
            idDetalle += anterior.getDetalles().size();
        }

        for (Ejemplar ejemplar : seleccionados) {
            DetallePrestamo detalle = new DetallePrestamo(idDetalle++,ejemplar, politica.calcularFechaLimite(fecha));

            prestamo.agregarDetalle(detalle);
            ejemplar.cambiarEstado("Prestado");
        }

        prestamos.add(prestamo);
        return prestamo;
    }

    public void registrarDevolucion(DetallePrestamo detalle,LocalDate fecha,Administrador admin) {
        if (admin == null || !usuarios.contains(admin) || !admin.getActivo()) {
            System.out.println("El administrador debe estar registrado y activo.");
            return;
        }

        Prestamo propietario = null;
        int idPenalizacion = 1;

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getDetalles().contains(detalle)) {
                propietario = prestamo;
            }

            for (DetallePrestamo actual : prestamo.getDetalles()) {
                if (actual.getPenalizacion() != null) {
                    idPenalizacion++;
                }
            }
        }

        if (propietario == null || detalle.getFechaDevolucion() != null) {
            System.out.println("Detalle no registrado o ya devuelto.");
            return;
        }

        if (fecha == null || fecha.isBefore(propietario.getFechaPrestamo()) || fecha.isAfter(LocalDate.now())) {
            System.out.println("Fecha de devolucion no valida.");
            return;
        }

        int dias = detalle.calcularDiasRetraso(fecha);

        if (dias > 0) {
            Penalizacion penalizacion = new Penalizacion(idPenalizacion, "Devolucion tardia", dias);

            penalizacion.calcularMonto(politica.getPenalizacionPorDia());
            detalle.setPenalizacion(penalizacion);
        }

        detalle.registrarDevolucion(fecha, admin, "Devolucion registrada");

        detalle.getEjemplar().cambiarEstado("Disponible");
    }

    public ArrayList<Prestamo> consultarHistorial(Lector lector) {
        ArrayList<Prestamo> historial = new ArrayList<>();

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getLector() == lector) {
                historial.add(prestamo);
            }
        }

        return historial;
    }

    public ArrayList<DetallePrestamo> consultarVencidos(LocalDate fecha) {
        ArrayList<DetallePrestamo> vencidos = new ArrayList<>();

        for (Prestamo prestamo : prestamos) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                if (detalle.estaVencido(fecha)) {
                    vencidos.add(detalle);
                }
            }
        }

        return vencidos;
    }

    public ArrayList<Penalizacion> consultarPenalizaciones(Lector lector) {
        ArrayList<Penalizacion> penalizaciones = new ArrayList<>();

        for (Prestamo prestamo : consultarHistorial(lector)) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                if (detalle.getPenalizacion() != null) {
                    penalizaciones.add(detalle.getPenalizacion());
                }
            }
        }

        return penalizaciones;
    }

    public void resolverPenalizacion(Penalizacion penalizacion) {
        for (Prestamo prestamo : prestamos) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                if (detalle.getPenalizacion() == penalizacion && penalizacion != null) {
                    penalizacion.resolver();
                    return;
                }
            }
        }

        System.out.println("Penalizacion no registrada.");
    }

    public String getNombre() {
        return nombre;
    }

    public PoliticaPrestamo getPolitica() {
        return politica;
    }

    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public ArrayList<Libro> getLibros() {
        return new ArrayList<>(libros);
    }

    public ArrayList<Prestamo> getPrestamos() {
        return new ArrayList<>(prestamos);
    }
}