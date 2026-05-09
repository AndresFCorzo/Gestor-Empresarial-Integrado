// com.gestorempresarial.modelo.Usuario.java
package main.java.com.gestorempresarial.modelo;

import java.util.Date;

/**
 * Clase que representa a un usuario del sistema Gestor Empresarial Integrado.
 * Un usuario puede tener diferentes roles como Administrador, Empleado, Contador, etc.
 * 
 * @author Andres Felipe Corzo Angarita
 * @author Thomas Felipe Colmenares Perdomo
 */
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
    private String preguntaSeguridad;
    private String respuestaSeguridad;
    
    // Constantes para roles de usuario
    public static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    public static final String ROL_EMPLEADO = "EMPLEADO";
    public static final String ROL_CONTADOR = "CONTADOR";
    public static final String ROL_GERENTE = "GERENTE";
    public static final String ROL_RRHH = "RECURSOS_HUMANOS";
    public static final String ROL_CLIENTE = "CLIENTE";
    
    // Constructores
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
    
    public Usuario(String nombre, String correo, String contrasena, String rol, 
                   String documentoIdentidad, String telefono) {
        this(nombre, correo, contrasena, rol);
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }
    
    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public Date getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public String getPreguntaSeguridad() {
        return preguntaSeguridad;
    }
    
    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }
    
    public String getRespuestaSeguridad() {
        return respuestaSeguridad;
    }
    
    public void setRespuestaSeguridad(String respuestaSeguridad) {
        this.respuestaSeguridad = respuestaSeguridad;
    }
    
    // Métodos de negocio
    public void registrarUsuario() {
        this.fechaRegistro = new Date();
        this.activo = true;
        System.out.println("Usuario registrado: " + nombre + " - Rol: " + rol);
    }
    
    public void actualizarUsuario() {
        System.out.println("Usuario actualizado: " + nombre);
    }
    
    public void eliminarUsuario() {
        this.activo = false;
        System.out.println("Usuario eliminado (desactivado): " + nombre);
    }
    
    public void activarUsuario() {
        this.activo = true;
        System.out.println("Usuario activado: " + nombre);
    }
    
    public void registrarAcceso() {
        this.ultimoAcceso = new Date();
        System.out.println("Acceso registrado para: " + nombre + " a las " + ultimoAcceso);
    }
    
    public boolean autenticar(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }
    
    public void cambiarContrasena(String nuevaContrasena) {
        this.contrasena = nuevaContrasena;
        System.out.println("Contraseña actualizada para: " + nombre);
    }
    
    public boolean tieneRol(String rolBuscado) {
        return this.rol != null && this.rol.equals(rolBuscado);
    }
    
    public boolean esAdministrador() {
        return ROL_ADMINISTRADOR.equals(this.rol);
    }
    
    public boolean esEmpleado() {
        return ROL_EMPLEADO.equals(this.rol);
    }
    
    public boolean esContador() {
        return ROL_CONTADOR.equals(this.rol);
    }
    
    public boolean esGerente() {
        return ROL_GERENTE.equals(this.rol);
    }
    
    public boolean esRRHH() {
        return ROL_RRHH.equals(this.rol);
    }
    
    // Obtener nombre legible del rol
    public String getRolNombre() {
        switch (rol) {
            case ROL_ADMINISTRADOR:
                return "Administrador";
            case ROL_EMPLEADO:
                return "Empleado";
            case ROL_CONTADOR:
                return "Contador";
            case ROL_GERENTE:
                return "Gerente";
            case ROL_RRHH:
                return "Recursos Humanos";
            case ROL_CLIENTE:
                return "Cliente";
            default:
                return "Rol Desconocido";
        }
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}