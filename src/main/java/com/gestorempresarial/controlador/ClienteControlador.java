// com.gestorempresarial.controlador.ClienteControlador.java
package main.java.com.gestorempresarial.controlador;

import com.gestorempresarial.dao.ClienteDAO;
import com.gestorempresarial.dao.RegistroGeneralDAO;
import com.gestorempresarial.modelo.Cliente;
import com.gestorempresarial.modelo.RegistroGeneral;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controlador para la gestión de clientes.
 * Implementa la lógica de negocio y validaciones para las operaciones CRUD de clientes.
 * 
 * @author Andres Felipe Corzo Angarita
 * @author Thomas Felipe Colmenares Perdomo
 */
public class ClienteControlador {
    
    private ClienteDAO clienteDAO;
    private RegistroGeneralDAO registroGeneralDAO;
    
    // Patrones para validaciones
    private static final Pattern PATRON_NIT = Pattern.compile("^[0-9]{1,10}-[0-9]$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^[0-9]{7,15}$");
    
    public ClienteControlador() {
        this.clienteDAO = new ClienteDAO();
        this.registroGeneralDAO = new RegistroGeneralDAO();
    }
    
    /**
     * Registrar un nuevo cliente en el sistema
     * @param cliente Objeto Cliente con los datos del cliente
     * @return true si se registró correctamente, false en caso contrario
     */
    public boolean registrarCliente(Cliente cliente) {
        // Validar datos del cliente antes de registrar
        if (!validarCliente(cliente)) {
            System.err.println("Error: Datos de cliente inválidos");
            return false;
        }
        
        // Verificar si ya existe un cliente con el mismo NIT
        Cliente existente = clienteDAO.buscarPorNit(cliente.getNit());
        if (existente != null) {
            System.err.println("Error: Ya existe un cliente con el NIT " + cliente.getNit());
            return false;
        }
        
        // Registrar el cliente
        boolean resultado = clienteDAO.insertarCliente(cliente);
        
        if (resultado) {
            // Crear registro general centralizado (HU-08)
            RegistroGeneral registro = new RegistroGeneral(
                RegistroGeneral.TIPO_CLIENTE,
                "Cliente registrado: " + cliente.getNombre(),
                cliente.getIdCliente(),
                "Cliente"
            );
            registro.registrarRegistro();
            registroGeneralDAO.insertarRegistro(registro);
            
            System.out.println("✓ Cliente registrado exitosamente con ID: " + cliente.getIdCliente());
        }
        
        return resultado;
    }
    
    /**
     * Actualizar los datos de un cliente existente
     * @param cliente Objeto Cliente con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizarCliente(Cliente cliente) {
        if (cliente.getIdCliente() <= 0) {
            System.err.println("Error: ID de cliente inválido");
            return false;
        }
        
        // Validar datos actualizados
        if (!validarCliente(cliente)) {
            System.err.println("Error: Datos de cliente inválidos");
            return false;
        }
        
        // Verificar que el cliente existe
        Cliente existente = clienteDAO.obtenerClientePorId(cliente.getIdCliente());
        if (existente == null) {
            System.err.println("Error: No existe un cliente con ID " + cliente.getIdCliente());
            return false;
        }
        
        boolean resultado = clienteDAO.actualizarCliente(cliente);
        
        if (resultado) {
            // Actualizar registro general
            List<RegistroGeneral> registros = registroGeneralDAO.obtenerRegistrosPorEntidad("Cliente", cliente.getIdCliente());
            if (!registros.isEmpty()) {
                RegistroGeneral registro = registros.get(0);
                registro.setDescripcion("Cliente actualizado: " + cliente.getNombre());
                registro.actualizarRegistro();
                registroGeneralDAO.actualizarRegistro(registro);
            }
            
            System.out.println("✓ Cliente actualizado exitosamente");
        }
        
        return resultado;
    }
    
    /**
     * Eliminar (desactivar) un cliente del sistema
     * @param idCliente ID del cliente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarCliente(int idCliente) {
        if (idCliente <= 0) {
            System.err.println("Error: ID de cliente inválido");
            return false;
        }
        
        // Verificar que el cliente existe
        Cliente cliente = clienteDAO.obtenerClientePorId(idCliente);
        if (cliente == null) {
            System.err.println("Error: No existe un cliente con ID " + idCliente);
            return false;
        }
        
        // Aquí se podría verificar si el cliente tiene facturas asociadas
        // Si tiene facturas, se podría impedir la eliminación o permitirla con advertencia
        
        boolean resultado = clienteDAO.eliminarCliente(idCliente);
        
        if (resultado) {
            // Actualizar registro general
            List<RegistroGeneral> registros = registroGeneralDAO.obtenerRegistrosPorEntidad("Cliente", idCliente);
            if (!registros.isEmpty()) {
                RegistroGeneral registro = registros.get(0);
                registro.eliminarRegistro();
                registroGeneralDAO.eliminarRegistro(registro.getIdRegistro());
            }
            
            System.out.println("✓ Cliente eliminado exitosamente");
        }
        
        return resultado;
    }
    
    /**
     * Buscar un cliente por su ID
     * @param idCliente ID del cliente a buscar
     * @return Objeto Cliente si se encuentra, null en caso contrario
     */
    public Cliente buscarClientePorId(int idCliente) {
        if (idCliente <= 0) {
            System.err.println("Error: ID de cliente inválido");
            return null;
        }
        
        return clienteDAO.obtenerClientePorId(idCliente);
    }
    
