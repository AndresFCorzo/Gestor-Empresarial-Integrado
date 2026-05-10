package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.DetalleFactura;
import com.gestorempresarial.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de detalles de factura
 * @author Andres Felipe Corzo Angarita
 * @author Thomas Felipe Colmenares Perdomo
 */
public class DetalleFacturaDAO {
    
    private ProductoDAO productoDAO = new ProductoDAO();
    
    /**
     * Insertar un detalle de factura (usando conexión existente para transacciones)
     * @param detalle DetalleFactura a insertar
     * @param conn Conexión existente (para transacción con FacturaDAO)
     * @return true si se insertó correctamente
     * @throws SQLException
     */
    public boolean insertarDetalle(DetalleFactura detalle, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal, valor_iva, total) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, detalle.getFactura().getIdFactura());
            pstmt.setInt(2, detalle.getProducto().getIdProducto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecioUnitario());
            pstmt.setDouble(5, detalle.getSubtotal());
            pstmt.setDouble(6, detalle.getValorIva());
            pstmt.setDouble(7, detalle.getTotal());
            
            int afectadas = pstmt.executeUpdate();
            if (afectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
                }
                return true;
            }
            return false;
        }
    }
    
    /**
     * Insertar un detalle de factura (con conexión propia, sin transacción)
     * @param detalle DetalleFactura a insertar
     * @return true si se insertó correctamente
     */
    public boolean insertarDetalle(DetalleFactura detalle) {
        String sql = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal, valor_iva, total) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, detalle.getFactura().getIdFactura());
            pstmt.setInt(2, detalle.getProducto().getIdProducto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecioUnitario());
            pstmt.setDouble(5, detalle.getSubtotal());
            pstmt.setDouble(6, detalle.getValorIva());
            pstmt.setDouble(7, detalle.getTotal());
            
            int afectadas = pstmt.executeUpdate();
            if (afectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    detalle.setIdDetalle(rs.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtener todos los detalles de una factura específica
     * @param idFactura ID de la factura
     * @return Lista de detalles de factura
     */
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
                
                // Obtener el producto asociado
                int idProducto = rs.getInt("id_producto");
                Producto producto = productoDAO.buscarPorId(idProducto);
                detalle.setProducto(producto);
                
                detalles.add(detalle);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles de factura: " + e.getMessage());
        }
        return detalles;
    }
    
    /**
     * Obtener un detalle específico por su ID
     * @param idDetalle ID del detalle
     * @return DetalleFactura encontrado o null
     */
    public DetalleFactura obtenerDetallePorId(int idDetalle) {
        String sql = "SELECT * FROM detalles_factura WHERE id_detalle = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idDetalle);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setIdDetalle(rs.getInt("id_detalle"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                
                int idProducto = rs.getInt("id_producto");
                Producto producto = productoDAO.buscarPorId(idProducto);
                detalle.setProducto(producto);
                
                return detalle;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener detalle por ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Actualizar un detalle de factura
     * @param detalle DetalleFactura con los datos actualizados
     * @return true si se actualizó correctamente
     */
    public boolean actualizarDetalle(DetalleFactura detalle) {
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
    
    /**
     * Eliminar un detalle de factura
     * @param idDetalle ID del detalle a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarDetalle(int idDetalle) {
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
    
    /**
     * Eliminar todos los detalles de una factura (útil al anular factura)
     * @param idFactura ID de la factura
     * @return true si se eliminaron correctamente
     */
    public boolean eliminarDetallesPorFactura(int idFactura) {
        String sql = "DELETE FROM detalles_factura WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalles de factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtener el total de detalles de una factura
     * @param idFactura ID de la factura
     * @return Número de detalles en la factura
     */
    public int contarDetallesPorFactura(int idFactura) {
        String sql = "SELECT COUNT(*) FROM detalles_factura WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar detalles: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Obtener el subtotal total de una factura (suma de subtotales de detalles)
     * @param idFactura ID de la factura
     * @return Subtotal total de la factura
     */
    public double obtenerSubtotalFactura(int idFactura) {
        String sql = "SELECT SUM(subtotal) as total FROM detalles_factura WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular subtotal: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Obtener el IVA total de una factura (suma de IVA de detalles)
     * @param idFactura ID de la factura
     * @return IVA total de la factura
     */
    public double obtenerIvaFactura(int idFactura) {
        String sql = "SELECT SUM(valor_iva) as total FROM detalles_factura WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular IVA: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Obtener el total de una factura (suma de totales de detalles)
     * @param idFactura ID de la factura
     * @return Total de la factura
     */
    public double obtenerTotalFactura(int idFactura) {
        String sql = "SELECT SUM(total) as total FROM detalles_factura WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idFactura);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al calcular total: " + e.getMessage());
        }
        return 0;
    }
}