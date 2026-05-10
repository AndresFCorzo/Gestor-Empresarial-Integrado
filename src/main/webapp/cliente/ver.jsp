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
    <title>Ver Cliente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Detalle Cliente</span>
            <a href="${pageContext.request.contextPath}/clientes" class="btn btn-outline-light">
                <i class="bi bi-arrow-left"></i> Volver
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h4><i class="bi bi-person-badge"></i> Información del Cliente</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty cliente}">
                            <div class="alert alert-danger">Cliente no encontrado</div>
                            <a href="${pageContext.request.contextPath}/clientes" class="btn btn-secondary">Volver</a>
                        </c:if>
                        
                        <c:if test="${not empty cliente}">
                            <table class="table table-bordered">
                                <tr>
                                    <th style="width: 30%; background-color: #f5f5f5;">ID</th>
                                    <td>${cliente.idCliente}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Nombre</th>
                                    <td>${cliente.nombre}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">NIT</th>
                                    <td>${cliente.nit}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Dirección</th>
                                    <td>${cliente.direccion != null ? cliente.direccion : 'No registrada'}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Teléfono</th>
                                    <td>${cliente.telefono != null ? cliente.telefono : 'No registrado'}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Correo Electrónico</th>
                                    <td>${cliente.correo != null ? cliente.correo : 'No registrado'}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Fecha de Registro</th>
                                    <td><fmt:formatDate value="${cliente.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                </tr>
                            </table>
                            
                            <div class="mt-3 text-end">
                                <a href="${pageContext.request.contextPath}/clientes/editar?id=${cliente.idCliente}" class="btn btn-warning">
                                    <i class="bi bi-pencil"></i> Editar
                                </a>
                                <a href="${pageContext.request.contextPath}/clientes/eliminar?id=${cliente.idCliente}" class="btn btn-danger" onclick="return confirm('¿Eliminar este cliente?')">
                                    <i class="bi bi-trash"></i> Eliminar
                                </a>
                                <a href="${pageContext.request.contextPath}/facturas?cliente=${cliente.idCliente}" class="btn btn-success">
                                    <i class="bi bi-receipt"></i> Ver Facturas
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>