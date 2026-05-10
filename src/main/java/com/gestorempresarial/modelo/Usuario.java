package com.gestorempresarial.modelo;

import java.util.Date;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol;
    private String documentoIdentidad;
    private String telefono;
    private String direccion;
    private Date fechaRegistro;
    private Date ultimoAcceso;
    private boolean activo;
    
    public static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    public static final String ROL_EMPLEADO = "EMPLEADO";
    public static final String ROL_CONTADOR = "CONTADOR";
    public static final String ROL_GERENTE = "GERENTE";
    
    public Usuario() {
        this.fechaRegistro = new Date();
        this.activo = true;
    }
    
    public Usuario(String nombre, String correo, String contrasena, String rol) {
        this();
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }
    
    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Date getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(Date ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public boolean autenticar(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }
    
    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombre=" + nombre + ", correo=" + correo + ", rol=" + rol + '}';
    }
}