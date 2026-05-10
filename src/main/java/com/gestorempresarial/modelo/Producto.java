package com.gestorempresarial.modelo;

public class Producto {
    private int idProducto;
    private String nombre;
    private String codigo;
    private double precio;
    private boolean aplicaIva;
    private double porcentajeIva;
    private int stock;
    private String categoria;
    
    public Producto() {}
    
    public Producto(String nombre, String codigo, double precio, boolean aplicaIva, 
                    double porcentajeIva, int stock, String categoria) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.aplicaIva = aplicaIva;
        this.porcentajeIva = aplicaIva ? porcentajeIva : 0;
        this.stock = stock;
        this.categoria = categoria;
    }
    
    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public boolean isAplicaIva() { return aplicaIva; }
    public void setAplicaIva(boolean aplicaIva) { this.aplicaIva = aplicaIva; }
    public double getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(double porcentajeIva) { this.porcentajeIva = porcentajeIva; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public double calcularPrecioConIva() {
        if (aplicaIva) {
            return precio + (precio * porcentajeIva / 100);
        }
        return precio;
    }
    
    @Override
    public String toString() {
        return nombre + " - $" + precio + " (Stock: " + stock + ")";
    }
}