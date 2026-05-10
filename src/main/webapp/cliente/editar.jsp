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
    <title>Editar Cliente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Editar Cliente</span>
            <a href="${pageContext.request.contextPath}/clientes" class="btn btn-outline-light">
                <i class="bi bi-arrow-left"></i> Volver
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-warning">
                        <h4><i class="bi bi-pencil-square"></i> Editar Cliente</h4>
                    </div>
                    <div class="card-body">
                        <%
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                            <div class="alert alert-danger"><%= error %></div>
                        <%
                            }
                        %>
                        
                        <c:if test="${empty cliente}">
                            <div class="alert alert-danger">Cliente no encontrado</div>
                            <a href="${pageContext.request.contextPath}/clientes" class="btn btn-secondary">Volver</a>
                        </c:if>
                        
                        <c:if test="${not empty cliente}">
                            <form action="${pageContext.request.contextPath}/clientes/editar" method="post">
                                <input type="hidden" name="id" value="${cliente.idCliente}">
                                
                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre *</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" 
                                           value="${cliente.nombre}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="nit" class="form-label">NIT *</label>
                                    <input type="text" class="form-control" id="nit" name="nit" 
                                           value="${cliente.nit}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="direccion" class="form-label">Dirección</label>
                                    <input type="text" class="form-control" id="direccion" name="direccion" 
                                           value="${cliente.direccion}">
                                </div>
                                <div class="mb-3">
                                    <label for="telefono" class="form-label">Teléfono</label>
                                    <input type="tel" class="form-control" id="telefono" name="telefono" 
                                           value="${cliente.telefono}">
                                </div>
                                <div class="mb-3">
                                    <label for="correo" class="form-label">Correo Electrónico</label>
                                    <input type="email" class="form-control" id="correo" name="correo" 
                                           value="${cliente.correo}">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Fecha de Registro</label>
                                    <input type="text" class="form-control" 
                                           value="<fmt:formatDate value='${cliente.fechaRegistro}' pattern='dd/MM/yyyy'/>" 
                                           disabled>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> Actualizar
                                </button>
                                <a href="${pageContext.request.contextPath}/clientes" class="btn btn-secondary">
                                    <i class="bi bi-x-circle"></i> Cancelar
                                </a>
                            </form>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>