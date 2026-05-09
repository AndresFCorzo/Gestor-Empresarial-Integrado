// com.gestorempresarial.vista.FacturaVista.java
package main.java.com.gestorempresarial.vista;

import com.gestorempresarial.controlador.FacturaControlador;
import com.gestorempresarial.dao.*;
import com.gestorempresarial.modelo.*;
import java.util.List;
import java.util.Scanner;

public class FacturaVista {
    
    private static Scanner scanner = new Scanner(System.in);
    private static FacturaControlador facturaControlador = new FacturaControlador();
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static FacturaDAO facturaDAO = new FacturaDAO();
    
    public static void mostrarMenuFacturacion() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║        MÓDULO DE FACTURACIÓN         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Emitir nueva factura             ║");
            System.out.println("║  2. Listar todas las facturas        ║");
            System.out.println("║  3. Buscar factura por ID            ║");
            System.out.println("║  4. Buscar facturas por cliente      ║");
            System.out.println("║  5. Anular factura                   ║");
            System.out.println("║  6. Generar reporte contable         ║");
            System.out.println("║  7. Volver al menú principal         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    emitirFactura();
                    break;
                case 2:
                    listarFacturas();
                    break;
                case 3:
                    buscarFacturaPorId();
                    break;
                case 4:
                    buscarFacturasPorCliente();
                    break;
                case 5:
                    anularFactura();
                    break;
                case 6:
                    generarReporte();
                    break;
                case 7:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("❌ Opción inválida");
            }
        } while (opcion != 7);
    }
    
    private static void emitirFactura() {
        System.out.println("\n=== EMITIR NUEVA FACTURA ===\n");
        
        // Mostrar clientes disponibles
        List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
        if (clientes.isEmpty()) {
            System.out.println("❌ No hay clientes registrados. Registre un cliente primero.");
            return;
        }
        
        System.out.println("--- CLIENTES DISPONIBLES ---");
        for (Cliente c : clientes) {
            System.out.println(c.getIdCliente() + " | " + c.getNombre() + " | NIT: " + c.getNit());
        }
        System.out.println("------------------------------");
        
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        
        Cliente cliente = clienteDAO.obtenerClientePorId(idCliente);
        if (cliente == null) {
            System.out.println("❌ Cliente no encontrado");
            return;
        }
        
        System.out.print("Número de factura: ");
        String numeroFactura = scanner.nextLine();
        
        // Validar que el número de factura no exista
        List<Factura> facturasExistentes = facturaDAO.obtenerTodasFacturas();
        for (Factura f : facturasExistentes) {
            if (f.getNumeroFactura().equals(numeroFactura)) {
                System.out.println("❌ El número de factura ya existe");
                return;
            }
        }
        
        Factura factura = new Factura(cliente, numeroFactura);
        
        // Agregar productos a la factura
        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n--- AGREGAR PRODUCTO ---");
            
            // Mostrar productos disponibles
            List<Producto> productos = productoDAO.obtenerTodosProductos();
            if (productos.isEmpty()) {
                System.out.println("❌ No hay productos registrados. Registre un producto primero.");
                return;
            }
            
            System.out.println("\n--- PRODUCTOS DISPONIBLES ---");
            System.out.printf("%-5s %-20s %-10s %-8s %-10s%n", "ID", "Nombre", "Precio", "Stock", "Código");
            System.out.println("----------------------------------------------------------");
            for (Producto p : productos) {
                System.out.printf("%-5d %-20s $%-9.2f %-8d %-10s%n", 
                    p.getIdProducto(), 
                    p.getNombre().length() > 20 ? p.getNombre().substring(0, 17) + "..." : p.getNombre(),
                    p.getPrecio(),
                    p.getStock(),
                    p.getCodigo());
            }
            System.out.println("----------------------------------------------------------");
            
            System.out.print("ID del producto (0 para terminar): ");
            int idProducto = scanner.nextInt();
            
            if (idProducto == 0) {
                if (factura.getDetalles().isEmpty()) {
                    System.out.println("❌ Debe agregar al menos un producto a la factura");
                    continue;
                }
                agregarMas = false;
                break;
            }
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            Producto producto = productoDAO.obtenerProductoPorId(idProducto);
            if (producto != null) {
                // Validar stock
                if (producto.getStock() < cantidad) {
                    System.out.println("❌ Stock insuficiente. Disponible: " + producto.getStock());
                    continue;
                }
                
                // Validar valor (corrección automática de errores - HU-02)
                if (!facturaControlador.validarValorFactura(producto.getPrecio() * cantidad)) {
                    System.out.print("¿Desea continuar de todas formas? (s/n): ");
                    String confirmacion = scanner.nextLine();
                    scanner.nextLine();
                    if (!confirmacion.equalsIgnoreCase("s")) {
                        continue;
                    }
                }
                
                DetalleFactura detalle = new DetalleFactura(producto, cantidad);
                factura.agregarDetalle(detalle);
                System.out.println("✓ Producto agregado: " + producto.getNombre() + " x" + cantidad);
                System.out.println("  Subtotal: $" + detalle.getSubtotal());
                System.out.println("  IVA: $" + detalle.getValorIva());
                System.out.println("  Total: $" + detalle.getTotal());
            } else {
                System.out.println("❌ Producto no encontrado");
            }
        }
        
        // Mostrar resumen de la factura
        System.out.println("\n=== RESUMEN DE FACTURA ===");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("NIT: " + cliente.getNit());
        System.out.println("Número de factura: " + numeroFactura);
        System.out.println("\n--- DETALLES ---");
        System.out.printf("%-20s %-8s %-12s %-12s %-12s%n", "Producto", "Cantidad", "Precio Unit.", "Subtotal", "Total");
        System.out.println("--------------------------------------------------------------------------------");
        for (DetalleFactura d : factura.getDetalles()) {
            System.out.printf("%-20s %-8d $%-11.2f $%-11.2f $%-11.2f%n",
                d.getProducto().getNombre().length() > 20 ? d.getProducto().getNombre().substring(0, 17) + "..." : d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal(),
                d.getTotal());
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-53s $%-11.2f%n", "SUBTOTAL:", factura.getSubtotal());
        System.out.printf("%-53s $%-11.2f%n", "IVA:", factura.getTotalIva());
        System.out.printf("%-53s $%-11.2f%n", "TOTAL:", factura.getTotal());
        System.out.println("--------------------------------------------------------------------------------");
        
        System.out.print("\n¿Confirmar emisión de factura? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            if (facturaControlador.crearFactura(factura)) {
                System.out.println("\n✅ ¡FACTURA EMITIDA EXITOSAMENTE!");
                System.out.println("   Número: " + factura.getNumeroFactura());
                System.out.println("   Total: $" + factura.getTotal());
                System.out.println("   Estado: " + factura.getEstado());
                
                // Actualizar stock de productos
                for (DetalleFactura detalle : factura.getDetalles()) {
                    Producto p = detalle.getProducto();
                    int nuevoStock = p.getStock() - detalle.getCantidad();
                    productoDAO.actualizarStock(p.getIdProducto(), nuevoStock);
                }
            } else {
                System.out.println("❌ Error al emitir la factura");
            }
        } else {
            System.out.println("Emisión de factura cancelada");
        }
    }
    
    private static void listarFacturas() {
        System.out.println("\n=== LISTADO DE FACTURAS ===\n");
        
        List<Factura> facturas = facturaDAO.obtenerTodasFacturas();
        
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas");
            return;
        }
        
        System.out.printf("%-5s %-15s %-12s %-12s %-12s %-10s%n", 
            "ID", "Nº Factura", "Fecha", "Total", "Cliente", "Estado");
        System.out.println("--------------------------------------------------------------------------------");
        
        for (Factura f : facturas) {
            System.out.printf("%-5d %-15s %-12s $%-11.2f %-12s %-10s%n",
                f.getIdFactura(),
                f.getNumeroFactura(),
                f.getFecha().toString(),
                f.getTotal(),
                f.getCliente().getNombre().length() > 12 ? f.getCliente().getNombre().substring(0, 9) + "..." : f.getCliente().getNombre(),
                f.getEstado());
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Total de facturas: " + facturas.size());
    }
    
    private static void buscarFacturaPorId() {
        System.out.println("\n=== BUSCAR FACTURA POR ID ===\n");
        
        System.out.print("ID de la factura: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Factura factura = facturaDAO.obtenerFacturaPorId(id);
        
        if (factura == null) {
            System.out.println("❌ Factura no encontrada");
            return;
        }
        
        mostrarDetalleFactura(factura);
    }
    
    private static void buscarFacturasPorCliente() {
        System.out.println("\n=== BUSCAR FACTURAS POR CLIENTE ===\n");
        
        // Mostrar clientes
        List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados");
            return;
        }
        
        System.out.println("--- CLIENTES ---");
        for (Cliente c : clientes) {
            System.out.println(c.getIdCliente() + " | " + c.getNombre() + " | NIT: " + c.getNit());
        }
        
        System.out.print("\nID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        
        List<Factura> facturas = facturaDAO.obtenerFacturasPorCliente(idCliente);
        
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas para este cliente");
            return;
        }
        
        Cliente cliente = clienteDAO.obtenerClientePorId(idCliente);
        System.out.println("\nFacturas de: " + cliente.getNombre());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-12s %-12s %-10s%n", "ID", "Nº Factura", "Fecha", "Total", "Estado");
        System.out.println("--------------------------------------------------------------------------------");
        
        double totalGeneral = 0;
        for (Factura f : facturas) {
            System.out.printf("%-5d %-15s %-12s $%-11.2f %-10s%n",
                f.getIdFactura(),
                f.getNumeroFactura(),
                f.getFecha().toString(),
                f.getTotal(),
                f.getEstado());
            totalGeneral += f.getTotal();
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("TOTAL COMPRAS DEL CLIENTE: $%.2f%n", totalGeneral);
    }
    
    private static void anularFactura() {
        System.out.println("\n=== ANULAR FACTURA ===\n");
        
        listarFacturas();
        
        System.out.print("\nID de la factura a anular: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Factura factura = facturaDAO.obtenerFacturaPorId(id);
        
        if (factura == null) {
            System.out.println("❌ Factura no encontrada");
            return;
        }
        
        if (factura.getEstado().equals("ANULADA")) {
            System.out.println("❌ Esta factura ya está anulada");
            return;
        }
        
        System.out.println("\n--- DATOS DE LA FACTURA ---");
        System.out.println("Número: " + factura.getNumeroFactura());
        System.out.println("Cliente: " + factura.getCliente().getNombre());
        System.out.println("Total: $" + factura.getTotal());
        System.out.println("Estado actual: " + factura.getEstado());
        
        System.out.print("\n¿Está seguro de que desea anular esta factura? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            if (facturaControlador.anularFactura(id)) {
                System.out.println("✅ Factura anulada exitosamente");
                
                // Restaurar stock de productos (opcional - según reglas de negocio)
                System.out.print("¿Desea restaurar el stock de los productos? (s/n): ");
                String restaurarStock = scanner.nextLine();
                if (restaurarStock.equalsIgnoreCase("s")) {
                    for (DetalleFactura detalle : factura.getDetalles()) {
                        Producto p = detalle.getProducto();
                        int nuevoStock = p.getStock() + detalle.getCantidad();
                        productoDAO.actualizarStock(p.getIdProducto(), nuevoStock);
                    }
                    System.out.println("✓ Stock restaurado");
                }
            } else {
                System.out.println("❌ Error al anular la factura");
            }
        } else {
            System.out.println("Operación cancelada");
        }
    }
    
    private static void generarReporte() {
        System.out.println("\n=== GENERAR REPORTE CONTABLE ===\n");
        
        System.out.print("Período del reporte (ej: Marzo 2025): ");
        String periodo = scanner.nextLine();
        
        facturaControlador.generarReporteVentas(periodo);
        
        System.out.print("\n¿Desea exportar el reporte? (s/n): ");
        String exportar = scanner.nextLine();
        
        if (exportar.equalsIgnoreCase("s")) {
            System.out.println("Seleccione formato:");
            System.out.println("1. PDF");
            System.out.println("2. Excel (CSV)");
            System.out.print("Opción: ");
            int formato = scanner.nextInt();
            scanner.nextLine();
            
            if (formato == 2) {
                exportarReporteCSV(periodo);
            } else {
                System.out.println("📄 Reporte listo para impresión (formato PDF simulado)");
            }
        }
    }
    
    private static void exportarReporteCSV(String periodo) {
        List<Factura> facturas = facturaDAO.obtenerTodasFacturas();
        String nombreArchivo = "reporte_ventas_" + periodo.replace(" ", "_") + ".csv";
        
        try {
            java.io.FileWriter writer = new java.io.FileWriter(nombreArchivo);
            writer.write("Código Producto,Nombre Producto,Cantidad,Valor Base,IVA,Total\n");
            
            for (Factura factura : facturas) {
                for (DetalleFactura detalle : factura.getDetalles()) {
                    Producto p = detalle.getProducto();
                    writer.write(String.format("%s,%s,%d,%.2f,%.2f,%.2f\n",
                        p.getCodigo(),
                        p.getNombre(),
                        detalle.getCantidad(),
                        detalle.getSubtotal(),
                        detalle.getValorIva(),
                        detalle.getTotal()));
                }
            }
            
            writer.close();
            System.out.println("✅ Reporte exportado a: " + nombreArchivo);
        } catch (Exception e) {
            System.out.println("❌ Error al exportar: " + e.getMessage());
        }
    }
    
    private static void mostrarDetalleFactura(Factura factura) {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      DETALLE DE FACTURA                       ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s: %-40s ║%n", "Número Factura", factura.getNumeroFactura());
        System.out.printf("║ %-20s: %-40s ║%n", "Fecha", factura.getFecha());
        System.out.printf("║ %-20s: %-40s ║%n", "Estado", factura.getEstado());
        System.out.printf("║ %-20s: %-40s ║%n", "Cliente", factura.getCliente().getNombre());
        System.out.printf("║ %-20s: %-40s ║%n", "NIT Cliente", factura.getCliente().getNit());
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║                        DETALLE PRODUCTOS                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        
        for (DetalleFactura d : factura.getDetalles()) {
            System.out.printf("║ %-20s x%-4d = $%-32.2f ║%n", 
                d.getProducto().getNombre(), 
                d.getCantidad(), 
                d.getTotal());
        }
        
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s: $%-40.2f ║%n", "SUBTOTAL", factura.getSubtotal());
        System.out.printf("║ %-20s: $%-40.2f ║%n", "IVA", factura.getTotalIva());
        System.out.printf("║ %-20s: $%-40.2f ║%n", "TOTAL", factura.getTotal());
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
}