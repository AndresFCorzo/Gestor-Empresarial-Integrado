package main.java.com.gestorempresarial.web.servlets;

import main.java.com.gestorempresarial.dao.*;
import main.java.com.gestorempresarial.modelo.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FacturaServlet extends HttpServlet {
    private FacturaDAO facturaDAO = new FacturaDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/facturas": listar(request, response); break;
            case "/facturas/emitir": emitirForm(request, response); break;
            case "/facturas/ver": ver(request, response); break;
            case "/facturas/anular": anular(request, response); break;
            case "/facturas/eliminar-producto": eliminarProducto(request, response); break;
            default: listar(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/facturas/emitir".equals(path)) emitir(request, response);
        else if ("/facturas/agregar-producto".equals(path)) agregarProducto(request, response);
        else listar(request, response);
    }
    
    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("facturas", facturaDAO.listarTodas());
        request.getRequestDispatcher("/factura/listar.jsp").forward(request, response);
    }
    
    private void emitirForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Factura facturaTemp = (Factura) session.getAttribute("facturaTemp");
        if (facturaTemp == null) facturaTemp = new Factura();
        session.setAttribute("facturaTemp", facturaTemp);
        request.setAttribute("clientes", clienteDAO.listarTodos());
        request.setAttribute("productos", productoDAO.listarTodos());
        request.setAttribute("facturaTemp", facturaTemp);
        request.getRequestDispatcher("/factura/emitir.jsp").forward(request, response);
    }
    
    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Factura facturaTemp = (Factura) session.getAttribute("facturaTemp");
        if (facturaTemp == null) facturaTemp = new Factura();
        int idProducto = Integer.parseInt(request.getParameter("idProducto"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        Producto producto = productoDAO.buscarPorId(idProducto);
        if (producto != null && producto.getStock() >= cantidad) {
            DetalleFactura detalle = new DetalleFactura(producto, cantidad);
            facturaTemp.agregarDetalle(detalle);
            session.setAttribute("facturaTemp", facturaTemp);
        }
        response.sendRedirect(request.getContextPath() + "/facturas/emitir");
    }
    
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Factura facturaTemp = (Factura) session.getAttribute("facturaTemp");
        if (facturaTemp != null) {
            int index = Integer.parseInt(request.getParameter("index"));
            if (index >= 0 && index < facturaTemp.getDetalles().size()) {
                facturaTemp.getDetalles().remove(index);
                facturaTemp.calcularTotales();
                session.setAttribute("facturaTemp", facturaTemp);
            }
        }
        response.sendRedirect(request.getContextPath() + "/facturas/emitir");
    }
    
    private void emitir(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Factura facturaTemp = (Factura) session.getAttribute("facturaTemp");
        if (facturaTemp == null || facturaTemp.getDetalles().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/facturas/emitir?error=No hay productos");
            return;
        }
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        String numeroFactura = request.getParameter("numeroFactura");
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        facturaTemp.setCliente(cliente);
        facturaTemp.setNumeroFactura(numeroFactura);
        facturaTemp.emitir();
        if (facturaDAO.insertar(facturaTemp)) {
            for (DetalleFactura d : facturaTemp.getDetalles()) {
                Producto p = d.getProducto();
                productoDAO.actualizarStock(p.getIdProducto(), p.getStock() - d.getCantidad());
            }
            session.removeAttribute("facturaTemp");
            response.sendRedirect(request.getContextPath() + "/facturas?success=Factura emitida");
        } else {
            response.sendRedirect(request.getContextPath() + "/facturas/emitir?error=Error al emitir");
        }
    }
    
    private void ver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("factura", facturaDAO.buscarPorId(id));
        request.getRequestDispatcher("/factura/ver.jsp").forward(request, response);
    }
    
    private void anular(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        facturaDAO.anular(id);
        response.sendRedirect(request.getContextPath() + "/facturas?success=Factura anulada");
    }
}