    /**
     * Buscar un cliente por su NIT
     * @param nit NIT del cliente a buscar
     * @return Objeto Cliente si se encuentra, null en caso contrario
     */
    public Cliente buscarClientePorNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) {
            System.err.println("Error: NIT inválido");
            return null;
        }
        
        return clienteDAO.buscarPorNit(nit);
    }
    
    /**
     * Obtener todos los clientes registrados
     * @return Lista de todos los clientes
     */
    public List<Cliente> listarTodosClientes() {
        return clienteDAO.obtenerTodosClientes();
    }
    
    /**
     * Obtener clientes activos (no eliminados lógicamente)
     * Nota: Como usamos DELETE físico, todos los clientes son "activos"
     * @return Lista de clientes activos
     */
    public List<Cliente> listarClientesActivos() {
        // En nuestra implementación actual, todos los clientes son activos
        // Si se implementara soft delete, aquí se filtraría por estado
        return clienteDAO.obtenerTodosClientes();
    }
    
    /**
     * Validar todos los datos de un cliente
     * @param cliente Objeto Cliente a validar
     * @return true si todos los datos son válidos, false en caso contrario
     */
    public boolean validarCliente(Cliente cliente) {
        if (cliente == null) {
            System.err.println("Error: Cliente nulo");
            return false;
        }
        
        boolean valido = true;
        
        // Validar nombre
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            System.err.println("Error: El nombre del cliente es obligatorio");
            valido = false;
        } else if (cliente.getNombre().length() > 100) {
            System.err.println("Error: El nombre no puede superar los 100 caracteres");
            valido = false;
        }
        
        // Validar NIT
        if (cliente.getNit() == null || cliente.getNit().trim().isEmpty()) {
            System.err.println("Error: El NIT del cliente es obligatorio");
            valido = false;
        } else if (!PATRON_NIT.matcher(cliente.getNit()).matches()) {
            System.err.println("Error: Formato de NIT inválido. Debe ser: 123456789-0");
            valido = false;
        }
        
        // Validar correo (si se proporcionó)
        if (cliente.getCorreo() != null && !cliente.getCorreo().trim().isEmpty()) {
            if (!PATRON_CORREO.matcher(cliente.getCorreo()).matches()) {
                System.err.println("Error: Formato de correo electrónico inválido");
                valido = false;
            }
        }
        
        // Validar teléfono (si se proporcionó)
        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            if (!PATRON_TELEFONO.matcher(cliente.getTelefono()).matches()) {
                System.err.println("Error: Formato de teléfono inválido. Debe tener 7-15 dígitos");
                valido = false;
            }
        }
        
        return valido;
    }
    
    /**
     * Validar específicamente el NIT del cliente
     * @param nit NIT a validar
     * @return true si el NIT es válido, false en caso contrario
     */
    public boolean validarNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) {
            return false;
        }
        return PATRON_NIT.matcher(nit).matches();
    }
    
    /**
     * Validar específicamente el correo electrónico
     * @param correo Correo a validar
     * @return true si el correo es válido, false en caso contrario
     */
    public boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return true; // El correo es opcional
        }
        return PATRON_CORREO.matcher(correo).matches();
    }
    
    /**
     * Validar específicamente el teléfono
     * @param telefono Teléfono a validar
     * @return true si el teléfono es válido, false en caso contrario
     */
    public boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return true; // El teléfono es opcional
        }
        return PATRON_TELEFONO.matcher(telefono).matches();
    }
    
    /**
     * Obtener el número total de clientes registrados
     * @return Cantidad de clientes
     */
    public int obtenerTotalClientes() {
        return clienteDAO.obtenerTodosClientes().size();
    }
    
    /**
     * Buscar clientes por coincidencia en el nombre
     * @param nombreFragmento Fragmento del nombre a buscar
     * @return Lista de clientes que coinciden con el nombre
     */
    public List<Cliente> buscarClientesPorNombre(String nombreFragmento) {
        List<Cliente> todos = clienteDAO.obtenerTodosClientes();
        List<Cliente> resultados = new java.util.ArrayList<>();
        
        if (nombreFragmento == null || nombreFragmento.trim().isEmpty()) {
            return resultados;
        }
        
        String busqueda = nombreFragmento.toLowerCase().trim();
        for (Cliente c : todos) {
            if (c.getNombre().toLowerCase().contains(busqueda)) {
                resultados.add(c);
            }
        }
        
        return resultados;
    }
    
    /**
     * Verificar si un NIT ya está registrado
     * @param nit NIT a verificar
     * @return true si ya existe un cliente con ese NIT, false en caso contrario
     */
    public boolean nitExiste(String nit) {
        return clienteDAO.buscarPorNit(nit) != null;
    }
    
    /**
     * Obtener resumen de información de un cliente
     * @param idCliente ID del cliente
     * @return String con el resumen del cliente
     */
    public String obtenerResumenCliente(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            return "Cliente no encontrado";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMEN DEL CLIENTE ===\n");
        sb.append("ID: ").append(cliente.getIdCliente()).append("\n");
        sb.append("Nombre: ").append(cliente.getNombre()).append("\n");
        sb.append("NIT: ").append(cliente.getNit()).append("\n");
        sb.append("Dirección: ").append(cliente.getDireccion() != null ? cliente.getDireccion() : "No registrada").append("\n");
        sb.append("Teléfono: ").append(cliente.getTelefono() != null ? cliente.getTelefono() : "No registrado").append("\n");
        sb.append("Correo: ").append(cliente.getCorreo() != null ? cliente.getCorreo() : "No registrado").append("\n");
        sb.append("Fecha registro: ").append(cliente.getFechaRegistro()).append("\n");
        
        return sb.toString();
    }
}