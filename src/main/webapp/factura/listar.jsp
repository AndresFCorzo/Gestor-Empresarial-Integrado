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
    <title>Lista de Facturas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Facturas</span>
            <div>
                <a href="${pageContext.request.contextPath}/facturas/emitir" class="btn btn-success btn-sm me-2">
                    <i class="bi bi-plus-circle"></i> Nueva Factura
                </a>
                <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-house"></i> Inicio
                </a>
            </div>
        </div>
    </nav>
    
    <div class="container mt-4">
        <h2><i class="bi bi-receipt"></i> Lista de Facturas</h2>
        
        <%
            String success = request.getParameter("success");
            if (success != null) {
        %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
            }
        %>
        
        <div class="card">
            <div class="card-body">
                <table class="table table-striped table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>N° Factura</th>
                            <th>Fecha</th>
                            <th>Cliente</th>
                            <th>Total</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                         </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="factura" items="${facturas}">
                            <tr>
                                <td>${factura.idFactura}</td>
                                <td>${factura.numeroFactura}</td>
                                <td><fmt:formatDate value="${factura.fecha}" pattern="dd/MM/yyyy"/></td>
                                <td>${factura.cliente.nombre}</td>
                                <td>$<fmt:formatNumber value="${factura.total}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${factura.estado == 'EMITIDA'}">
                                            <span class="badge bg-success">EMITIDA</span>
                                        </c:when>
                                        <c:when test="${factura.estado == 'ANULADA'}">
                                            <span class="badge bg-danger">ANULADA</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning">PENDIENTE</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/facturas/ver?id=${factura.idFactura}" class="btn btn-sm btn-info">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <c:if test="${factura.estado != 'ANULADA'}">
                                        <a href="${pageContext.request.contextPath}/facturas/anular?id=${factura.idFactura}" class="btn btn-sm btn-danger" onclick="return confirm('¿Anular esta factura?')">
                                            <i class="bi bi-x-circle"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>