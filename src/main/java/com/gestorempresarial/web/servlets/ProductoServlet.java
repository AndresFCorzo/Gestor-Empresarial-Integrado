package main.java.com.gestorempresarial.web.servlets;

import main.java.com.gestorempresarial.dao.ProductoDAO;
import main.java.com.gestorempresarial.modelo.Producto;
import main.java.com.gestorempresarial.utils.Validaciones;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductoServlet extends HttpServlet {
    private ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/productos": listar(request, response); break;
            case "/productos/registrar": request.getRequestDispatcher("/producto/registrar.jsp").forward(request, response); break;
            case "/productos/editar": editarForm(request, response); break;
            case "/productos/eliminar": eliminar(request, response); break;
            case "/productos/ver": ver(request, response); break;
            default: listar(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/productos/registrar".equals(path)) registrar(request, response);
        else if ("/productos/editar".equals(path)) actualizar(request, response);
        else listar(request, response);
    }
    
    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("productos", productoDAO.listarTodos());
        request.getRequestDispatcher("/producto/listar.jsp").forward(request, response);
    }
    
    private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String codigo = request.getParameter("codigo");
        double precio = Double.parseDouble(request.getParameter("precio"));
        boolean aplicaIva = Boolean.parseBoolean(request.getParameter("aplicaIva"));
        double porcentajeIva = Double.parseDouble(request.getParameter("porcentajeIva"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        String categoria = request.getParameter("categoria");
        String error = Validaciones.validarProducto(nombre, codigo, precio);
        if (error != null) { request.setAttribute("error", error); request.getRequestDispatcher("/producto/registrar.jsp").forward(request, response); return; }
        Producto producto = new Producto(nombre, codigo, precio, aplicaIva, porcentajeIva, stock, categoria);
        if (productoDAO.insertar(producto)) response.sendRedirect(request.getContextPath() + "/productos?success=Registrado");
        else { request.setAttribute("error", "Error al registrar"); request.getRequestDispatcher("/producto/registrar.jsp").forward(request, response); }
    }
    
    private void editarForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("producto", productoDAO.buscarPorId(id));
        request.getRequestDispatcher("/producto/editar.jsp").forward(request, response);
    }
    
    private void actualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String codigo = request.getParameter("codigo");
        double precio = Double.parseDouble(request.getParameter("precio"));
        boolean aplicaIva = Boolean.parseBoolean(request.getParameter("aplicaIva"));
        double porcentajeIva = Double.parseDouble(request.getParameter("porcentajeIva"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        String categoria = request.getParameter("categoria");
        Producto producto = new Producto(nombre, codigo, precio, aplicaIva, porcentajeIva, stock, categoria);
        producto.setIdProducto(id);
        if (productoDAO.actualizar(producto)) response.sendRedirect(request.getContextPath() + "/productos?success=Actualizado");
        else { request.setAttribute("error", "Error al actualizar"); editarForm(request, response); }
    }
    
    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productoDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/productos?success=Eliminado");
    }
    
    private void ver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("producto", productoDAO.buscarPorId(id));
        request.getRequestDispatcher("/producto/ver.jsp").forward(request, response);
    }
}