package com.gestorempresarial.console.controlador;

import com.gestorempresarial.dao.ProductoDAO;
import com.gestorempresarial.modelo.Producto;
import com.gestorempresarial.utils.Validaciones;
import java.util.List;

/**
 * Controlador para la gestión de productos (Versión Consola)
 * @author Andres Felipe Corzo Angarita
 */
public class ProductoControlador {
    
    private ProductoDAO productoDAO;
    
    public ProductoControlador() {
        this.productoDAO = new ProductoDAO();
    }
    
    /**
     * Registrar un nuevo producto
     * @param nombre Nombre del producto
     * @param codigo Código del producto
     * @param precio Precio
     * @param aplicaIva Si aplica IVA
     * @param porcentajeIva Porcentaje de IVA
     * @param stock Stock inicial
     * @param categoria Categoría
     * @return Mensaje de resultado
     */
    public String registrarProducto(String nombre, String codigo, double precio, boolean aplicaIva, 
                                    double porcentajeIva, int stock, String categoria) {
        // Validar datos
        String error = Validaciones.validarProducto(nombre, codigo, precio);
        if (error != null) {
            return "❌ " + error;
        }
        
        // Verificar si ya existe un producto con el mismo código
        Producto existente = productoDAO.buscarPorCodigo(codigo);
        if (existente != null) {
            return "❌ Ya existe un producto con el código: " + codigo;
        }
        
        Producto producto = new Producto(nombre, codigo, precio, aplicaIva, porcentajeIva, stock, categoria);
        
        if (productoDAO.insertar(producto)) {
            return "✅ Producto registrado exitosamente con ID: " + producto.getIdProducto();
        } else {
            return "❌ Error al registrar el producto";
        }
    }
    
    /**
     * Listar todos los productos
     * @return Lista de productos
     */
    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }
    
    /**
     * Buscar producto por ID
     * @param id ID del producto
     * @return Producto encontrado o null
     */
    public Producto buscarProductoPorId(int id) {
        if (id <= 0) {
            System.out.println("❌ ID inválido");
            return null;
        }
        return productoDAO.buscarPorId(id);
    }
    
    /**
     * Buscar producto por código
     * @param codigo Código del producto
     * @return Producto encontrado o null
     */
    public Producto buscarProductoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            System.out.println("❌ Código inválido");
            return null;
        }
        return productoDAO.buscarPorCodigo(codigo);
    }
    
    /**
     * Actualizar un producto existente
     * @param id ID del producto
     * @param precio Nuevo precio
     * @param stock Nuevo stock
     * @return Mensaje de resultado
     */
    public String actualizarProducto(int id, Double precio, Integer stock) {
        Producto producto = productoDAO.buscarPorId(id);
        if (producto == null) {
            return "❌ Producto no encontrado";
        }
        
        if (precio != null && precio > 0) {
            producto.setPrecio(precio);
        }
        if (stock != null && stock >= 0) {
            producto.setStock(stock);
        }
        
        if (productoDAO.actualizar(producto)) {
            return "✅ Producto actualizado exitosamente";
        } else {
            return "❌ Error al actualizar producto";
        }
    }
    
    /**
     * Actualizar stock de un producto
     * @param id ID del producto
     * @param nuevoStock Nuevo stock
     * @return true si se actualizó correctamente
     */
    public boolean actualizarStock(int id, int nuevoStock) {
        return productoDAO.actualizarStock(id, nuevoStock);
    }
    
    /**
     * Eliminar un producto
     * @param id ID del producto
     * @return Mensaje de resultado
     */
    public String eliminarProducto(int id) {
        Producto producto = productoDAO.buscarPorId(id);
        if (producto == null) {
            return "❌ Producto no encontrado";
        }
        
        if (productoDAO.eliminar(id)) {
            return "✅ Producto eliminado exitosamente";
        } else {
            return "❌ Error al eliminar producto";
        }
    }
    
    /**
     * Verificar si hay suficiente stock
     * @param idProducto ID del producto
     * @param cantidad Cantidad deseada
     * @return true si hay stock suficiente
     */
    public boolean verificarStock(int idProducto, int cantidad) {
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto == null) {
            return false;
        }
        return producto.getStock() >= cantidad;
    }
    
    /**
     * Mostrar información detallada de un producto
     * @param producto Producto a mostrar
     */
    public void mostrarDetalleProducto(Producto producto) {
        if (producto == null) {
            System.out.println("❌ Producto no encontrado");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     DETALLE DEL PRODUCTO                      ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ %-20s: %-40s ║%n", "ID", producto.getIdProducto());
        System.out.printf("║ %-20s: %-40s ║%n", "Nombre", producto.getNombre());
        System.out.printf("║ %-20s: %-40s ║%n", "Código", producto.getCodigo());
        System.out.printf("║ %-20s: %-40s ║%n", "Categoría", producto.getCategoria() != null ? producto.getCategoria() : "Sin categoría");
        System.out.printf("║ %-20s: $%-39.2f ║%n", "Precio Base", producto.getPrecio());
        System.out.printf("║ %-20s: %-40s ║%n", "IVA", producto.isAplicaIva() ? producto.getPorcentajeIva() + "%" : "Exento");
        System.out.printf("║ %-20s: $%-39.2f ║%n", "Precio con IVA", producto.calcularPrecioConIva());
        System.out.printf("║ %-20s: %-40d ║%n", "Stock", producto.getStock());
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Mostrar lista de productos en formato tabla
     * @param productos Lista de productos
     */
    public void mostrarListaProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("📋 No hay productos registrados");
            return;
        }
        
        System.out.println("\n┌─────┬──────────┬──────────────────────────────────┬────────────┬────────┬─────────┐");
        System.out.println("│ ID  │ CÓDIGO   │ NOMBRE                           │ PRECIO     │ STOCK  │ IVA     │");
        System.out.println("├─────┼──────────┼──────────────────────────────────┼────────────┼────────┼─────────┤");
        for (Producto p : productos) {
            System.out.printf("│ %-3d │ %-8s │ %-32s │ $%-9.2f │ %-6d │ %-7s │%n",
                p.getIdProducto(),
                p.getCodigo(),
                p.getNombre().length() > 32 ? p.getNombre().substring(0, 29) + "..." : p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.isAplicaIva() ? p.getPorcentajeIva() + "%" : "Exento");
        }
        System.out.println("└─────┴──────────┴──────────────────────────────────┴────────────┴────────┴─────────┘");
    }
}