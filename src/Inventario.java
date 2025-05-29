
import java.util.ArrayList;
import java.util.List;

class Inventario {
   private final List<Producto> productos;
    
    // Constructor
    public Inventario() {
        this.productos = new ArrayList<>();
    }

    // Obtiene la lista de productos (para persistencia)
    public List<Producto> getProductos() {
        return new ArrayList<>(productos); // Retorna una copia
    }

    // Carga productos desde una lista (usado al cargar desde archivo)
    public void cargarProductos(List<Producto> productosGuardados) {
        this.productos.clear();
        this.productos.addAll(productosGuardados);
    }

    // Agregar un producto al inventario
    public void agregarProducto(Producto producto) {
        if (buscarProductoPorNombre(producto.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el nombre '" + producto.getNombre() + "'.");
        }
        this.productos.add(producto);
    }

    // Método para buscar un producto por su ID
    public Producto buscarProductoPorId(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    // Método para buscar un producto por su nombre
    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    // Verifica si el inventario tiene productos
    public boolean tieneProductos() {
        return !productos.isEmpty();
    }

    // Muestra todos los productos del inventario
    public void mostrarInventario() {
        if (productos.isEmpty()) {
            System.out.println("El inventario está vacio.");
            return;
        }
        System.out.println("\nInventario:");
        for (Producto producto : productos) {
            System.out.println("--------------------");
            producto.mostrarInformacion();
        }
        System.out.println("--------------------");
    }

    // Actualiza el stock de un producto por su ID
    public void actualizarStock(int id, int cantidad, boolean aumentar) {
        Producto producto = buscarProductoPorId(id);
        if (producto != null) {
            if (aumentar) {
                producto.aumentarStock(cantidad);
                System.out.println("Stock de " + producto.getNombre() + " aumentado en " + cantidad + ".");
            } else {
                if (producto.getCantidadEnStock() >= cantidad) {
                    producto.disminuirStock(cantidad);
                    System.out.println("Stock de " + producto.getNombre() + " disminuido en " + cantidad + ".");
                } else {
                    System.out.println("No hay suficiente stock de " + producto.getNombre() + " para disminuir " + cantidad + " unidades.");
                }
            }
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    // Obtiene el precio de un producto por su ID
    public double obtenerPrecioProducto(int id) {
        Producto producto = buscarProductoPorId(id);
        if (producto != null) {
            return producto.getPrecio();
        }
        return -1;
    }

    // Guarda el inventario en archivo
    public void guardarEnArchivo() {
        GestorArchivos.guardarProductos(productos);
    }

    // Carga el inventario desde archivo
    public void cargarDesdeArchivo() {
        List<Producto> productosGuardados = GestorArchivos.cargarProductos();
        cargarProductos(productosGuardados);
    }
}