// com.gestorempresarial.controlador.FacturaControlador.java
package main.java.com.gestorempresarial.controlador;

import com.gestorempresarial.dao.*;
import com.gestorempresarial.modelo.*;
import java.util.List;

public class FacturaControlador {
    
    private FacturaDAO facturaDAO;
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private DetalleFacturaDAO detalleDAO;
    private RegistroGeneralDAO registroGeneralDAO;
    
    public FacturaControlador() {
        this.facturaDAO = new FacturaDAO();
        this.clienteDAO = new ClienteDAO();
        this.productoDAO = new ProductoDAO();
        this.detalleDAO = new DetalleFacturaDAO();
        this.registroGeneralDAO = new RegistroGeneralDAO();
    }
    
    // Crear nueva factura
    public boolean crearFactura(Factura factura) {
        if (factura.getCliente() == null || factura.getDetalles().isEmpty()) {
            System.err.println("Error: Factura sin cliente o sin productos");
            return false;
        }
        
        factura.calcularTotales();
        factura.emitir();
        
        boolean resultado = facturaDAO.insertarFactura(factura);
        
        if (resultado) {
            // Crear registro general centralizado (HU-08)
            RegistroGeneral registro = new RegistroGeneral(
                RegistroGeneral.TIPO_FACTURA,
                "Factura emitida: " + factura.getNumeroFactura() + " - Total: $" + factura.getTotal(),
                factura.getIdFactura(),
                "Factura"
            );
            registro.registrarRegistro();
            registroGeneralDAO.insertarRegistro(registro);
        }
        
        return resultado;
    }
    
    // Obtener factura por ID
    public Factura obtenerFactura(int id) {
        return facturaDAO.obtenerFacturaPorId(id);
    }
    
    // Listar todas las facturas
    public List<Factura> listarFacturas() {
        return facturaDAO.obtenerTodasFacturas();
    }
    
    // Anular factura
    public boolean anularFactura(int id) {
        Factura factura = facturaDAO.obtenerFacturaPorId(id);
        if (factura != null) {
            factura.anular();
            boolean resultado = facturaDAO.anularFactura(id);
            
            if (resultado) {
                // Actualizar registro general
                List<RegistroGeneral> registros = registroGeneralDAO.obtenerRegistrosPorEntidad("Factura", id);
                if (!registros.isEmpty()) {
                    RegistroGeneral registro = registros.get(0);
                    registro.setDescripcion("Factura anulada: " + factura.getNumeroFactura());
                    registro.actualizarRegistro();
                    registroGeneralDAO.actualizarRegistro(registro);
                }
            }
            
            return resultado;
        }
        return false;
    }
    
    // Agregar producto a factura (sin guardar)
    public void agregarProductoAFactura(Factura factura, int idProducto, int cantidad) {
        Producto producto = productoDAO.obtenerProductoPorId(idProducto);
        if (producto != null && producto.getStock() >= cantidad) {
            DetalleFactura detalle = new DetalleFactura(producto, cantidad);
            factura.agregarDetalle(detalle);
            System.out.println("✓ Producto agregado: " + producto.getNombre());
        } else if (producto != null) {
            System.err.println("❌ Stock insuficiente. Disponible: " + producto.getStock());
        } else {
            System.err.println("❌ Producto no encontrado");
        }
    }
    
    // Validar valores (corrección automática de errores - HU-02)
    public boolean validarValorFactura(double valor) {
        if (valor <= 0) {
            System.err.println("❌ Error: El valor debe ser mayor a cero");
            return false;
        }
        
        // Detectar posibles errores (ceros de más)
        String valorStr = String.valueOf(valor);
        if (valorStr.contains(".") && valorStr.split("\\.")[0].length() > 9) {
            System.err.println("⚠️ Alerta: Valor inusualmente alto ($" + valor + "). ¿Verificar?");
            return false;
        }
        
        // Validar valores atípicos (muy grandes)
        if (valor > 100000000) { // Más de 100 millones
            System.err.println("⚠️ Alerta: Valor superior a $100,000,000. Verifique que sea correcto.");
        }
        
        return true;
    }
    
    // Generar reporte contable (HU-06)
    public void generarReporteVentas(String periodo) {
        List<Factura> facturas = facturaDAO.obtenerTodasFacturas();
        
        System.out.println("\n=== REPORTE CONTABLE - " + periodo + " ===");
        System.out.println("┌─────────┬──────────────┬──────────┬────────────┬───────────┬────────────┐");
        System.out.println("│ Código  │ Producto     │ Cantidad │ Valor Base │ Impuestos │ Total      │");
        System.out.println("├─────────┼──────────────┼──────────┼────────────┼───────────┼────────────┤");
        
        double totalGeneral = 0;
        for (Factura factura : facturas) {
            for (DetalleFactura detalle : factura.getDetalles()) {
                Producto p = detalle.getProducto();
                System.out.printf("│ %-7s │ %-12s │ %8d │ $%9.2f │ $%7.2f │ $%10.2f │%n",
                    p.getCodigo(),
                    p.getNombre().length() > 12 ? p.getNombre().substring(0, 10) + ".." : p.getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubtotal(),
                    detalle.getValorIva(),
                    detalle.getTotal());
                totalGeneral += detalle.getTotal();
            }
        }
        System.out.println("├─────────┼──────────────┼──────────┼────────────┼───────────┼────────────┤");
        System.out.printf("│         │              │          │            │ TOTAL     │ $%10.2f │%n", totalGeneral);
        System.out.println("└─────────┴──────────────┴──────────┴────────────┴───────────┴────────────┘");
    }
    
    // Obtener facturas de un cliente específico
    public List<Factura> listarFacturasPorCliente(int idCliente) {
        return facturaDAO.obtenerFacturasPorCliente(idCliente);
    }
    
    // Obtener total de ventas en un período
    public double obtenerTotalVentas() {
        List<Factura> facturas = facturaDAO.obtenerTodasFacturas();
        double total = 0;
        for (Factura f : facturas) {
            if (!"ANULADA".equals(f.getEstado())) {
                total += f.getTotal();
            }
        }
        return total;
    }
}