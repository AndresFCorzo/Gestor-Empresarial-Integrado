package main.java.com.gestorempresarial.console.controlador;

import main.java.com.gestorempresarial.dao.ClienteDAO;
import main.java.com.gestorempresarial.modelo.Cliente;
import main.java.com.gestorempresarial.utils.Validaciones;
import java.util.List;

/**
 * Controlador para la gestión de clientes (Versión Consola)
 * @author Andres Felipe Corzo Angarita
 * @author Thomas Felipe Colmenares Perdomo
 */
public class ClienteControlador {
    
    private ClienteDAO clienteDAO;
    
    public ClienteControlador() {
        this.clienteDAO = new ClienteDAO();
    }
    
    /**
     * Registrar un nuevo cliente
     * @param nombre Nombre del cliente
     * @param nit NIT del cliente
     * @param direccion Dirección
     * @param correo Correo electrónico
     * @param telefono Teléfono
     * @return Mensaje de resultado
     */
    public String registrarCliente(String nombre, String nit, String direccion, String correo, String telefono) {
        // Validar datos
        String error = Validaciones.validarCliente(nombre, nit);
        if (error != null) {
            return "❌ " + error;
        }
        
        // Verificar si ya existe un cliente con el mismo NIT
        Cliente existente = clienteDAO.buscarPorNit(nit);
        if (existente != null) {
            return "❌ Ya existe un cliente con el NIT: " + nit;
        }
        
        Cliente cliente = new Cliente(nombre, nit, direccion, correo, telefono);
        
        if (clienteDAO.insertar(cliente)) {
            return "✅ Cliente registrado exitosamente con ID: " + cliente.getIdCliente();
        } else {
            return "❌ Error al registrar el cliente";
        }
    }
    
    /**
     * Listar todos los clientes
     * @return Lista de clientes
     */
    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }
    
    /**
     * Buscar cliente por ID
     * @param id ID del cliente
     * @return Cliente encontrado o null
     */
    public Cliente buscarClientePorId(int id) {
        if (id <= 0) {
            System.out.println("❌ ID inválido");
            return null;
        }
        return clienteDAO.buscarPorId(id);
    }
    
    /**
     * Buscar cliente por NIT
     * @param nit NIT del cliente
     * @return Cliente encontrado o null
     */
    public Cliente buscarClientePorNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) {
            System.out.println("❌ NIT inválido");
            return null;
        }
        return clienteDAO.buscarPorNit(nit);
    }
    
    /**
     * Actualizar un cliente existente
     * @param id ID del cliente
     * @param nombre Nuevo nombre
     * @param nit Nuevo NIT
     * @param direccion Nueva dirección
     * @param correo Nuevo correo
     * @param telefono Nuevo teléfono
     * @return Mensaje de resultado
     */
    public String actualizarCliente(int id, String nombre, String nit, String direccion, String correo, String telefono) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            return "❌ Cliente no encontrado";
        }
        
        if (nombre != null && !nombre.trim().isEmpty()) cliente.setNombre(nombre);
        if (nit != null && !nit.trim().isEmpty()) cliente.setNit(nit);
        if (direccion != null && !direccion.trim().isEmpty()) cliente.setDireccion(direccion);
        if (correo != null && !correo.trim().isEmpty()) cliente.setCorreo(correo);
        if (telefono != null && !telefono.trim().isEmpty()) cliente.setTelefono(telefono);
        
        if (clienteDAO.actualizar(cliente)) {
            return "✅ Cliente actualizado exitosamente";
        } else {
            return "❌ Error al actualizar cliente";
        }
    }
    
    /**
     * Eliminar un cliente
     * @param id ID del cliente
     * @return Mensaje de resultado
     */
    public String eliminarCliente(int id) {
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            return "❌ Cliente no encontrado";
        }
        
        if (clienteDAO.eliminar(id)) {
            return "✅ Cliente eliminado exitosamente";
        } else {
            return "❌ Error al eliminar cliente";
        }
    }
    
    /**
     * Mostrar información detallada de un cliente
     * @param cliente Cliente a mostrar
     */
    public void mostrarDetalleCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("❌ Cliente no encontrado");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         DETALLE DEL CLIENTE          ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║ %-15s: %-25s ║%n", "ID", cliente.getIdCliente());
        System.out.printf("║ %-15s: %-25s ║%n", "Nombre", cliente.getNombre());
        System.out.printf("║ %-15s: %-25s ║%n", "NIT", cliente.getNit());
        System.out.printf("║ %-15s: %-25s ║%n", "Dirección", cliente.getDireccion() != null ? cliente.getDireccion() : "No registrada");
        System.out.printf("║ %-15s: %-25s ║%n", "Teléfono", cliente.getTelefono() != null ? cliente.getTelefono() : "No registrado");
        System.out.printf("║ %-15s: %-25s ║%n", "Correo", cliente.getCorreo() != null ? cliente.getCorreo() : "No registrado");
        System.out.println("╚══════════════════════════════════════╝");
    }
    
    /**
     * Mostrar lista de clientes en formato tabla
     * @param clientes Lista de clientes
     */
    public void mostrarListaClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("📋 No hay clientes registrados");
            return;
        }
        
        System.out.println("\n┌─────┬──────────────────────────────────┬──────────────┬─────────────────┐");
        System.out.println("│ ID  │ NOMBRE                           │ NIT          │ TELÉFONO        │");
        System.out.println("├─────┼──────────────────────────────────┼──────────────┼─────────────────┤");
        for (Cliente c : clientes) {
            System.out.printf("│ %-3d │ %-32s │ %-12s │ %-15s │%n",
                c.getIdCliente(),
                c.getNombre().length() > 32 ? c.getNombre().substring(0, 29) + "..." : c.getNombre(),
                c.getNit(),
                c.getTelefono() != null ? c.getTelefono() : "-");
        }
        System.out.println("└─────┴──────────────────────────────────┴──────────────┴─────────────────┘");
    }
}