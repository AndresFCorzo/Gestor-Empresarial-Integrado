// com.gestorempresarial.dao.ClienteDAO.java
package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    // CREATE - Insertar cliente
    public boolean insertarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, nit, direccion, correo, telefono, fecha_registro) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getNit());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getCorreo());
            pstmt.setString(5, cliente.getTelefono());
            pstmt.setDate(6, new java.sql.Date(cliente.getFechaRegistro().getTime()));
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Consultar cliente por ID
    public Cliente obtenerClientePorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setNit(rs.getString("nit"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setFechaRegistro(rs.getDate("fecha_registro"));
                return cliente;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Consultar todos los clientes
    public List<Cliente> obtenerTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setNit(rs.getString("nit"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setFechaRegistro(rs.getDate("fecha_registro"));
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }
    
    // UPDATE - Actualizar cliente
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, nit = ?, direccion = ?, "
                   + "correo = ?, telefono = ? WHERE id_cliente = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getNit());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getCorreo());
            pstmt.setString(5, cliente.getTelefono());
            pstmt.setInt(6, cliente.getIdCliente());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Eliminar cliente
    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Buscar cliente por NIT
    public Cliente buscarPorNit(String nit) {
        String sql = "SELECT * FROM clientes WHERE nit = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nit);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setNit(rs.getString("nit"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setFechaRegistro(rs.getDate("fecha_registro"));
                return cliente;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por NIT: " + e.getMessage());
        }
        return null;
    }
} 