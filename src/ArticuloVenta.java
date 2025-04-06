/**
 *
 * @author Marcos Altamirano
 */
class ArticuloVenta {
    private Producto producto;
    private int cantidad;

    public ArticuloVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        validarDatos();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }
    private void validarDatos() {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de artículos debe ser mayor que cero.");
        }
    }
}