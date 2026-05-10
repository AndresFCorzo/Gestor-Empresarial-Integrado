package com.gestorempresarial.console.controlador;

import com.gestorempresarial.dao.FacturaDAO;
import com.gestorempresarial.dao.ProductoDAO;
import com.gestorempresarial.modelo.Cliente;
import com.gestorempresarial.modelo.DetalleFactura;
import com.gestorempresarial.modelo.Factura;
import com.gestorempresarial.modelo.Producto;
import com.gestorempresarial.utils.Validaciones;
import java.util.List;

/**
 * Controlador para la gestión de facturación (Versión Consola)
 * @author Andres Felipe Corzo Angarita
 */
public class FacturaControlador {
    
    private FacturaDAO facturaDAO;
    private ProductoDAO productoDAO;
    
    public FacturaControlador() {
        this.facturaDAO = new FacturaDAO();
        this.productoDAO = new ProductoDAO();
    }
    
    /**
     * Crear una nueva factura
     * @param cliente Cliente asociado
     * @param numeroFactura Número de factura
     * @param detalles Lista de detalles de la factura
     * @return Mensaje de resultado
     */
    public String crearFactura(Cliente cliente, String numeroFactura, List<DetalleFactura> detalles) {
        if (cliente == null) {
            return "❌ Cliente no válido";
        }
        
        if (detalles == null || detalles.isEmpty()) {
            return "❌ La factura debe tener al menos un producto";
        }
        
        // Validar stock antes de crear la factura
        for (DetalleFactura detalle : detalles) {
            Producto p = detalle.getProducto();
            if (p.getStock() < detalle.getCantidad()) {
                return "❌ Stock insuficiente para el producto: " + p.getNombre() + " (Disponible: " + p.getStock() + ")";
            }
        }
        
        Factura factura = new Factura(cliente, numeroFactura);
        for (DetalleFactura detalle : detalles) {
            factura.agregarDetalle(detalle);
        }
        factura.emitir();
        
        if (facturaDAO.insertar(factura)) {
            // Actualizar stock
            for (DetalleFactura detalle : detalles) {
                Producto p = detalle.getProducto();
                int nuevoStock = p.getStock() - detalle.getCantidad();
                productoDAO.actualizarStock(p.getIdProducto(), nuevoStock);
            }
            return "✅ Factura emitida exitosamente\n   Número: " + factura.getNumeroFactura() + "\n   Total: $" + factura.getTotal();
        } else {
            return "❌ Error al emitir la factura";
        }
    }
    
    /**
     * Listar todas las facturas
     * @return Lista de facturas
     */
    public List<Factura> listarFacturas() {
        return facturaDAO.listarTodas();
    }
    
    /**
     * Buscar factura por ID
     * @param id ID de la factura
     * @return Factura encontrada o null
     */
    public Factura buscarFacturaPorId(int id) {
        if (id <= 0) {
            System.out.println("❌ ID inválido");
            return null;
        }
        return facturaDAO.buscarPorId(id);
    }
    
    /**
     * Anular una factura
     * @param id ID de la factura
     * @return Mensaje de resultado
     */
    public String anularFactura(int id) {
        Factura factura = facturaDAO.buscarPorId(id);
        if (factura == null) {
            return "❌ Factura no encontrada";
        }
        
        if (factura.getEstado().equals(Factura.ESTADO_ANULADA)) {
            return "❌ La factura ya está anulada";
        }
        
        if (facturaDAO.anular(id)) {
            return "✅ Factura anulada exitosamente";
        } else {
            return "❌ Error al anular la factura";
        }
    }
    
    /**
     * Listar facturas de un cliente específico
     * @param idCliente ID del cliente
     * @return Lista de facturas del cliente
     */
    public List<Factura> listarFacturasPorCliente(int idCliente) {
        return facturaDAO.listarPorCliente(idCliente);
    }
    
    /**
     * Validar valor de factura (corrección automática de errores)
     * @param valor Valor a validar
     * @return true si el valor parece correcto
     */
    public boolean validarValorFactura(double valor) {
        return Validaciones.validarValorAtipico(valor);
    }
    
