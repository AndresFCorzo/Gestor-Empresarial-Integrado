<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("usuario") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Reporte de Ventas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Reporte de Ventas</span>
            <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-outline-light btn-sm">
                <i class="bi bi-house"></i> Inicio
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <h2><i class="bi bi-graph-up"></i> Reporte de Ventas</h2>
        
        <div class="card">
            <div class="card-body">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>Factura</th>
                            <th>Fecha</th>
                            <th>Cliente</th>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Valor Unitario</th>
                            <th>Total</th>
                         </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="factura" items="${facturas}">
                            <c:forEach var="detalle" items="${factura.detalles}">
                                <tr>
                                    <td>${factura.numeroFactura}</td>
                                    <td><fmt:formatDate value="${factura.fecha}" pattern="dd/MM/yyyy"/></td>
                                    <td>${factura.cliente.nombre}</td>
                                    <td>${detalle.producto.nombre}</td>
                                    <td class="text-center">${detalle.cantidad}</td>
                                    <td>$<fmt:formatNumber value="${detalle.precioUnitario}"/></td>
                                    <td>$<fmt:formatNumber value="${detalle.total}"/></td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr class="table-success">
                            <th colspan="6" class="text-end">TOTAL GENERAL:</th>
                            <th>$<fmt:formatNumber value="${totalGeneral}"/></th>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
        
        <div class="mt-3">
            <button onclick="window.print()" class="btn btn-primary">
                <i class="bi bi-printer"></i> Imprimir
            </button>
            <button onclick="exportarExcel()" class="btn btn-success">
                <i class="bi bi-file-excel"></i> Exportar a Excel
            </button>
        </div>
    </div>
    
    <script>
        function exportarExcel() {
            window.location.href = "${pageContext.request.contextPath}/reportes/ventas?exportar=excel";
        }
    </script>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>