package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (nombre, codigo, precio, aplica_iva, porcentaje_iva, stock, categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCodigo());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setBoolean(4, producto.isAplicaIva());
            pstmt.setDouble(5, producto.getPorcentajeIva());
            pstmt.setInt(6, producto.getStock());
            pstmt.setString(7, producto.getCategoria());
            int afectadas = pstmt.executeUpdate();
            if (afectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) producto.setIdProducto(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setCodigo(rs.getString("codigo"));
                p.setPrecio(rs.getDouble("precio"));
                p.setAplicaIva(rs.getBoolean("aplica_iva"));
                p.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
                p.setStock(rs.getInt("stock"));
                p.setCategoria(rs.getString("categoria"));
                productos.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return productos;
    }
    
    public Producto buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setCodigo(rs.getString("codigo"));
                p.setPrecio(rs.getDouble("precio"));
                p.setAplicaIva(rs.getBoolean("aplica_iva"));
                p.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
                p.setStock(rs.getInt("stock"));
                p.setCategoria(rs.getString("categoria"));
                return p;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setCodigo(rs.getString("codigo"));
                p.setPrecio(rs.getDouble("precio"));
                p.setAplicaIva(rs.getBoolean("aplica_iva"));
                p.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
                p.setStock(rs.getInt("stock"));
                p.setCategoria(rs.getString("categoria"));
                return p;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre=?, codigo=?, precio=?, aplica_iva=?, porcentaje_iva=?, stock=?, categoria=? WHERE id_producto=?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCodigo());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setBoolean(4, producto.isAplicaIva());
            pstmt.setDouble(5, producto.getPorcentajeIva());
            pstmt.setInt(6, producto.getStock());
            pstmt.setString(7, producto.getCategoria());
            pstmt.setInt(8, producto.getIdProducto());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public boolean actualizarStock(int idProducto, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idProducto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}