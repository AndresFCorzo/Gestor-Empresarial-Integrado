package main.java.com.gestorempresarial.dao;

import com.gestorempresarial.modelo.DetalleFactura;
import com.gestorempresarial.modelo.Factura;
import com.gestorempresarial.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaDAO {
    
    private ProductoDAO productoDAO = new ProductoDAO();
    
    // CREATE - Insertar detalle
    public boolean insertarDetalleFactura(DetalleFactura detalle, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, "
                   + "precio_unitario, subtotal, valor_iva, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, detalle.getFactura().getIdFactura());
            pstmt.setInt(2, detalle.getProducto().getIdProducto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecioUnitario());
            pstmt.setDouble(5, detalle.getSubtotal());
            pstmt.setDouble(6, detalle.getValorIva());
            pstmt.setDouble(7, detalle.getTotal());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
                }
                return true;
            }
            return false;
        }
    }
    
    // READ - Obtener detalles por factura
    public List<DetalleFactura> obtenerDetallesPorFactura(int idFactura) {
        List<DetalleFactura> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalles_factura WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setIdDetalle(rs.getInt("id_detalle"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                
                Producto producto = productoDAO.obtenerProductoPorId(rs.getInt("id_producto"));
                detalle.setProducto(producto);
                
                // Forzar recalcular valores
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                
                detalles.add(detalle);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de factura: " + e.getMessage());
        }
        return detalles;
    }
    
    // DELETE - Eliminar detalle
    public boolean eliminarDetalleFactura(int idDetalle) {
        String sql = "DELETE FROM detalles_factura WHERE id_detalle = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idDetalle);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle: " + e.getMessage());
            return false;
        }
    }
    
    // UPDATE - Actualizar detalle
    public boolean actualizarDetalleFactura(DetalleFactura detalle) {
        String sql = "UPDATE detalles_factura SET cantidad = ?, precio_unitario = ?, "
                   + "subtotal = ?, valor_iva = ?, total = ? WHERE id_detalle = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, detalle.getCantidad());
            pstmt.setDouble(2, detalle.getPrecioUnitario());
            pstmt.setDouble(3, detalle.getSubtotal());
            pstmt.setDouble(4, detalle.getValorIva());
            pstmt.setDouble(5, detalle.getTotal());
            pstmt.setInt(6, detalle.getIdDetalle());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle: " + e.getMessage());
            return false;
        }
    }
}