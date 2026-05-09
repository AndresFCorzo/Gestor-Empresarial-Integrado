// com.gestorempresarial.modelo.DetalleFactura.java
package com.gestorempresarial.modelo;

public class DetalleFactura {
    
    private int idDetalle;
    private Factura factura;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private double valorIva;
    private double total;
    
    public DetalleFactura() {}
    
    public DetalleFactura(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        calcularValores();
    }
    
    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    
    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { 
        this.producto = producto;
        this.precioUnitario = producto.getPrecio();
        calcularValores();
    }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        calcularValores();
    }
    
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { 
        this.precioUnitario = precioUnitario;
        calcularValores();
    }
    
    public double getSubtotal() { return subtotal; }
    public double getValorIva() { return valorIva; }
    public double getTotal() { return total; }
    
    private void calcularValores() {
        this.subtotal = cantidad * precioUnitario;
        if (producto != null && producto.isAplicaIva()) {
            this.valorIva = subtotal * producto.getPorcentajeIva() / 100;
        } else {
            this.valorIva = 0;
        }
        this.total = subtotal + valorIva;
    }
    
    public void agregarDetalle() {
        System.out.println("Detalle agregado: " + cantidad + " x " + producto.getNombre());
    }
    
    public void actualizarDetalle() {
        calcularValores();
        System.out.println("Detalle actualizado");
    }
    
    public void eliminarDetalle() {
        System.out.println("Detalle eliminado: " + producto.getNombre());
    }
}