    /**
     * Mostrar información detallada de una factura
     * @param factura Factura a mostrar
     */
    public void mostrarDetalleFactura(Factura factura) {
        if (factura == null) {
            System.out.println("❌ Factura no encontrada");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              FACTURA DE VENTA                               ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s: %-50s ║%n", "Número", factura.getNumeroFactura());
        System.out.printf("║ %-20s: %-50s ║%n", "Fecha", factura.getFecha());
        System.out.printf("║ %-20s: %-50s ║%n", "Estado", factura.getEstado());
        System.out.printf("║ %-20s: %-50s ║%n", "Cliente", factura.getCliente().getNombre());
        System.out.printf("║ %-20s: %-50s ║%n", "NIT", factura.getCliente().getNit());
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║                             DETALLE DE PRODUCTOS                           ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        
        for (DetalleFactura d : factura.getDetalles()) {
            System.out.printf("║ %-30s x%-4d = $%-30.2f ║%n", 
                d.getProducto().getNombre().length() > 30 ? d.getProducto().getNombre().substring(0, 27) + "..." : d.getProducto().getNombre(),
                d.getCantidad(),
                d.getTotal());
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s: $%-50.2f ║%n", "SUBTOTAL", factura.getSubtotal());
        System.out.printf("║ %-20s: $%-50.2f ║%n", "IVA", factura.getTotalIva());
        System.out.printf("║ %-20s: $%-50.2f ║%n", "TOTAL", factura.getTotal());
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Mostrar lista de facturas en formato tabla
     * @param facturas Lista de facturas
     */
    public void mostrarListaFacturas(List<Factura> facturas) {
        if (facturas.isEmpty()) {
            System.out.println("📋 No hay facturas registradas");
            return;
        }
        
        System.out.println("\n┌─────┬──────────────────┬────────────┬──────────────────────────┬────────────┬───────────┐");
        System.out.println("│ ID  │ N° FACTURA       │ FECHA      │ CLIENTE                  │ TOTAL      │ ESTADO    │");
        System.out.println("├─────┼──────────────────┼────────────┼──────────────────────────┼────────────┼───────────┤");
        for (Factura f : facturas) {
            System.out.printf("│ %-3d │ %-16s │ %-10s │ %-24s │ $%-9.2f │ %-9s │%n",
                f.getIdFactura(),
                f.getNumeroFactura(),
                f.getFecha().toString(),
                f.getCliente().getNombre().length() > 24 ? f.getCliente().getNombre().substring(0, 21) + "..." : f.getCliente().getNombre(),
                f.getTotal(),
                f.getEstado());
        }
        System.out.println("└─────┴──────────────────┴────────────┴──────────────────────────┴────────────┴───────────┘");
    }
    
    /**
     * Generar reporte de ventas
     * @param periodo Período del reporte
     */
    public void generarReporteVentas(String periodo) {
        List<Factura> facturas = facturaDAO.listarTodas();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        REPORTE CONTABLE DE VENTAS                          ║");
        System.out.printf("║                              Período: %-36s ║%n", periodo);
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("║ CÓDIGO │ PRODUCTO                      │ CANTIDAD │ VALOR BASE │ IVA     │ TOTAL     ║");
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        
        double totalGeneral = 0;
        for (Factura factura : facturas) {
            for (DetalleFactura detalle : factura.getDetalles()) {
                Producto p = detalle.getProducto();
                System.out.printf("║ %-6s │ %-28s │ %8d │ $%9.2f │ $%7.2f │ $%9.2f ║%n",
                    p.getCodigo(),
                    p.getNombre().length() > 28 ? p.getNombre().substring(0, 25) + "..." : p.getNombre(),
                    detalle.getCantidad(),
                    detalle.getSubtotal(),
                    detalle.getValorIva(),
                    detalle.getTotal());
                totalGeneral += detalle.getTotal();
            }
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-68s $%9.2f ║%n", "TOTAL GENERAL", totalGeneral);
        System.out.println("╚════════════════════════════════════════════════════════════════════════════╝");
    }
}