/**
 *
 * @author Marcos Altamirano
 */
class Producto {
    private final String nombre;
    private final String descripcion;
    private final double precio;
    private int cantidadEnStock;
    private final int id;  // Identificador único para cada producto
    private static int contadorIds = 1; // Contador para generar IDs únicos

    // Constructor de la clase Producto
    public Producto(String nombre, String descripcion, double precio, int cantidadEnStock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidadEnStock = cantidadEnStock;
        this.id = contadorIds++; // Asigna el ID y luego incrementa el contador
        validarDatos(); // Valida los datos del producto al crearlo
    }

    // Métodos getter para acceder a los atributos del producto
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

    // Método para disminuir el stock del producto
    public void disminuirStock(int cantidad) {
        if (cantidad > 0) {
            this.cantidadEnStock -= cantidad;
        }
    }

    // Método para aumentar el stock del producto
    public void aumentarStock(int cantidad) {
        if (cantidad > 0) {
            this.cantidadEnStock += cantidad;
        }
    }

    // Método para mostrar la información del producto
    public void mostrarInformacion() {
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Precio: $" + precio);
        System.out.println("Cantidad en Stock: " + cantidadEnStock);
    }

    // Método privado para validar los datos del producto
    private void validarDatos() {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }
        if (cantidadEnStock < 0) {
            throw new IllegalArgumentException("La cantidad en stock no puede ser negativa.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto no puede estar vacía.");
        }
    }
}