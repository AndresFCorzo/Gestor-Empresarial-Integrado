// com.gestorempresarial.dao.ProductoDAO.java
package main.java.com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    // CREATE - Insertar producto
    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, codigo, precio, aplica_iva, "
                   + "porcentaje_iva, stock, categoria) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCodigo());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setBoolean(4, producto.isAplicaIva());
            pstmt.setDouble(5, producto.getPorcentajeIva());
            pstmt.setInt(6, producto.getStock());
            pstmt.setString(7, producto.getCategoria());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    producto.setIdProducto(rs.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Obtener producto por ID
    public Producto obtenerProductoPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setAplicaIva(rs.getBoolean("aplica_iva"));
                producto.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                return producto;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Obtener todos los productos
    public List<Producto> obtenerTodosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY nombre";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setAplicaIva(rs.getBoolean("aplica_iva"));
                producto.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return productos;
    }
    
    // UPDATE - Actualizar producto
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, codigo = ?, precio = ?, "
                   + "aplica_iva = ?, porcentaje_iva = ?, stock = ?, categoria = ? "
                   + "WHERE id_producto = ?";
        
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
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Eliminar producto
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Buscar producto por código
    public Producto buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setCodigo(rs.getString("codigo"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setAplicaIva(rs.getBoolean("aplica_iva"));
                producto.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
                producto.setStock(rs.getInt("stock"));
                producto.setCategoria(rs.getString("categoria"));
                return producto;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
        }
        return null;
    }
    
    // UPDATE - Actualizar stock
    public boolean actualizarStock(int idProducto, int nuevaCantidad) {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, nuevaCantidad);
            pstmt.setInt(2, idProducto);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
}