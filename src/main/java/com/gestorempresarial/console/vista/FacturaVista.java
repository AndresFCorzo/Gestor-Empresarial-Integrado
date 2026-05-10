package main.java.com.gestorempresarial.console.vista;

import main.java.com.gestorempresarial.console.controlador.ClienteControlador;
import main.java.com.gestorempresarial.console.controlador.FacturaControlador;
import main.java.com.gestorempresarial.console.controlador.ProductoControlador;
import main.java.com.gestorempresarial.modelo.Cliente;
import main.java.com.gestorempresarial.modelo.DetalleFactura;
import main.java.com.gestorempresarial.modelo.Factura;
import main.java.com.gestorempresarial.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Vista del módulo de facturación (Versión Consola)
 * @author Andres Felipe Corzo Angarita
 * @author Thomas Felipe Colmenares Perdomo
 */
public class FacturaVista {
    
    private static Scanner scanner = new Scanner(System.in);
    private static ClienteControlador clienteControlador = new ClienteControlador();
    private static ProductoControlador productoControlador = new ProductoControlador();
    private static FacturaControlador facturaControlador = new FacturaControlador();
    
    public static void mostrarMenuFacturacion() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║        MÓDULO DE FACTURACIÓN         ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Emitir nueva factura             ║");
            System.out.println("║  2. Listar facturas                  ║");
            System.out.println("║  3. Buscar factura por ID            ║");
            System.out.println("║  4. Anular factura                   ║");
            System.out.println("║  5. Generar reporte de ventas        ║");
            System.out.println("║  6. Volver                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Opción: ");
            
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
                    anularFactura();
                    break;
                case 5:
                    generarReporte();
                    break;
            }
        } while (opcion != 6);
    }
    
    private static void emitirFactura() {
        System.out.println("\n--- EMISIÓN DE FACTURA ---");
        
        // Seleccionar cliente
        List<Cliente> clientes = clienteControlador.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("❌ No hay clientes registrados. Registre un cliente primero.");
            return;
        }
        
        clienteControlador.mostrarListaClientes(clientes);
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        
        Cliente cliente = clienteControlador.buscarClientePorId(idCliente);
        if (cliente == null) {
            System.out.println("❌ Cliente no encontrado");
            return;
        }
        
        System.out.print("Número de factura: ");
        String numeroFactura = scanner.nextLine();
        
        // Agregar productos
        List<DetalleFactura> detalles = new ArrayList<>();
        boolean continuar = true;
        
        while (continuar) {
            List<Producto> productos = productoControlador.listarProductos();
            if (productos.isEmpty()) {
                System.out.println("❌ No hay productos registrados. Registre un producto primero.");
                return;
            }
            
            productoControlador.mostrarListaProductos(productos);
            System.out.print("ID del producto (0 para terminar): ");
            int idProducto = scanner.nextInt();
            
            if (idProducto == 0) {
                if (detalles.isEmpty()) {
                    System.out.println("❌ Debe agregar al menos un producto");
                    continue;
                }
                continuar = false;
                break;
            }
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            Producto producto = productoControlador.buscarProductoPorId(idProducto);
            if (producto != null) {
                if (!productoControlador.verificarStock(idProducto, cantidad)) {
                    System.out.println("❌ Stock insuficiente. Disponible: " + producto.getStock());
                    continue;
                }
                
                // Validar valor atípico (HU-02)
                if (!facturaControlador.validarValorFactura(producto.getPrecio() * cantidad)) {
                    System.out.print("⚠️ Valor inusualmente alto. ¿Continuar? (s/n): ");
                    String confirmacion = scanner.nextLine();
                    scanner.nextLine();
                    if (!confirmacion.equalsIgnoreCase("s")) {
                        continue;
                    }
                }
                
                DetalleFactura detalle = new DetalleFactura(producto, cantidad);
                detalles.add(detalle);
                System.out.println("✓ Producto agregado: " + producto.getNombre() + " x" + cantidad);
            } else {
                System.out.println("❌ Producto no encontrado");
            }
        }
        
        // Confirmar y crear factura
        System.out.println("\n--- RESUMEN DE FACTURA ---");
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("N° Factura: " + numeroFactura);
        double total = 0;
        for (DetalleFactura d : detalles) {
            System.out.printf("  %s x%d = $%.2f%n", d.getProducto().getNombre(), d.getCantidad(), d.getTotal());
            total += d.getTotal();
        }
        System.out.println("TOTAL: $" + total);
        
        System.out.print("\n¿Confirmar emisión? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            String resultado = facturaControlador.crearFactura(cliente, numeroFactura, detalles);
            System.out.println(resultado);
        } else {
            System.out.println("Emisión cancelada");
        }
    }
    
    private static void listarFacturas() {
        List<Factura> facturas = facturaControlador.listarFacturas();
        facturaControlador.mostrarListaFacturas(facturas);
    }
    
    private static void buscarFacturaPorId() {
        System.out.print("\nID de la factura: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Factura factura = facturaControlador.buscarFacturaPorId(id);
        facturaControlador.mostrarDetalleFactura(factura);
    }
    
    private static void anularFactura() {
        listarFacturas();
        System.out.print("\nID de la factura a anular: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("¿Está seguro? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            String resultado = facturaControlador.anularFactura(id);
            System.out.println(resultado);
        } else {
            System.out.println("Operación cancelada");
        }
    }
    
    private static void generarReporte() {
        System.out.print("\nPeríodo del reporte (Ej: Marzo 2025): ");
        String periodo = scanner.nextLine();
        facturaControlador.generarReporteVentas(periodo);
    }
}