package main.java.com.gestorempresarial.web.servlets;

import main.java.com.gestorempresarial.dao.ClienteDAO;
import main.java.com.gestorempresarial.modelo.Cliente;
import main.java.com.gestorempresarial.utils.Validaciones;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClienteServlet extends HttpServlet {
    private ClienteDAO clienteDAO = new ClienteDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        switch (path) {
            case "/clientes": listar(request, response); break;
            case "/clientes/registrar": request.getRequestDispatcher("/cliente/registrar.jsp").forward(request, response); break;
            case "/clientes/editar": editarForm(request, response); break;
            case "/clientes/eliminar": eliminar(request, response); break;
            case "/clientes/ver": ver(request, response); break;
            default: listar(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/clientes/registrar".equals(path)) registrar(request, response);
        else if ("/clientes/editar".equals(path)) actualizar(request, response);
        else listar(request, response);
    }
    
    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("clientes", clienteDAO.listarTodos());
        request.getRequestDispatcher("/cliente/listar.jsp").forward(request, response);
    }
    
    private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String nit = request.getParameter("nit");
        String direccion = request.getParameter("direccion");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        String error = Validaciones.validarCliente(nombre, nit);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/cliente/registrar.jsp").forward(request, response);
            return;
        }
        Cliente cliente = new Cliente(nombre, nit, direccion, correo, telefono);
        if (clienteDAO.insertar(cliente)) response.sendRedirect(request.getContextPath() + "/clientes?success=Registrado");
        else { request.setAttribute("error", "Error al registrar"); request.getRequestDispatcher("/cliente/registrar.jsp").forward(request, response); }
    }
    
    private void editarForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cliente cliente = clienteDAO.buscarPorId(id);
        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("/cliente/editar.jsp").forward(request, response);
    }
    
    private void actualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String nit = request.getParameter("nit");
        String direccion = request.getParameter("direccion");
        String correo = request.getParameter("correo");
        String telefono = request.getParameter("telefono");
        Cliente cliente = new Cliente(nombre, nit, direccion, correo, telefono);
        cliente.setIdCliente(id);
        if (clienteDAO.actualizar(cliente)) response.sendRedirect(request.getContextPath() + "/clientes?success=Actualizado");
        else { request.setAttribute("error", "Error al actualizar"); editarForm(request, response); }
    }
    
    private void eliminar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        clienteDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/clientes?success=Eliminado");
    }
    
    private void ver(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("cliente", clienteDAO.buscarPorId(id));
        request.getRequestDispatcher("/cliente/ver.jsp").forward(request, response);
    }
}