package com.gestorempresarial.utils;

import java.util.regex.Pattern;

public class Validaciones {
    
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PATRON_NIT = Pattern.compile("^[0-9]{1,10}-[0-9]$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^[0-9]{7,15}$");
    private static final Pattern PATRON_CODIGO = Pattern.compile("^[A-Za-z0-9-]{3,20}$");
    
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return PATRON_EMAIL.matcher(email).matches();
    }
    
    public static boolean validarNit(String nit) {
        if (nit == null || nit.trim().isEmpty()) return false;
        return PATRON_NIT.matcher(nit).matches();
    }
    
    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) return true;
        return PATRON_TELEFONO.matcher(telefono).matches();
    }
    
    public static boolean validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return false;
        return PATRON_CODIGO.matcher(codigo).matches();
    }
    
    public static boolean validarRequerido(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
    
    public static boolean validarPositivo(double valor) {
        return valor > 0;
    }
    
    public static boolean validarValorAtipico(double valor) {
        if (valor <= 0) return false;
        if (valor > 100000000) return false;
        return true;
    }
    
    public static String validarProducto(String nombre, String codigo, double precio) {
        if (!validarRequerido(nombre)) return "El nombre es obligatorio";
        if (!validarRequerido(codigo)) return "El código es obligatorio";
        if (!validarCodigo(codigo)) return "El código debe tener 3-20 caracteres alfanuméricos y guiones";
        if (!validarPositivo(precio)) return "El precio debe ser mayor a cero";
        return null;
    }
    
    public static String validarCliente(String nombre, String nit) {
        if (!validarRequerido(nombre)) return "El nombre es obligatorio";
        if (!validarRequerido(nit)) return "El NIT es obligatorio";
        if (!validarNit(nit)) return "Formato de NIT inválido (Ej: 900123456-1)";
        return null;
    }
}