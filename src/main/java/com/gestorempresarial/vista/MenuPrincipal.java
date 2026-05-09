// com.gestorempresarial.vista.MenuPrincipal.java
package main.java.com.gestorempresarial.vista;

import com.gestorempresarial.controlador.FacturaControlador;
import com.gestorempresarial.dao.*;
import com.gestorempresarial.modelo.*;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    
    private static Scanner scanner = new Scanner(System.in);
    private static FacturaControlador facturaControlador = new FacturaControlador();
    private static ClienteDAO clienteDAO = new ClienteDAO();
    private static ProductoDAO productoDAO = new ProductoDAO();
    private static FacturaDAO facturaDAO = new FacturaDAO();
    
    public static void main(String[] args) {
        int opcion;
        
        do {
            System.out.println("\n=== GESTOR EMPRESARIAL INTEGRADO ===");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Productos");
            System.out.println("3. Emitir Factura");
            System.out.println("4. Listar Facturas");
            System.out.println("5. Anular Factura");
            System.out.println("6. Generar Reporte Contable");
            System.out.println("7. Salir");
            System.out.print("Seleccione una opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    gestionarClientes();
                    break;
                case 2:
                    gestionarProductos();
                    break;
                case 3:
                    emitirFactura();
                    break;
                case 4:
                    listarFacturas();
                    break;
                case 5:
                    anularFactura();
                    break;
                case 6:
                    generarReporte();
                    break;
                case 7:
                    System.out.println("¡Gracias por usar el sistema!");
                    ConexionBD.cerrarConexion();
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 7);
    }
    
    private static void gestionarClientes() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE CLIENTES ---");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Actualizar Cliente");
            System.out.println("4. Eliminar Cliente");
            System.out.println("5. Buscar Cliente por NIT");
            System.out.println("6. Volver");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    listarClientes();
                    break;
                case 3:
                    actualizarCliente();
                    break;
                case 4:
                    eliminarCliente();
                    break;
                case 5:
                    buscarClientePorNit();
                    break;
            }
        } while (opcion != 6);
    }
    
    private static void registrarCliente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("NIT: ");
        String nit = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        Cliente cliente = new Cliente(nombre, nit, direccion, correo, telefono);
        if (clienteDAO.insertarCliente(cliente)) {
            System.out.println("✓ Cliente registrado con ID: " + cliente.getIdCliente());
        } else {
            System.out.println("✗ Error al registrar cliente");
        }
    }
    
    private static void listarClientes() {
        List<Cliente> clientes = clienteDAO.obtenerTodosClientes();
        System.out.println("\n--- LISTA DE CLIENTES ---");
        for (Cliente c : clientes) {
            System.out.println(c.getIdCliente() + " | " + c.getNombre() + " | NIT: " + c.getNit());
        }
    }
    
    private static void actualizarCliente() {
        System.out.print("ID del cliente a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Cliente cliente = clienteDAO.obtenerClientePorId(id);
        if (cliente != null) {
            System.out.print("Nuevo nombre (" + cliente.getNombre() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.isEmpty()) cliente.setNombre(nombre);
            
            if (clienteDAO.actualizarCliente(cliente)) {
                System.out.println("✓ Cliente actualizado");
            } else {
                System.out.println("✗ Error al actualizar");
            }
        } else {
            System.out.println("Cliente no encontrado");
        }
    }
    
    private static void eliminarCliente() {
        System.out.print("ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        
        if (clienteDAO.eliminarCliente(id)) {
            System.out.println("✓ Cliente eliminado");
        } else {
            System.out.println("✗ Error al eliminar");
        }
    }
    
    private static void buscarClientePorNit() {
        System.out.print("NIT del cliente: ");
        String nit = scanner.nextLine();
        Cliente cliente = clienteDAO.buscarPorNit(nit);
        
        if (cliente != null) {
            System.out.println("Cliente encontrado: " + cliente.getNombre());
            System.out.println("Dirección: " + cliente.getDireccion());
            System.out.println("Correo: " + cliente.getCorreo());
        } else {
            System.out.println("Cliente no encontrado");
        }
    }
    
    private static void gestionarProductos() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE PRODUCTOS ---");
            System.out.println("1. Registrar Producto");
            System.out.println("2. Listar Productos");
            System.out.println("3. Actualizar Producto");
            System.out.println("4. Eliminar Producto");
            System.out.println("5. Buscar Producto por Código");
            System.out.println("6. Volver");
            System.out.print("Opción: ");
            
            opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    registrarProducto();
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    actualizarProducto();
                    break;
                case 4:
                    eliminarProducto();
                    break;
                case 5:
                    buscarProductoPorCodigo();
                    break;
            }
        } while (opcion != 6);
    }
    
    private static void registrarProducto() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Precio: ");
        double precio = scanner.nextDouble();
        System.out.print("¿Aplica IVA? (true/false): ");
        boolean aplicaIva = scanner.nextBoolean();
        System.out.print("Porcentaje IVA (si aplica): ");
        double porcentajeIva = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        
        Producto producto = new Producto(nombre, codigo, precio, aplicaIva, porcentajeIva, stock, categoria);
        if (productoDAO.insertarProducto(producto)) {
            System.out.println("✓ Producto registrado con ID: " + producto.getIdProducto());
        } else {
            System.out.println("✗ Error al registrar producto");
        }
    }
    
    private static void listarProductos() {
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        System.out.println("\n--- LISTA DE PRODUCTOS ---");
        for (Producto p : productos) {
            System.out.println(p.getIdProducto() + " | " + p.getNombre() + " | $" + p.getPrecio() + " | Stock: " + p.getStock());
        }
    }
    
    private static void actualizarProducto() {
        System.out.print("ID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Producto producto = productoDAO.obtenerProductoPorId(id);
        if (producto != null) {
            System.out.print("Nuevo precio (" + producto.getPrecio() + "): ");
            String precioStr = scanner.nextLine();
            if (!precioStr.isEmpty()) {
                producto.setPrecio(Double.parseDouble(precioStr));
            }
            
            if (productoDAO.actualizarProducto(producto)) {
                System.out.println("✓ Producto actualizado");
            } else {
                System.out.println("✗ Error al actualizar");
            }
        } else {
            System.out.println("Producto no encontrado");
        }
    }
    
    private static void eliminarProducto() {
        System.out.print("ID del producto a eliminar: ");
        int id = scanner.nextInt();
        
        if (productoDAO.eliminarProducto(id)) {
            System.out.println("✓ Producto eliminado");
        } else {
            System.out.println("✗ Error al eliminar");
        }
    }
    
    private static void buscarProductoPorCodigo() {
        System.out.print("Código del producto: ");
        String codigo = scanner.nextLine();
        Producto producto = productoDAO.buscarPorCodigo(codigo);
        
        if (producto != null) {
            System.out.println("Producto: " + producto.getNombre());
            System.out.println("Precio: $" + producto.getPrecio());
            System.out.println("Stock: " + producto.getStock());
        } else {
            System.out.println("Producto no encontrado");
        }
    }
    
    private static void emitirFactura() {
        System.out.println("\n--- EMITIR FACTURA ---");
        
        // Seleccionar cliente
        listarClientes();
        System.out.print("ID del cliente: ");
        int idCliente = scanner.nextInt();
        Cliente cliente = clienteDAO.obtenerClientePorId(idCliente);
        
        if (cliente == null) {
            System.out.println("Cliente no encontrado");
            return;
        }
        
        System.out.print("Número de factura: ");
        String numeroFactura = scanner.nextLine();
        scanner.nextLine();
        
        Factura factura = new Factura(cliente, numeroFactura);
        
        // Agregar productos
        boolean continuar = true;
        while (continuar) {
            listarProductos();
            System.out.print("ID del producto (0 para terminar): ");
            int idProducto = scanner.nextInt();
            if (idProducto == 0) break;
            
            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            
            Producto producto = productoDAO.obtenerProductoPorId(idProducto);
            if (producto != null) {
                DetalleFactura detalle = new DetalleFactura(producto, cantidad);
                factura.agregarDetalle(detalle);
                System.out.println("Producto agregado: " + producto.getNombre());
            } else {
                System.out.println("Producto no encontrado");
            }
        }
        
        if (facturaControlador.crearFactura(factura)) {
            System.out.println("✓ Factura emitida exitosamente");
            System.out.println("Total: $" + factura.getTotal());
        } else {
            System.out.println("✗ Error al emitir factura");
        }
    }
    
    private static void listarFacturas() {
        List<Factura> facturas = facturaDAO.obtenerTodasFacturas();
        System.out.println("\n--- LISTA DE FACTURAS ---");
        for (Factura f : facturas) {
            System.out.println(f.getIdFactura() + " | No. " + f.getNumeroFactura() + " | " + f.getFecha() + " | $" + f.getTotal() + " | " + f.getEstado());
        }
    }
    
    private static void anularFactura() {
        listarFacturas();
        System.out.print("ID de factura a anular: ");
        int id = scanner.nextInt();
        
        if (facturaControlador.anularFactura(id)) {
            System.out.println("✓ Factura anulada");
        } else {
            System.out.println("✗ Error al anular factura");
        }
    }
    
    private static void generarReporte() {
        System.out.print("Período del reporte (ej: Marzo 2025): ");
        String periodo = scanner.nextLine();
        facturaControlador.generarReporteVentas(periodo);
    }
}