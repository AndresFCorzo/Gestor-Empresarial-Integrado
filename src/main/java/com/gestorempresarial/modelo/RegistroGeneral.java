// com.gestorempresarial.modelo.RegistroGeneral.java
package main.java.com.gestorempresarial.modelo;

import java.util.Date;

/**
 * Clase que actúa como entidad central del sistema, relacionando
 * clientes, empleados, facturas, viáticos, nóminas, documentos y reportes contables.
 * 
 * @author Andres Felipe Corzo Angarita
 * @author Thomas Felipe Colmenares Perdomo
 */
public class RegistroGeneral {
    
    private int idRegistro;
    private String tipoRegistro;
    private Date fechaRegistro;
    private String descripcion;
    private int idRelacionado;
    private String entidadRelacionada;
    private String estado;
    private String usuarioRegistro;
    
    // Constantes para tipos de registro
    public static final String TIPO_CLIENTE = "CLIENTE";
    public static final String TIPO_EMPLEADO = "EMPLEADO";
    public static final String TIPO_FACTURA = "FACTURA";
    public static final String TIPO_DOCUMENTO = "DOCUMENTO";
    public static final String TIPO_VIATICO = "VIATICO";
    public static final String TIPO_NOMINA = "NOMINA";
    public static final String TIPO_REPORTE = "REPORTE_CONTABLE";
    public static final String TIPO_PRODUCTO = "PRODUCTO";
    public static final String TIPO_USUARIO = "USUARIO";
    
    // Constantes para estados
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_INACTIVO = "INACTIVO";
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_COMPLETADO = "COMPLETADO";
    
    // Constructores
    public RegistroGeneral() {
        this.fechaRegistro = new Date();
        this.estado = ESTADO_ACTIVO;
    }
    
    public RegistroGeneral(String tipoRegistro, String descripcion, int idRelacionado, String entidadRelacionada) {
        this();
        this.tipoRegistro = tipoRegistro;
        this.descripcion = descripcion;
        this.idRelacionado = idRelacionado;
        this.entidadRelacionada = entidadRelacionada;
    }
    
    public RegistroGeneral(String tipoRegistro, String descripcion, int idRelacionado, 
                          String entidadRelacionada, String usuarioRegistro) {
        this(tipoRegistro, descripcion, idRelacionado, entidadRelacionada);
        this.usuarioRegistro = usuarioRegistro;
    }
    
    // Getters y Setters
    public int getIdRegistro() {
        return idRegistro;
    }
    
    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }
    
    public String getTipoRegistro() {
        return tipoRegistro;
    }
    
    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
    
    public Date getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getIdRelacionado() {
        return idRelacionado;
    }
    
    public void setIdRelacionado(int idRelacionado) {
        this.idRelacionado = idRelacionado;
    }
    
    public String getEntidadRelacionada() {
        return entidadRelacionada;
    }
    
    public void setEntidadRelacionada(String entidadRelacionada) {
        this.entidadRelacionada = entidadRelacionada;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }
    
    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
    
    // Métodos de negocio
    public void registrarRegistro() {
        this.fechaRegistro = new Date();
        this.estado = ESTADO_ACTIVO;
        System.out.println("Registro general creado: " + tipoRegistro + " - " + descripcion);
    }
    
    public void actualizarRegistro() {
        System.out.println("Registro general actualizado: " + idRegistro);
    }
    
    public void eliminarRegistro() {
        this.estado = ESTADO_INACTIVO;
        System.out.println("Registro general eliminado (inactivado): " + idRegistro);
    }
    
    public void completarRegistro() {
        this.estado = ESTADO_COMPLETADO;
        System.out.println("Registro general completado: " + idRegistro);
    }
    
    // Método para obtener el nombre legible del tipo de registro
    public String getTipoRegistroNombre() {
        switch (tipoRegistro) {
            case TIPO_CLIENTE:
                return "Cliente";
            case TIPO_EMPLEADO:
                return "Empleado";
            case TIPO_FACTURA:
                return "Factura";
            case TIPO_DOCUMENTO:
                return "Documento";
            case TIPO_VIATICO:
                return "Viático";
            case TIPO_NOMINA:
                return "Nómina";
            case TIPO_REPORTE:
                return "Reporte Contable";
            case TIPO_PRODUCTO:
                return "Producto";
            case TIPO_USUARIO:
                return "Usuario";
            default:
                return "Desconocido";
        }
    }
    
    @Override
    public String toString() {
        return "RegistroGeneral{" +
                "idRegistro=" + idRegistro +
                ", tipoRegistro='" + tipoRegistro + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", descripcion='" + descripcion + '\'' +
                ", idRelacionado=" + idRelacionado +
                ", entidadRelacionada='" + entidadRelacionada + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}