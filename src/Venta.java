
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcos Altamirano
 */

class Venta {
    private List<ArticuloVenta> articulos;
    private double total;
    private int idVenta;
    private static int contadorVentas = 1;
    private String cliente; //Se agrega el atributo cliente
    private String metodoPago; //Se agrega el atributo metodoPago

    public Venta(String cliente, String metodoPago) {
        this.articulos = new ArrayList<>();
        this.total = 0.0;
        this.idVenta = contadorVentas++;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        validarDatos();
    }

    public int getIdVenta() {
        return idVenta;
    }

    // Método para agregar un artículo a la venta
    public void agregarArticulo(ArticuloVenta articulo) {
        Producto producto = articulo.getProducto();
        int cantidad = articulo.getCantidad();

        if (producto.getCantidadEnStock() >= cantidad) {
            articulos.add(articulo);
            producto.disminuirStock(cantidad);
            total += articulo.calcularSubtotal();
        } else {
             throw new IllegalArgumentException("No hay suficiente stock de " + producto.getNombre() + ".");
        }
    }

    // Método para calcular el total de la venta
    public double calcularTotal() {
        return total;
    }

    // Método para mostrar los detalles de la venta
    public void mostrarDetalles() {
        System.out.println("\nDetalles de la Venta #" + idVenta);
        System.out.println("Cliente: " + cliente);
        System.out.println("Método de Pago: " + metodoPago);
        System.out.println("Artículos:");
        for (ArticuloVenta articulo : articulos) {
            System.out.println(articulo.getProducto().getNombre() + " x" + articulo.getCantidad() + " = $" + articulo.calcularSubtotal());
        }
        System.out.println("Total: $" + total);
    }
    private void validarDatos() {
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente no puede estar vacío.");
        }
        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            throw new IllegalArgumentException("El método de pago no puede estar vacío.");
        }
    }
}

