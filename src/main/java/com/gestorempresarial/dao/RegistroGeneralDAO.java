// com.gestorempresarial.modelo.RegistroGeneralDAO.java
package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.RegistroGeneral;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroGeneralDAO {
    
    // CREATE - Insertar registro general
    public boolean insertarRegistro(RegistroGeneral registro) {
        String sql = "INSERT INTO registros_generales (tipo_registro, fecha_registro, descripcion, "
                   + "id_relacionado, entidad_relacionada, estado, usuario_registro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, registro.getTipoRegistro());
            pstmt.setDate(2, new java.sql.Date(registro.getFechaRegistro().getTime()));
            pstmt.setString(3, registro.getDescripcion());
            pstmt.setInt(4, registro.getIdRelacionado());
            pstmt.setString(5, registro.getEntidadRelacionada());
            pstmt.setString(6, registro.getEstado());
            pstmt.setString(7, registro.getUsuarioRegistro());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    registro.setIdRegistro(rs.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar registro general: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Obtener registro por ID
    public RegistroGeneral obtenerRegistroPorId(int id) {
        String sql = "SELECT * FROM registros_generales WHERE id_registro = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                RegistroGeneral registro = new RegistroGeneral();
                registro.setIdRegistro(rs.getInt("id_registro"));
                registro.setTipoRegistro(rs.getString("tipo_registro"));
                registro.setFechaRegistro(rs.getDate("fecha_registro"));
                registro.setDescripcion(rs.getString("descripcion"));
                registro.setIdRelacionado(rs.getInt("id_relacionado"));
                registro.setEntidadRelacionada(rs.getString("entidad_relacionada"));
                registro.setEstado(rs.getString("estado"));
                registro.setUsuarioRegistro(rs.getString("usuario_registro"));
                return registro;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registro general: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Obtener registros por tipo
    public List<RegistroGeneral> obtenerRegistrosPorTipo(String tipoRegistro) {
        List<RegistroGeneral> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros_generales WHERE tipo_registro = ? ORDER BY fecha_registro DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tipoRegistro);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RegistroGeneral registro = new RegistroGeneral();
                registro.setIdRegistro(rs.getInt("id_registro"));
                registro.setTipoRegistro(rs.getString("tipo_registro"));
                registro.setFechaRegistro(rs.getDate("fecha_registro"));
                registro.setDescripcion(rs.getString("descripcion"));
                registro.setIdRelacionado(rs.getInt("id_relacionado"));
                registro.setEntidadRelacionada(rs.getString("entidad_relacionada"));
                registro.setEstado(rs.getString("estado"));
                registro.setUsuarioRegistro(rs.getString("usuario_registro"));
                registros.add(registro);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros por tipo: " + e.getMessage());
        }
        return registros;
    }
    
    // READ - Obtener todos los registros
    public List<RegistroGeneral> obtenerTodosRegistros() {
        List<RegistroGeneral> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros_generales ORDER BY fecha_registro DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                RegistroGeneral registro = new RegistroGeneral();
                registro.setIdRegistro(rs.getInt("id_registro"));
                registro.setTipoRegistro(rs.getString("tipo_registro"));
                registro.setFechaRegistro(rs.getDate("fecha_registro"));
                registro.setDescripcion(rs.getString("descripcion"));
                registro.setIdRelacionado(rs.getInt("id_relacionado"));
                registro.setEntidadRelacionada(rs.getString("entidad_relacionada"));
                registro.setEstado(rs.getString("estado"));
                registro.setUsuarioRegistro(rs.getString("usuario_registro"));
                registros.add(registro);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar registros generales: " + e.getMessage());
        }
        return registros;
    }
    
    // UPDATE - Actualizar registro
    public boolean actualizarRegistro(RegistroGeneral registro) {
        String sql = "UPDATE registros_generales SET tipo_registro = ?, descripcion = ?, "
                   + "estado = ? WHERE id_registro = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, registro.getTipoRegistro());
            pstmt.setString(2, registro.getDescripcion());
            pstmt.setString(3, registro.getEstado());
            pstmt.setInt(4, registro.getIdRegistro());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar registro general: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Eliminar registro (soft delete)
    public boolean eliminarRegistro(int id) {
        String sql = "UPDATE registros_generales SET estado = ? WHERE id_registro = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, RegistroGeneral.ESTADO_INACTIVO);
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar registro general: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Obtener registros por entidad relacionada
    public List<RegistroGeneral> obtenerRegistrosPorEntidad(String entidadRelacionada, int idRelacionado) {
        List<RegistroGeneral> registros = new ArrayList<>();
        String sql = "SELECT * FROM registros_generales WHERE entidad_relacionada = ? AND id_relacionado = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entidadRelacionada);
            pstmt.setInt(2, idRelacionado);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                RegistroGeneral registro = new RegistroGeneral();
                registro.setIdRegistro(rs.getInt("id_registro"));
                registro.setTipoRegistro(rs.getString("tipo_registro"));
                registro.setFechaRegistro(rs.getDate("fecha_registro"));
                registro.setDescripcion(rs.getString("descripcion"));
                registro.setIdRelacionado(rs.getInt("id_relacionado"));
                registro.setEntidadRelacionada(rs.getString("entidad_relacionada"));
                registro.setEstado(rs.getString("estado"));
                registro.setUsuarioRegistro(rs.getString("usuario_registro"));
                registros.add(registro);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener registros por entidad: " + e.getMessage());
        }
        return registros;
    }
}