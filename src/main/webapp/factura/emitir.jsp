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
    <title>Emitir Factura</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Emitir Factura</span>
            <a href="${pageContext.request.contextPath}/facturas" class="btn btn-outline-light">
                <i class="bi bi-arrow-left"></i> Volver
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <!-- Mensajes de alerta -->
        <%
            String error = request.getParameter("error");
            String success = request.getParameter("success");
            if (error != null) {
        %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle"></i> <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
            }
            if (success != null) {
        %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle"></i> <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <%
            }
        %>
        
        <div class="row">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5><i class="bi bi-cart-plus"></i> Agregar Producto</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/facturas/agregar-producto" method="post">
                            <div class="mb-3">
                                <label for="idProducto" class="form-label">Producto</label>
                                <select class="form-select" id="idProducto" name="idProducto" required>
                                    <option value="">Seleccione un producto...</option>
                                    <c:forEach var="producto" items="${productos}">
                                        <option value="${producto.idProducto}">
                                            ${producto.nombre} - $<fmt:formatNumber value="${producto.precio}"/> (Stock: ${producto.stock})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label for="cantidad" class="form-label">Cantidad</label>
                                <input type="number" class="form-control" id="cantidad" name="cantidad" min="1" value="1" required>
                            </div>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-plus-circle"></i> Agregar Producto
                            </button>
                        </form>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5><i class="bi bi-receipt"></i> Factura Actual</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty facturaTemp.detalles}">
                                <p class="text-muted">No hay productos agregados</p>
                            </c:when>
                            <c:otherwise>
                                <table class="table table-sm">
                                    <thead>
                                        <tr>
                                            <th>Producto</th>
                                            <th>Cant.</th>
                                            <th>Precio</th>
                                            <th>Total</th>
                                            <th></th>
                                         </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="detalle" items="${facturaTemp.detalles}" varStatus="status">
                                            <tr>
                                                <td>${detalle.producto.nombre}</td>
                                                <td>${detalle.cantidad}</td>
                                                <td>$<fmt:formatNumber value="${detalle.precioUnitario}"/></td>
                                                <td>$<fmt:formatNumber value="${detalle.total}"/></td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/facturas/eliminar-producto?index=${status.index}" class="btn btn-sm btn-danger" onclick="return confirm('¿Eliminar este producto?')">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="table-primary">
                                            <th colspan="3">SUBTOTAL</th>
                                            <th>$<fmt:formatNumber value="${facturaTemp.subtotal}"/></th>
                                            <th></th>
                                        </tr>
                                        <tr class="table-info">
                                            <th colspan="3">IVA</th>
                                            <th>$<fmt:formatNumber value="${facturaTemp.totalIva}"/></th>
                                            <th></th>
                                        </tr>
                                        <tr class="table-success">
                                            <th colspan="3">TOTAL</th>
                                            <th>$<fmt:formatNumber value="${facturaTemp.total}"/></th>
                                            <th></th>
                                        </tr>
                                    </tfoot>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-warning">
                        <h5><i class="bi bi-check-circle"></i> Finalizar Factura</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/facturas/emitir" method="post">
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="idCliente" class="form-label">Cliente *</label>
                                    <select class="form-select" id="idCliente" name="idCliente" required ${empty facturaTemp.detalles ? 'disabled' : ''}>
                                        <option value="">Seleccione un cliente...</option>
                                        <c:forEach var="cliente" items="${clientes}">
                                            <option value="${cliente.idCliente}">${cliente.nombre} - ${cliente.nit}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-6">
                                    <label for="numeroFactura" class="form-label">Número de Factura *</label>
                                    <input type="text" class="form-control" id="numeroFactura" name="numeroFactura" required ${empty facturaTemp.detalles ? 'disabled' : ''}>
                                </div>
                            </div>
                            <div class="mt-3">
                                <button type="submit" class="btn btn-success" ${empty facturaTemp.detalles ? 'disabled' : ''}>
                                    <i class="bi bi-check-lg"></i> Emitir Factura
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>