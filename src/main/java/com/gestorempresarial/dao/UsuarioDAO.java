// com.gestorempresarial.dao.UsuarioDAO.java
package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol, documento_identidad, telefono, fecha_registro, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getRol());
            pstmt.setString(5, usuario.getDocumentoIdentidad());
            pstmt.setString(6, usuario.getTelefono());
            pstmt.setDate(7, new java.sql.Date(usuario.getFechaRegistro().getTime()));
            pstmt.setBoolean(8, usuario.isActivo());
            int afectadas = pstmt.executeUpdate();
            if (afectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) usuario.setIdUsuario(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public Usuario autenticar(String correo, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ? AND activo = true";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasena(rs.getString("contrasena"));
                u.setRol(rs.getString("rol"));
                u.setDocumentoIdentidad(rs.getString("documento_identidad"));
                u.setTelefono(rs.getString("telefono"));
                u.setFechaRegistro(rs.getDate("fecha_registro"));
                u.setActivo(rs.getBoolean("activo"));
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setRol(rs.getString("rol"));
                u.setActivo(rs.getBoolean("activo"));
                usuarios.add(u);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return usuarios;
    }
}