package main.java.com.gestorempresarial.web.servlets;

import main.java.com.gestorempresarial.dao.FacturaDAO;
import main.java.com.gestorempresarial.modelo.Factura;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReporteServlet extends HttpServlet {
    private FacturaDAO facturaDAO = new FacturaDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Factura> facturas = facturaDAO.listarTodas();
        double totalGeneral = 0;
        for (Factura f : facturas) totalGeneral += f.getTotal();
        request.setAttribute("facturas", facturas);
        request.setAttribute("totalGeneral", totalGeneral);
        request.getRequestDispatcher("/reporte/ventas.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}