/**
 *
 * @author Marcos Altamirano
 */
class Producto {
     private final String nombre;
    private final String descripcion;
    private final double precio;
    private int cantidadEnStock;
    private final int id;
    private static int contadorIds = 1;

    // Constructor para nuevos productos
    public Producto(String nombre, String descripcion, double precio, int cantidadEnStock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidadEnStock = cantidadEnStock;
        this.id = contadorIds++;
        validarDatos();
    }

    // Constructor para productos cargados desde archivo (con ID específico)
    public Producto(int id, String nombre, String descripcion, double precio, int cantidadEnStock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidadEnStock = cantidadEnStock;
        validarDatos();
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidadEnStock() {
        return cantidadEnStock;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Métodos para manejar el contador de IDs (necesario para la persistencia)
    public static int getContadorIds() {
        return contadorIds;
    }

    public static void setContadorIds(int nuevoContador) {
        contadorIds = nuevoContador;
    }

    // Disminuye el stock del producto
    public void disminuirStock(int cantidad) {
        if (cantidad > 0) {
            this.cantidadEnStock -= cantidad;
        }
    }

    // Aumenta el stock del producto
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.cantidadEnStock += cantidad;
        }
    }

    // Muestra la información del producto
    public void mostrarInformacion() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Descripcion: " + descripcion);
        System.out.println("Precio: $" + precio);
        System.out.println("Cantidad en stock: " + cantidadEnStock);
    }

    // Valida los datos del producto
    private void validarDatos() {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }
        if (cantidadEnStock < 0) {
            throw new IllegalArgumentException("La cantidad en stock no puede ser negativa.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion del producto no puede estar vacia.");
        }
    }
}