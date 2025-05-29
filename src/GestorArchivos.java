
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class GestorArchivos {
     private static final String ARCHIVO_PRODUCTOS = "productos.json";
    private static final String ARCHIVO_VENTAS = "ventas.json";
    private static final String ARCHIVO_CONTADORES = "contadores.json";

    // Guarda la lista de productos en un archivo JSON
    public static void guardarProductos(List<Producto> productos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_PRODUCTOS))) {
            writer.println("[");
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                writer.println("  {");
                writer.println("    \"id\": " + p.getId() + ",");
                writer.println("    \"nombre\": \"" + escaparTexto(p.getNombre()) + "\",");
                writer.println("    \"descripcion\": \"" + escaparTexto(p.getDescripcion()) + "\",");
                writer.println("    \"precio\": " + p.getPrecio() + ",");
                writer.println("    \"cantidadEnStock\": " + p.getCantidadEnStock());
                writer.print("  }");
                if (i < productos.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            writer.println("]");
            System.out.println("Productos guardados exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar productos: " + e.getMessage());
        }
    }

    // Carga la lista de productos desde un archivo JSON
    public static List<Producto> cargarProductos() {
        List<Producto> productos = new ArrayList<>();
        File archivo = new File(ARCHIVO_PRODUCTOS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de productos no existe. Iniciando con inventario vacío.");
            return productos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea.trim());
            }
            
            String json = contenido.toString();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1); // Remover corchetes
                if (!json.trim().isEmpty()) {
                    String[] productosJson = dividirProductos(json);
                    
                    for (String productoJson : productosJson) {
                        try {
                            Producto producto = parsearProducto(productoJson.trim());
                            if (producto != null) {
                                productos.add(producto);
                                // Actualizar el contador de IDs
                                if (producto.getId() >= Producto.getContadorIds()) {
                                    Producto.setContadorIds(producto.getId() + 1);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error al parsear producto: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Productos cargados exitosamente: " + productos.size());
        } catch (IOException e) {
            System.out.println("Error al cargar productos: " + e.getMessage());
        }
        
        return productos;
    }

    // Guarda la lista de ventas en un archivo JSON
    public static void guardarVentas(List<Venta> ventas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_VENTAS))) {
            writer.println("[");
            for (int i = 0; i < ventas.size(); i++) {
                Venta v = ventas.get(i);
                writer.println("  {");
                writer.println("    \"idVenta\": " + v.getIdVenta() + ",");
                writer.println("    \"cliente\": \"" + escaparTexto(v.getCliente()) + "\",");
                writer.println("    \"metodoPago\": \"" + escaparTexto(v.getMetodoPago()) + "\",");
                writer.println("    \"total\": " + v.calcularTotal() + ",");
                writer.println("    \"articulos\": [");
                
                List<ArticuloVenta> articulos = v.getArticulos();
                for (int j = 0; j < articulos.size(); j++) {
                    ArticuloVenta art = articulos.get(j);
                    writer.println("      {");
                    writer.println("        \"productoId\": " + art.getProducto().getId() + ",");
                    writer.println("        \"cantidad\": " + art.getCantidad());
                    writer.print("      }");
                    if (j < articulos.size() - 1) {
                        writer.println(",");
                    } else {
                        writer.println();
                    }
                }
                writer.println("    ]");
                writer.print("  }");
                if (i < ventas.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }
            writer.println("]");
            System.out.println("Ventas guardadas exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar ventas: " + e.getMessage());
        }
    }

    // Carga la lista de ventas desde un archivo JSON
    public static List<Venta> cargarVentas(Inventario inventario) {
        List<Venta> ventas = new ArrayList<>();
        File archivo = new File(ARCHIVO_VENTAS);
        
        if (!archivo.exists()) {
            System.out.println("Archivo de ventas no existe. Iniciando sin ventas registradas.");
            return ventas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            StringBuilder contenido = new StringBuilder();
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea.trim());
            }
            
            String json = contenido.toString();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1);
                if (!json.trim().isEmpty()) {
                    String[] ventasJson = dividirVentas(json);
                    
                    for (String ventaJson : ventasJson) {
                        try {
                            Venta venta = parsearVenta(ventaJson.trim(), inventario);
                            if (venta != null) {
                                ventas.add(venta);
                                // Actualizar el contador de ventas
                                if (venta.getIdVenta() >= Venta.getContadorVentas()) {
                                    Venta.setContadorVentas(venta.getIdVenta() + 1);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error al parsear venta: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Ventas cargadas exitosamente: " + ventas.size());
        } catch (IOException e) {
            System.out.println("Error al cargar ventas: " + e.getMessage());
        }
        
        return ventas;
    }

    // Métodos auxiliares para parsear JSON manualmente
    private static String[] dividirProductos(String json) {
        List<String> productos = new ArrayList<>();
        int nivel = 0;
        int inicio = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (nivel == 0) inicio = i;
                nivel++;
            } else if (c == '}') {
                nivel--;
                if (nivel == 0) {
                    productos.add(json.substring(inicio, i + 1));
                }
            }
        }
        
        return productos.toArray(new String[0]);
    }

    private static String[] dividirVentas(String json) {
        return dividirProductos(json); // Mismo algoritmo
    }

    private static Producto parsearProducto(String json) {
        try {
            int id = extraerEntero(json, "id");
            String nombre = extraerTexto(json, "nombre");
            String descripcion = extraerTexto(json, "descripcion");
            double precio = extraerDecimal(json, "precio");
            int cantidadEnStock = extraerEntero(json, "cantidadEnStock");
            
            return new Producto(id, nombre, descripcion, precio, cantidadEnStock);
        } catch (Exception e) {
            System.out.println("Error al parsear producto: " + e.getMessage());
            return null;
        }
    }

    private static Venta parsearVenta(String json, Inventario inventario) {
        try {
            int idVenta = extraerEntero(json, "idVenta");
            String cliente = extraerTexto(json, "cliente");
            String metodoPago = extraerTexto(json, "metodoPago");
            
            Venta venta = new Venta(idVenta, cliente, metodoPago);
            
            // Parsear artículos
            String articulosJson = extraerArray(json, "articulos");
            if (!articulosJson.isEmpty()) {
                String[] articulos = dividirProductos(articulosJson);
                for (String articuloJson : articulos) {
                    int productoId = extraerEntero(articuloJson, "productoId");
                    int cantidad = extraerEntero(articuloJson, "cantidad");
                    
                    Producto producto = inventario.buscarProductoPorId(productoId);
                    if (producto != null) {
                        ArticuloVenta articulo = new ArticuloVenta(producto, cantidad);
                        venta.agregarArticuloSinValidarStock(articulo);
                    }
                }
            }
            
            return venta;
        } catch (Exception e) {
            System.out.println("Error al parsear venta: " + e.getMessage());
            return null;
        }
    }

    // Métodos auxiliares para extraer valores del JSON
    private static String extraerTexto(String json, String campo) {
        String patron = "\"" + campo + "\":\\s*\"";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return "";
        
        inicio += patron.length();
        int fin = json.indexOf("\"", inicio);
        if (fin == -1) return "";
        
        return desescaparTexto(json.substring(inicio, fin));
    }

    private static int extraerEntero(String json, String campo) {
        String patron = "\"" + campo + "\":\\s*";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return 0;
        
        inicio += patron.length();
        int fin = inicio;
        while (fin < json.length() && (Character.isDigit(json.charAt(fin)) || json.charAt(fin) == '-')) {
            fin++;
        }
        
        return Integer.parseInt(json.substring(inicio, fin));
    }

    private static double extraerDecimal(String json, String campo) {
        String patron = "\"" + campo + "\":\\s*";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return 0.0;
        
        inicio += patron.length();
        int fin = inicio;
        while (fin < json.length() && (Character.isDigit(json.charAt(fin)) || json.charAt(fin) == '.' || json.charAt(fin) == '-')) {
            fin++;
        }
        
        return Double.parseDouble(json.substring(inicio, fin));
    }

    private static String extraerArray(String json, String campo) {
        String patron = "\"" + campo + "\":\\s*\\[";
        int inicio = json.indexOf(patron);
        if (inicio == -1) return "";
        
        inicio = json.indexOf("[", inicio);
        int nivel = 1;
        int i = inicio + 1;
        
        while (i < json.length() && nivel > 0) {
            if (json.charAt(i) == '[') nivel++;
            else if (json.charAt(i) == ']') nivel--;
            i++;
        }
        
        return json.substring(inicio + 1, i - 1);
    }

    private static String escaparTexto(String texto) {
        return texto.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String desescaparTexto(String texto) {
        return texto.replace("\\\"", "\"").replace("\\n", "\n").replace("\\r", "\r");
    }
}
