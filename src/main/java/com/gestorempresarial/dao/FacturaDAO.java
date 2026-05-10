package com.gestorempresarial.dao;

import com.gestorempresarial.modelo.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    
    public boolean insertar(Factura factura) {
        String sql = "INSERT INTO facturas (numero_factura, fecha, estado, subtotal, total_iva, total, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                int afectadas = pstmt.executeUpdate();
                if (afectadas > 0) {
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        factura.setIdFactura(rs.getInt(1));
                        for (DetalleFactura detalle : factura.getDetalles()) {
                            detalle.setFactura(factura);
                            insertarDetalle(detalle, conn);
                        }
                    }
                    conn.commit();
                    return true;
                }
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) {}
        }
    }
    
    private void insertarDetalle(DetalleFactura detalle, Connection conn) throws SQLException {
        String sql = "INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal, valor_iva, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, detalle.getFactura().getIdFactura());
            pstmt.setInt(2, detalle.getProducto().getIdProducto());
            pstmt.setInt(3, detalle.getCantidad());
            pstmt.setDouble(4, detalle.getPrecioUnitario());
            pstmt.setDouble(5, detalle.getSubtotal());
            pstmt.setDouble(6, detalle.getValorIva());
            pstmt.setDouble(7, detalle.getTotal());
            pstmt.executeUpdate();
        }
    }
    
    public List<Factura> listarTodas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas ORDER BY fecha DESC";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("id_factura"));
                f.setNumeroFactura(rs.getString("numero_factura"));
                f.setFecha(rs.getDate("fecha"));
                f.setEstado(rs.getString("estado"));
                f.setSubtotal(rs.getDouble("subtotal"));
                f.setTotalIva(rs.getDouble("total_iva"));
                f.setTotal(rs.getDouble("total"));
                Cliente cliente = clienteDAO.buscarPorId(rs.getInt("id_cliente"));
                f.setCliente(cliente);
                f.setDetalles(obtenerDetalles(f.getIdFactura()));
                facturas.add(f);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return facturas;
    }
    
    public Factura buscarPorId(int id) {
        String sql = "SELECT * FROM facturas WHERE id_factura = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("id_factura"));
                f.setNumeroFactura(rs.getString("numero_factura"));
                f.setFecha(rs.getDate("fecha"));
                f.setEstado(rs.getString("estado"));
                f.setSubtotal(rs.getDouble("subtotal"));
                f.setTotalIva(rs.getDouble("total_iva"));
                f.setTotal(rs.getDouble("total"));
                Cliente cliente = clienteDAO.buscarPorId(rs.getInt("id_cliente"));
                f.setCliente(cliente);
                f.setDetalles(obtenerDetalles(f.getIdFactura()));
                return f;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public List<DetalleFactura> obtenerDetalles(int idFactura) {
        List<DetalleFactura> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalles_factura WHERE id_factura = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFactura);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DetalleFactura d = new DetalleFactura();
                d.setIdDetalle(rs.getInt("id_detalle"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                Producto p = productoDAO.buscarPorId(rs.getInt("id_producto"));
                d.setProducto(p);
                detalles.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return detalles;
    }
    
    public boolean anular(int id) {
        String sql = "UPDATE facturas SET estado = 'ANULADA' WHERE id_factura = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    
    public List<Factura> listarPorCliente(int idCliente) {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas WHERE id_cliente = ? ORDER BY fecha DESC";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Factura f = new Factura();
                f.setIdFactura(rs.getInt("id_factura"));
                f.setNumeroFactura(rs.getString("numero_factura"));
                f.setFecha(rs.getDate("fecha"));
                f.setEstado(rs.getString("estado"));
                f.setTotal(rs.getDouble("total"));
                facturas.add(f);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return facturas;
    }
}