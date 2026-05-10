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
    <title>Ver Factura</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <style>
        @media print {
            .no-print {
                display: none;
            }
        }
        .factura-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .factura-header {
            border-bottom: 2px solid #333;
            margin-bottom: 20px;
            padding-bottom: 20px;
        }
        .factura-footer {
            border-top: 2px solid #333;
            margin-top: 20px;
            padding-top: 20px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-dark bg-dark no-print">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Detalle Factura</span>
            <div>
                <button onclick="window.print()" class="btn btn-info btn-sm me-2">
                    <i class="bi bi-printer"></i> Imprimir
                </button>
                <a href="${pageContext.request.contextPath}/facturas" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-arrow-left"></i> Volver
                </a>
            </div>
        </div>
    </nav>
    
    <div class="container mt-4 mb-4">
        <c:if test="${empty factura}">
            <div class="alert alert-danger">Factura no encontrada</div>
            <a href="${pageContext.request.contextPath}/facturas" class="btn btn-secondary">Volver</a>
        </c:if>
        
        <c:if test="${not empty factura}">
            <div class="factura-container">
                <!-- Encabezado de la factura -->
                <div class="factura-header text-center">
                    <h2>🏢 GESTOR EMPRESARIAL INTEGRADO</h2>
                    <h4>FACTURA DE VENTA</h4>
                    <p>NIT: 900123456-1 | Tel: (601) 123-4567</p>
                    <p>Dirección: Calle 123 #45-67, Bogotá</p>
                </div>
                
                <!-- Información de la factura -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <strong>FACTURA N°:</strong> ${factura.numeroFactura}<br>
                        <strong>FECHA:</strong> <fmt:formatDate value="${factura.fecha}" pattern="dd/MM/yyyy HH:mm"/><br>
                        <strong>ESTADO:</strong> 
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
                    </div>
                    <div class="col-md-6">
                        <strong>CLIENTE:</strong> ${factura.cliente.nombre}<br>
                        <strong>NIT:</strong> ${factura.cliente.nit}<br>
                        <strong>DIRECCIÓN:</strong> ${factura.cliente.direccion != null ? factura.cliente.direccion : 'No registrada'}<br>
                        <strong>TELÉFONO:</strong> ${factura.cliente.telefono != null ? factura.cliente.telefono : 'No registrado'}
                    </div>
                </div>
                
                <!-- Detalle de productos -->
                <table class="table table-bordered">
                    <thead class="table-dark">
                        <tr class="text-center">
                            <th>Cantidad</th>
                            <th>Descripción</th>
                            <th>Valor Unitario</th>
                            <th>Subtotal</th>
                            <th>IVA</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detalle" items="${factura.detalles}">
                            <tr>
                                <td class="text-center">${detalle.cantidad}</td>
                                <td>${detalle.producto.nombre}</td>
                                <td class="text-end">$<fmt:formatNumber value="${detalle.precioUnitario}"/></td>
                                <td class="text-end">$<fmt:formatNumber value="${detalle.subtotal}"/></td>
                                <td class="text-end">$<fmt:formatNumber value="${detalle.valorIva}"/></td>
                                <td class="text-end">$<fmt:formatNumber value="${detalle.total}"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr class="table-primary">
                            <th colspan="3" class="text-end">SUBTOTAL</th>
                            <th class="text-end">$<fmt:formatNumber value="${factura.subtotal}"/></th>
                            <th class="text-end"></th>
                            <th class="text-end"></th>
                        </tr>
                        <tr class="table-info">
                            <th colspan="3" class="text-end">IVA (19%)</th>
                            <th class="text-end"></th>
                            <th class="text-end">$<fmt:formatNumber value="${factura.totalIva}"/></th>
                            <th class="text-end"></th>
                        </tr>
                        <tr class="table-success">
                            <th colspan="3" class="text-end">TOTAL</th>
                            <th class="text-end"></th>
                            <th class="text-end"></th>
                            <th class="text-end">$<fmt:formatNumber value="${factura.total}"/></th>
                        </tr>
                    </tfoot>
                </table>
                
                <!-- Totales en letras -->
                <div class="factura-footer">
                    <p><strong>SON:</strong> <fmt:formatNumber value="${factura.total}" type="currency" currencySymbol="Pesos"/> M/CTE</p>
                    <p><strong>OBSERVACIONES:</strong> Esta factura es un comprobante de venta válido para efectos contables.</p>
                    <p class="text-center mt-4">¡Gracias por su compra!</p>
                </div>
            </div>
            
            <!-- Botones de acción -->
            <div class="text-center mt-4 no-print">
                <c:if test="${factura.estado != 'ANULADA'}">
                    <a href="${pageContext.request.contextPath}/facturas/anular?id=${factura.idFactura}" 
                       class="btn btn-danger" onclick="return confirm('¿Anular esta factura?')">
                        <i class="bi bi-x-circle"></i> Anular Factura
                    </a>
                </c:if>
                <button onclick="window.print()" class="btn btn-info">
                    <i class="bi bi-printer"></i> Imprimir
                </button>
            </div>
        </c:if>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>