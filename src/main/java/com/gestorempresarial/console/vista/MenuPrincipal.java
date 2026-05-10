package com.gestorempresarial.console.vista;

import com.gestorempresarial.console.controlador.ClienteControlador;
import com.gestorempresarial.console.controlador.ProductoControlador;
import com.gestorempresarial.dao.ConexionBD;
import com.gestorempresarial.modelo.Cliente;
import com.gestorempresarial.modelo.Producto;
import java.util.List;
import java.util.Scanner;

/**
 * Menú principal de la versión consola
 * @author Andres Felipe Corzo Angarita
 */
public class MenuPrincipal {
    
    private static Scanner scanner = new Scanner(System.in);
    private static ClienteControlador clienteControlador = new ClienteControlador();
    private static ProductoControlador productoControlador = new ProductoControlador();
    
    public static void main(String[] args) {
        int opcion;
        
        do {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║              GESTOR EMPRESARIAL INTEGRADO                   ║");
            System.out.println("║                       MENÚ PRINCIPAL                        ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            System.out.println("║                                                            ║");
            System.out.println("║  1. Gestión de Clientes                                     ║");
            System.out.println("║  2. Gestión de Productos                                    ║");
            System.out.println("║  3. Módulo de Facturación                                   ║");
            System.out.println("║  4. Salir                                                   ║");
            System.out.println("║                                                            ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
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
                    FacturaVista.mostrarMenuFacturacion();
                    break;
                case 4:
                    System.out.println("\n¡Gracias por usar el Gestor Empresarial Integrado!");
                    ConexionBD.cerrarConexion();
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 4);
    }
    
    private static void gestionarClientes() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         GESTIÓN DE CLIENTES           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Registrar Cliente                 ║");
            System.out.println("║  2. Listar Clientes                   ║");
            System.out.println("║  3. Buscar Cliente por ID             ║");
            System.out.println("║  4. Buscar Cliente por NIT            ║");
            System.out.println("║  5. Actualizar Cliente                ║");
            System.out.println("║  6. Eliminar Cliente                  ║");
            System.out.println("║  7. Volver                            ║");
            System.out.println("╚══════════════════════════════════════╝");
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
                    buscarClientePorId();
                    break;
                case 4:
                    buscarClientePorNit();
                    break;
                case 5:
                    actualizarCliente();
                    break;
                case 6:
                    eliminarCliente();
                    break;
            }
        } while (opcion != 7);
    }
    
    private static void registrarCliente() {
        System.out.println("\n--- REGISTRO DE CLIENTE ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("NIT (Ej: 900123456-1): ");
        String nit = scanner.nextLine();
        System.out.print("Dirección: ");
        String direccion = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        String resultado = clienteControlador.registrarCliente(nombre, nit, direccion, correo, telefono);
        System.out.println(resultado);
    }
    
    private static void listarClientes() {
        List<Cliente> clientes = clienteControlador.listarClientes();
        clienteControlador.mostrarListaClientes(clientes);
    }
    
    private static void buscarClientePorId() {
        System.out.print("\nID del cliente: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Cliente cliente = clienteControlador.buscarClientePorId(id);
        clienteControlador.mostrarDetalleCliente(cliente);
    }
    
    private static void buscarClientePorNit() {
        System.out.print("\nNIT del cliente: ");
        String nit = scanner.nextLine();
        
        Cliente cliente = clienteControlador.buscarClientePorNit(nit);
        clienteControlador.mostrarDetalleCliente(cliente);
    }
    
    private static void actualizarCliente() {
        System.out.print("\nID del cliente a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Cliente cliente = clienteControlador.buscarClientePorId(id);
        if (cliente == null) {
            System.out.println("❌ Cliente no encontrado");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea modificar");
        System.out.print("Nuevo nombre (" + cliente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        System.out.print("Nueva dirección (" + cliente.getDireccion() + "): ");
        String direccion = scanner.nextLine();
        System.out.print("Nuevo teléfono (" + cliente.getTelefono() + "): ");
        String telefono = scanner.nextLine();
        System.out.print("Nuevo correo (" + cliente.getCorreo() + "): ");
        String correo = scanner.nextLine();
        
        String resultado = clienteControlador.actualizarCliente(id, 
            nombre.isEmpty() ? null : nombre,
            null,
            direccion.isEmpty() ? null : direccion,
            correo.isEmpty() ? null : correo,
            telefono.isEmpty() ? null : telefono);
        System.out.println(resultado);
    }
    
    private static void eliminarCliente() {
        System.out.print("\nID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("¿Está seguro? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            String resultado = clienteControlador.eliminarCliente(id);
            System.out.println(resultado);
        } else {
            System.out.println("Operación cancelada");
        }
    }
    
    private static void gestionarProductos() {
        int opcion;
        do {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         GESTIÓN DE PRODUCTOS          ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Registrar Producto                ║");
            System.out.println("║  2. Listar Productos                  ║");
            System.out.println("║  3. Buscar Producto por ID            ║");
            System.out.println("║  4. Buscar Producto por Código        ║");
            System.out.println("║  5. Actualizar Producto               ║");
            System.out.println("║  6. Eliminar Producto                 ║");
            System.out.println("║  7. Volver                            ║");
            System.out.println("╚══════════════════════════════════════╝");
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
                    buscarProductoPorId();
                    break;
                case 4:
                    buscarProductoPorCodigo();
                    break;
                case 5:
                    actualizarProducto();
                    break;
                case 6:
                    eliminarProducto();
                    break;
            }
        } while (opcion != 7);
    }
    
    private static void registrarProducto() {
        System.out.println("\n--- REGISTRO DE PRODUCTO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Código (Ej: PROD-001): ");
        String codigo = scanner.nextLine();
        System.out.print("Precio: ");
        double precio = scanner.nextDouble();
        System.out.print("¿Aplica IVA? (true/false): ");
        boolean aplicaIva = scanner.nextBoolean();
        double porcentajeIva = 0;
        if (aplicaIva) {
            System.out.print("Porcentaje IVA (%): ");
            porcentajeIva = scanner.nextDouble();
        }
        System.out.print("Stock inicial: ");
        int stock = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        
        String resultado = productoControlador.registrarProducto(nombre, codigo, precio, aplicaIva, porcentajeIva, stock, categoria);
        System.out.println(resultado);
    }
    
    private static void listarProductos() {
        List<Producto> productos = productoControlador.listarProductos();
        productoControlador.mostrarListaProductos(productos);
    }
    
    private static void buscarProductoPorId() {
        System.out.print("\nID del producto: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Producto producto = productoControlador.buscarProductoPorId(id);
        productoControlador.mostrarDetalleProducto(producto);
    }
    
    private static void buscarProductoPorCodigo() {
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine();
        
        Producto producto = productoControlador.buscarProductoPorCodigo(codigo);
        productoControlador.mostrarDetalleProducto(producto);
    }
    
    private static void actualizarProducto() {
        System.out.print("\nID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Producto producto = productoControlador.buscarProductoPorId(id);
        if (producto == null) {
            System.out.println("❌ Producto no encontrado");
            return;
        }
        
        System.out.println("Deje en blanco los campos que no desea modificar");
        System.out.print("Nuevo precio (" + producto.getPrecio() + "): ");
        String precioStr = scanner.nextLine();
        Double precio = precioStr.isEmpty() ? null : Double.parseDouble(precioStr);
        
        System.out.print("Nuevo stock (" + producto.getStock() + "): ");
        String stockStr = scanner.nextLine();
        Integer stock = stockStr.isEmpty() ? null : Integer.parseInt(stockStr);
        
        String resultado = productoControlador.actualizarProducto(id, precio, stock);
        System.out.println(resultado);
    }
    
    private static void eliminarProducto() {
        System.out.print("\nID del producto a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("¿Está seguro? (s/n): ");
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("s")) {
            String resultado = productoControlador.eliminarProducto(id);
            System.out.println(resultado);
        } else {
            System.out.println("Operación cancelada");
        }
    }
}