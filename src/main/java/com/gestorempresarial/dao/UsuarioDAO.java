// com.gestorempresarial.dao.UsuarioDAO.java
package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    // CREATE - Insertar usuario
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, rol, documento_identidad, "
                   + "telefono, direccion, fecha_registro, activo, pregunta_seguridad, respuesta_seguridad) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getContrasena());
            pstmt.setString(4, usuario.getRol());
            pstmt.setString(5, usuario.getDocumentoIdentidad());
            pstmt.setString(6, usuario.getTelefono());
            pstmt.setString(7, usuario.getDireccion());
            pstmt.setDate(8, new java.sql.Date(usuario.getFechaRegistro().getTime()));
            pstmt.setBoolean(9, usuario.isActivo());
            pstmt.setString(10, usuario.getPreguntaSeguridad());
            pstmt.setString(11, usuario.getRespuestaSeguridad());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Obtener usuario por ID
    public Usuario obtenerUsuarioPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Obtener usuario por correo (para login)
    public Usuario obtenerUsuarioPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Autenticar usuario
    public Usuario autenticar(String correo, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ? AND activo = true";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = mapearUsuario(rs);
                // Actualizar último acceso
                actualizarUltimoAcceso(usuario.getIdUsuario());
                return usuario;
            }
            
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }
    
    // READ - Obtener usuarios por rol
    public List<Usuario> obtenerUsuariosPorRol(String rol) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE rol = ? ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rol);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios por rol: " + e.getMessage());
        }
        return usuarios;
    }
    
    // READ - Obtener usuarios activos
    public List<Usuario> obtenerUsuariosActivos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE activo = true ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios activos: " + e.getMessage());
        }
        return usuarios;
    }
    
    // UPDATE - Actualizar usuario
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, correo = ?, rol = ?, "
                   + "documento_identidad = ?, telefono = ?, direccion = ? "
                   + "WHERE id_usuario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getNombre());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getRol());
            pstmt.setString(4, usuario.getDocumentoIdentidad());
            pstmt.setString(5, usuario.getTelefono());
            pstmt.setString(6, usuario.getDireccion());
            pstmt.setInt(7, usuario.getIdUsuario());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // UPDATE - Cambiar contraseña
    public boolean cambiarContrasena(int idUsuario, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevaContrasena);
            pstmt.setInt(2, idUsuario);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }
    
    // UPDATE - Actualizar último acceso
    public boolean actualizarUltimoAcceso(int idUsuario) {
        String sql = "UPDATE usuarios SET ultimo_acceso = ? WHERE id_usuario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, idUsuario);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar último acceso: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Eliminar usuario (soft delete)
    public boolean eliminarUsuario(int id) {
        String sql = "UPDATE usuarios SET activo = false WHERE id_usuario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Hard delete (eliminar físicamente)
    public boolean eliminarUsuarioPermanente(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario permanentemente: " + e.getMessage());
            return false;
        }
    }
    
    // Método auxiliar para mapear ResultSet a Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setRol(rs.getString("rol"));
        usuario.setDocumentoIdentidad(rs.getString("documento_identidad"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setFechaRegistro(rs.getDate("fecha_registro"));
        usuario.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setPreguntaSeguridad(rs.getString("pregunta_seguridad"));
        usuario.setRespuestaSeguridad(rs.getString("respuesta_seguridad"));
        return usuario;
    }
}