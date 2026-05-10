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
    <title>Ver Producto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Detalle Producto</span>
            <a href="${pageContext.request.contextPath}/productos" class="btn btn-outline-light">
                <i class="bi bi-arrow-left"></i> Volver
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h4><i class="bi bi-box-seam"></i> Información del Producto</h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty producto}">
                            <div class="alert alert-danger">Producto no encontrado</div>
                            <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">Volver</a>
                        </c:if>
                        
                        <c:if test="${not empty producto}">
                            <!-- Imagen o ícono del producto -->
                            <div class="text-center mb-4">
                                <div class="display-1">
                                    <i class="bi bi-box"></i>
                                </div>
                            </div>
                            
                            <table class="table table-bordered">
                                <tr>
                                    <th style="width: 30%; background-color: #f5f5f5;">ID</th>
                                    <td>${producto.idProducto}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Código</th>
                                    <td><span class="badge bg-secondary">${producto.codigo}</span></td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Nombre</th>
                                    <td>${producto.nombre}</td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Categoría</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty producto.categoria}">
                                                <span class="text-muted">Sin categoría</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-primary">${producto.categoria}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Precio Unitario</th>
                                    <td>
                                        <h4 class="text-success">
                                            $<fmt:formatNumber value="${producto.precio}" type="number" groupingUsed="true"/>
                                        </h4>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Precio con IVA</th>
                                    <td>
                                        <strong>
                                            $<fmt:formatNumber value="${producto.calcularPrecioConIva()}" type="number" groupingUsed="true"/>
                                        </strong>
                                        <c:if test="${producto.aplicaIva}">
                                            <span class="badge bg-info">IVA ${producto.porcentajeIva}% incluido</span>
                                        </c:if>
                                        <c:if test="${!producto.aplicaIva}">
                                            <span class="badge bg-secondary">Exento de IVA</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Stock Actual</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${producto.stock <= 0}">
                                                <span class="badge bg-danger">Agotado</span>
                                            </c:when>
                                            <c:when test="${producto.stock <= 5}">
                                                <span class="badge bg-warning">Stock bajo: ${producto.stock} unidades</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success">${producto.stock} unidades</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="background-color: #f5f5f5;">Estado</th>
                                    <td>
                                        <c:choose>
                                            <c:when test="${producto.stock > 0}">
                                                <span class="badge bg-success">Disponible</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">No disponible</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                            
                            <!-- Resumen de precios -->
                            <div class="card mt-3 bg-light">
                                <div class="card-body">
                                    <h6 class="card-title"><i class="bi bi-calculator"></i> Resumen de Precios</h6>
                                    <table class="table table-sm table-borderless">
                                        <tr>
                                            <td>Precio Base:</td>
                                            <td class="text-end">$<fmt:formatNumber value="${producto.precio}"/></td>
                                        </tr>
                                        <c:if test="${producto.aplicaIva}">
                                            <tr>
                                                <td>IVA (${producto.porcentajeIva}%):</td>
                                                <td class="text-end">$<fmt:formatNumber value="${producto.precio * producto.porcentajeIva / 100}"/></td>
                                            </tr>
                                        </c:if>
                                        <tr class="table-primary">
                                            <td><strong>Precio Total:</strong></td>
                                            <td class="text-end"><strong>$<fmt:formatNumber value="${producto.calcularPrecioConIva()}"/></strong></td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            
                            <div class="mt-3 text-end">
                                <a href="${pageContext.request.contextPath}/productos/editar?id=${producto.idProducto}" class="btn btn-warning">
                                    <i class="bi bi-pencil"></i> Editar
                                </a>
                                <a href="${pageContext.request.contextPath}/productos/eliminar?id=${producto.idProducto}" class="btn btn-danger" onclick="return confirm('¿Eliminar este producto?')">
                                    <i class="bi bi-trash"></i> Eliminar
                                </a>
                                <c:if test="${producto.stock > 0}">
                                    <a href="${pageContext.request.contextPath}/facturas/emitir?producto=${producto.idProducto}" class="btn btn-success">
                                        <i class="bi bi-cart-plus"></i> Agregar a Factura
                                    </a>
                                </c:if>
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