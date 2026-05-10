package main.java.com.gestorempresarial.modelo;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int idFactura;
    private String numeroFactura;
    private Date fecha;
    private String estado;
    private double subtotal;
    private double totalIva;
    private double total;
    private Cliente cliente;
    private List<DetalleFactura> detalles;
    
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_EMITIDA = "EMITIDA";
    public static final String ESTADO_ANULADA = "ANULADA";
    
    public Factura() {
        this.detalles = new ArrayList<>();
        this.fecha = new Date();
        this.estado = ESTADO_PENDIENTE;
    }
    
    public Factura(Cliente cliente, String numeroFactura) {
        this();
        this.cliente = cliente;
        this.numeroFactura = numeroFactura;
    }
    
    // Getters y Setters
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getTotalIva() { return totalIva; }
    public void setTotalIva(double totalIva) { this.totalIva = totalIva; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<DetalleFactura> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleFactura> detalles) { this.detalles = detalles; }
    
    public void agregarDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
        calcularTotales();
    }
    
    public void calcularTotales() {
        subtotal = 0;
        totalIva = 0;
        total = 0;
        for (DetalleFactura detalle : detalles) {
            subtotal += detalle.getSubtotal();
            totalIva += detalle.getValorIva();
            total += detalle.getTotal();
        }
    }
    
    public void emitir() {
        if (cliente != null && !detalles.isEmpty()) {
            this.estado = ESTADO_EMITIDA;
        }
    }
    
    public void anular() {
        this.estado = ESTADO_ANULADA;
    }
}