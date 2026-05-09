// com.gestorempresarial.dao.FacturaDAO.java
package main.java.com.gestorempresarial.dao;

import com.gestorempresarial.modelo.Factura;
import com.gestorempresarial.modelo.Cliente;
import com.gestorempresarial.modelo.DetalleFactura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    
    private ClienteDAO clienteDAO = new ClienteDAO();
    private DetalleFacturaDAO detalleDAO = new DetalleFacturaDAO();
    
    // CREATE - Insertar factura
    public boolean insertarFactura(Factura factura) {
        String sql = "INSERT INTO facturas (numero_factura, fecha, estado, subtotal, "
                   + "total_iva, total, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                
                pstmt.setString(1, factura.getNumeroFactura());
                pstmt.setDate(2, new java.sql.Date(factura.getFecha().getTime()));
                pstmt.setString(3, factura.getEstado());
                pstmt.setDouble(4, factura.getSubtotal());
                pstmt.setDouble(5, factura.getTotalIva());
                pstmt.setDouble(6, factura.getTotal());
                pstmt.setInt(7, factura.getCliente().getIdCliente());
                
                int filasAfectadas = pstmt.executeUpdate();
                
                if (filasAfectadas > 0) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        int idFactura = rs.getInt(1);
                        factura.setIdFactura(idFactura);
                        
                        // Insertar detalles
                        for (DetalleFactura detalle : factura.getDetalles()) {
                            detalle.setFactura(factura);
                            detalleDAO.insertarDetalleFactura(detalle, conn);
                        }
                    }
                    conn.commit();
                    return true;
                }
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            System.err.println("Error al insertar factura: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error al restaurar autoCommit: " + e.getMessage());
            }
        }
    }
    
    // READ - Obtener factura por ID
    public Factura obtenerFacturaPorId(int id) {
        String sql = "SELECT * FROM facturas WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Factura factura = new Factura();
                factura.setIdFactura(rs.getInt("id_factura"));
                factura.setNumeroFactura(rs.getString("numero_factura"));
                factura.setFecha(rs.getDate("fecha"));
                factura.setEstado(rs.getString("estado"));
                factura.setSubtotal(rs.getDouble("subtotal"));
                factura.setTotalIva(rs.getDouble("total_iva"));
                factura.setTotal(rs.getDouble("total"));
                
                Cliente cliente = clienteDAO.obtenerClientePorId(rs.getInt("id_cliente"));
                factura.setCliente(cliente);
                
                List<DetalleFactura> detalles = detalleDAO.obtenerDetallesPorFactura(id);
                factura.setDetalles(detalles);
                
                return factura;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener factura: " + e.getMessage());
        }
        return null;
    }
    
    // READ - Obtener todas las facturas
    public List<Factura> obtenerTodasFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas ORDER BY fecha DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Factura factura = new Factura();
                factura.setIdFactura(rs.getInt("id_factura"));
                factura.setNumeroFactura(rs.getString("numero_factura"));
                factura.setFecha(rs.getDate("fecha"));
                factura.setEstado(rs.getString("estado"));
                factura.setSubtotal(rs.getDouble("subtotal"));
                factura.setTotalIva(rs.getDouble("total_iva"));
                factura.setTotal(rs.getDouble("total"));
                
                Cliente cliente = clienteDAO.obtenerClientePorId(rs.getInt("id_cliente"));
                factura.setCliente(cliente);
                
                List<DetalleFactura> detalles = detalleDAO.obtenerDetallesPorFactura(factura.getIdFactura());
                factura.setDetalles(detalles);
                
                facturas.add(factura);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar facturas: " + e.getMessage());
        }
        return facturas;
    }
    
    // UPDATE - Actualizar factura
    public boolean actualizarFactura(Factura factura) {
        String sql = "UPDATE facturas SET numero_factura = ?, fecha = ?, estado = ?, "
                   + "subtotal = ?, total_iva = ?, total = ?, id_cliente = ? WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, factura.getNumeroFactura());
            pstmt.setDate(2, new java.sql.Date(factura.getFecha().getTime()));
            pstmt.setString(3, factura.getEstado());
            pstmt.setDouble(4, factura.getSubtotal());
            pstmt.setDouble(5, factura.getTotalIva());
            pstmt.setDouble(6, factura.getTotal());
            pstmt.setInt(7, factura.getCliente().getIdCliente());
            pstmt.setInt(8, factura.getIdFactura());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar factura: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Anular/eliminar factura (soft delete)
    public boolean anularFactura(int id) {
        String sql = "UPDATE facturas SET estado = 'ANULADA' WHERE id_factura = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al anular factura: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Facturas por cliente
    public List<Factura> obtenerFacturasPorCliente(int idCliente) {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas WHERE id_cliente = ? ORDER BY fecha DESC";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Factura factura = new Factura();
                factura.setIdFactura(rs.getInt("id_factura"));
                factura.setNumeroFactura(rs.getString("numero_factura"));
                factura.setFecha(rs.getDate("fecha"));
                factura.setEstado(rs.getString("estado"));
                factura.setSubtotal(rs.getDouble("subtotal"));
                factura.setTotalIva(rs.getDouble("total_iva"));
                factura.setTotal(rs.getDouble("total"));
                
                Cliente cliente = clienteDAO.obtenerClientePorId(idCliente);
                factura.setCliente(cliente);
                
                facturas.add(factura);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener facturas por cliente: " + e.getMessage());
        }
        return facturas;
    }
}