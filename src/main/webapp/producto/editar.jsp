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
    <title>Editar Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <script>
        function toggleIva() {
            var aplicaIva = document.getElementById("aplicaIva").checked;
            var divPorcentajeIva = document.getElementById("divPorcentajeIva");
            if (aplicaIva) {
                divPorcentajeIva.style.display = "block";
            } else {
                divPorcentajeIva.style.display = "none";
                document.getElementById("porcentajeIva").value = 0;
            }
        }
    </script>
</head>
<body onload="toggleIva()">
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Editar Producto</span>
            <a href="${pageContext.request.contextPath}/productos" class="btn btn-outline-light">
                <i class="bi bi-arrow-left"></i> Volver
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-warning">
                        <h4><i class="bi bi-pencil-square"></i> Editar Producto</h4>
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
                        
                        <c:if test="${empty producto}">
                            <div class="alert alert-danger">Producto no encontrado</div>
                            <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">Volver</a>
                        </c:if>
                        
                        <c:if test="${not empty producto}">
                            <form action="${pageContext.request.contextPath}/productos/editar" method="post">
                                <input type="hidden" name="id" value="${producto.idProducto}">
                                
                                <div class="mb-3">
                                    <label for="nombre" class="form-label">Nombre *</label>
                                    <input type="text" class="form-control" id="nombre" name="nombre" 
                                           value="${producto.nombre}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="codigo" class="form-label">Código *</label>
                                    <input type="text" class="form-control" id="codigo" name="codigo" 
                                           value="${producto.codigo}" required>
                                </div>
                                <div class="mb-3">
                                    <label for="precio" class="form-label">Precio *</label>
                                    <input type="number" step="0.01" class="form-control" id="precio" name="precio" 
                                           value="${producto.precio}" required>
                                </div>
                                <div class="mb-3">
                                    <div class="form-check">
                                        <input type="checkbox" class="form-check-input" id="aplicaIva" name="aplicaIva" 
                                               value="true" ${producto.aplicaIva ? 'checked' : ''} onclick="toggleIva()">
                                        <label class="form-check-label" for="aplicaIva">Aplica IVA</label>
                                    </div>
                                </div>
                                <div class="mb-3" id="divPorcentajeIva" style="${producto.aplicaIva ? 'display:block' : 'display:none'}">
                                    <label for="porcentajeIva" class="form-label">Porcentaje IVA (%)</label>
                                    <input type="number" step="0.01" class="form-control" id="porcentajeIva" name="porcentajeIva" 
                                           value="${producto.porcentajeIva}">
                                </div>
                                <div class="mb-3">
                                    <label for="stock" class="form-label">Stock</label>
                                    <input type="number" class="form-control" id="stock" name="stock" 
                                           value="${producto.stock}">
                                </div>
                                <div class="mb-3">
                                    <label for="categoria" class="form-label">Categoría</label>
                                    <select class="form-select" id="categoria" name="categoria">
                                        <option value="">Seleccione una categoría...</option>
                                        <option value="Electrónica" ${producto.categoria == 'Electrónica' ? 'selected' : ''}>Electrónica</option>
                                        <option value="Accesorios" ${producto.categoria == 'Accesorios' ? 'selected' : ''}>Accesorios</option>
                                        <option value="Mobiliario" ${producto.categoria == 'Mobiliario' ? 'selected' : ''}>Mobiliario</option>
                                        <option value="Servicios" ${producto.categoria == 'Servicios' ? 'selected' : ''}>Servicios</option>
                                        <option value="Otros" ${producto.categoria == 'Otros' ? 'selected' : ''}>Otros</option>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> Actualizar
                                </button>
                                <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">
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