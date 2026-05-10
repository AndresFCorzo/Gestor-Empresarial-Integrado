package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, nit, direccion, correo, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getNit());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getCorreo());
            pstmt.setString(5, cliente.getTelefono());
            pstmt.setDate(6, new java.sql.Date(cliente.getFechaRegistro().getTime()));
            int afectadas = pstmt.executeUpdate();
            if (afectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) cliente.setIdCliente(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setNit(rs.getString("nit"));
                c.setDireccion(rs.getString("direccion"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                c.setFechaRegistro(rs.getDate("fecha_registro"));
                clientes.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return clientes;
    }
    
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setNit(rs.getString("nit"));
                c.setDireccion(rs.getString("direccion"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                c.setFechaRegistro(rs.getDate("fecha_registro"));
                return c;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public Cliente buscarPorNit(String nit) {
        String sql = "SELECT * FROM clientes WHERE nit = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nit);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setNit(rs.getString("nit"));
                c.setDireccion(rs.getString("direccion"));
                c.setCorreo(rs.getString("correo"));
                c.setTelefono(rs.getString("telefono"));
                c.setFechaRegistro(rs.getDate("fecha_registro"));
                return c;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre=?, nit=?, direccion=?, correo=?, telefono=? WHERE id_cliente=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getNit());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getCorreo());
            pstmt.setString(5, cliente.getTelefono());
            pstmt.setInt(6, cliente.getIdCliente());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}