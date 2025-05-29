
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaPuntoDeVenta {

    private static final Inventario inventario = new Inventario();
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Venta> ventas = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Sistema de Punto de Venta ===");
        System.out.println("Cargando datos...");
        
        // Cargar datos desde archivos
        cargarDatos();
        
        // Si no hay productos, ofrecer cargar datos de ejemplo
        if (!inventario.tieneProductos()) {
            System.out.println("\nNo se encontraron productos guardados.");
            System.out.print("¿Desea cargar productos de ejemplo? (s/n): ");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                inicializarInventario();
            }
        }

        // Agregar hook para guardar datos al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nGuardando datos...");
            guardarDatos();
            System.out.println("Datos guardados exitosamente.");
        }));

        // Menú principal del sistema
        while (true) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            ejecutarOpcion(opcion);
        }
    }

    // Carga datos desde archivos
    private static void cargarDatos() {
        try {
            // Cargar inventario
            inventario.cargarDesdeArchivo();
            
            // Cargar ventas
            List<Venta> ventasGuardadas = GestorArchivos.cargarVentas(inventario);
            ventas.addAll(ventasGuardadas);
            
            System.out.println("Datos cargados exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al cargar datos: " + e.getMessage());
        }
    }

    // Guarda datos en archivos
    private static void guardarDatos() {
        try {
            // Guardar inventario
            inventario.guardarEnArchivo();
            
            // Guardar ventas
            GestorArchivos.guardarVentas(ventas);
        } catch (Exception e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    // Añade información al inventario con productos de ejemplo
    private static void inicializarInventario() {
        try {
            inventario.agregarProducto(new Producto("Laptop", "Computadora portatil", 1200.0, 10));
            inventario.agregarProducto(new Producto("Mouse", "Mouse inalámbrico", 25.0, 50));
            inventario.agregarProducto(new Producto("Teclado", "Teclado mecanico", 75.0, 30));
            inventario.agregarProducto(new Producto("Monitor", "Monitor 27 pulgadas", 300.0, 15));
            inventario.agregarProducto(new Producto("Impresora", "Impresora laser", 200.0, 5));
            System.out.println("Productos de ejemplo agregados al inventario.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al inicializar el inventario: " + e.getMessage());
        }
    }

    // Muestra el menu principal
    private static void mostrarMenu() {
        System.out.println("\n=== Sistema de Punto de Venta ===");
        System.out.println("1. Mostrar inventario");
        System.out.println("2. Agregar producto al inventario");
        System.out.println("3. Actualizar stock de producto");
        System.out.println("4. Realizar venta");
        System.out.println("5. Mostrar detalles de venta");
        System.out.println("6. Mostrar todas las ventas");
        System.out.println("7. Guardar datos manualmente");
        System.out.println("8. Salir");
        System.out.print("Ingrese su opcion: ");
    }

    // Obtiene la opcion del usuario
    private static int obtenerOpcion() {
        while (true) {
            try {
                String input = scanner.nextLine();
                int opcion = Integer.parseInt(input);
                return opcion;
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Por favor, ingrear un numero: ");
            }
        }
    }

    // Ejecuta la opcion seleccionada por el usuario
    private static void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    inventario.mostrarInventario();
                    break;
                case 2:
                    agregarProducto();
                    break;
                case 3:
                    actualizarStockProducto();
                    break;
                case 4:
                    realizarVenta();
                    break;
                case 5:
                    mostrarDetallesDeVenta();
                    break;
                case 6:
                    mostrarTodasLasVentas();
                    break;
                case 7:
                    System.out.println("Guardando datos...");
                    guardarDatos();
                    System.out.println("Datos guardados exitosamente.");
                    break;
                case 8:
                    System.out.println("Guardando datos antes de salir...");
                    guardarDatos();
                    System.out.println("Saliendo del sistema...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción invalida. Por favor, intente de nuevo.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Agregar un nuevo producto al inventario
    private static void agregarProducto() {
        System.out.println("\nAgregar Producto al Inventario");
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la descripción del producto: ");
        String descripcion = scanner.nextLine();
        double precio = obtenerPrecioValido();
        int cantidadEnStock = obtenerCantidadValida();

        try {
            Producto nuevoProducto = new Producto(nombre, descripcion, precio, cantidadEnStock);
            inventario.agregarProducto(nuevoProducto);
            System.out.println("Producto agregado al inventario exitosamente.");
            
            // Guardar automáticamente después de agregar
            System.out.println("Guardandoproducto...");
            inventario.guardarEnArchivo();
        } catch (IllegalArgumentException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        }
    }

    // Valida y obtiene precio válido del usuario
    private static double obtenerPrecioValido() {
        double precio = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print("Ingrese el precio del producto: $");
            try {
                precio = Double.parseDouble(scanner.nextLine());
                if (precio >= 0) {
                    valido = true;
                } else {
                    System.out.println("El precio debe ser mayor o igual a cero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero valido.");
            }
        }
        return precio;
    }

    // Valida y obtiene cantidad de stock del usuario
    private static int obtenerCantidadValida() {
        int cantidad = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print("Ingrese la cantidad en stock: ");
            try {
                cantidad = Integer.parseInt(scanner.nextLine());
                if (cantidad >= 0) {
                    valido = true;
                } else {
                    System.out.println("La cantidad debe ser mayor o igual a cero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número entero valido.");
            }
        }
        return cantidad;
    }

    // Actualiza el stock de un producto
    private static void actualizarStockProducto() {
        if (!inventario.tieneProductos()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }

        inventario.mostrarInventario();
        System.out.print("Ingrese el ID del producto a actualizar: ");
        int idProducto = obtenerOpcion();
        Producto producto = inventario.buscarProductoPorId(idProducto);

        if (producto == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.print("Ingrese la cantidad a aumentar o disminuir (negativo para disminuir): ");
        int cantidad = obtenerOpcion();

        inventario.actualizarStock(idProducto, Math.abs(cantidad), cantidad > 0);
        
        // Guardar automáticamente después de actualizar
        System.out.println("Guardando cambios...");
        inventario.guardarEnArchivo();
    }

    // Realiza la venta de un producto
    private static void realizarVenta() {
        if (!inventario.tieneProductos()) {
            System.out.println("No hay productos en el inventario para realizar una venta.");
            return;
        }

        System.out.print("Ingrese el nombre del cliente: ");
        String cliente = scanner.nextLine();
        System.out.print("Ingrese el método de pago: ");
        String metodoPago = scanner.nextLine();
        
        Venta venta;
        try {
            venta = new Venta(cliente, metodoPago);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear la venta: " + e.getMessage());
            return;
        }

        boolean ventaRealizada = false;
        while (true) {
            inventario.mostrarInventario();
            System.out.print("Ingrese el ID del producto a agregar a la venta (0 para terminar): ");
            int idProducto = obtenerOpcion();

            if (idProducto == 0) {
                break;
            }

            Producto producto = inventario.buscarProductoPorId(idProducto);
            if (producto != null) {
                System.out.print("Ingrese la cantidad: ");
                int cantidad = obtenerOpcion();
                try {
                    ArticuloVenta articulo = new ArticuloVenta(producto, cantidad);
                    venta.agregarArticulo(articulo);
                    System.out.println("Articulo agregado a la venta.");
                    ventaRealizada = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Error al agregar articulo: " + e.getMessage());
                }
            } else {
                System.out.println("Producto no encontrado.");
            }
        }
        
        if (ventaRealizada && venta.calcularTotal() > 0) {
            ventas.add(venta);
            venta.mostrarDetalles();
            System.out.println("Venta realizada con exito.");
            
            // Guardar automáticamente después de realizar venta
            System.out.println("Guardando venta y actualizando inventario...");
            guardarDatos();
        } else {
            System.out.println("Venta cancelada, no se agregaron productos.");
        }
    }

    // Método para mostrar los detalles de una venta
    private static void mostrarDetallesDeVenta() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.print("Ingrese el ID de la venta a mostrar: ");
        int idVenta = obtenerOpcion();

        for (Venta venta : ventas) {
            if (venta.getIdVenta() == idVenta) {
                venta.mostrarDetalles();
                return;
            }
        }
        System.out.println("Venta no encontrada.");
    }

    // Muestra todas las ventas
    private static void mostrarTodasLasVentas() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        System.out.println("\n=== Todas las Ventas ===");
        for (Venta venta : ventas) {
            System.out.println("--------------------");
            venta.mostrarDetalles();
        }
        System.out.println("--------------------");
        System.out.println("Total de ventas registradas: " + ventas.size());
    }
}
