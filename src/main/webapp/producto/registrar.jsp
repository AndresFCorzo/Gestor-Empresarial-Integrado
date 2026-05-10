<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Registrar Producto</title>
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
        
        function validarFormulario() {
            var nombre = document.getElementById("nombre").value.trim();
            var codigo = document.getElementById("codigo").value.trim();
            var precio = document.getElementById("precio").value;
            
            if (nombre === "") {
                alert("El nombre del producto es obligatorio");
                return false;
            }
            if (codigo === "") {
                alert("El código del producto es obligatorio");
                return false;
            }
            if (precio === "" || parseFloat(precio) <= 0) {
                alert("El precio debe ser mayor a cero");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <nav class="navbar navbar-dark bg-dark">
        <div class="container-fluid">
            <span class="navbar-brand mb-0 h1">🏢 Gestor Empresarial - Nuevo Producto</span>
            <a href="${pageContext.request.contextPath}/productos" class="btn btn-outline-light">
                <i class="bi bi-arrow-left"></i> Volver
            </a>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h4><i class="bi bi-plus-circle"></i> Registrar Nuevo Producto</h4>
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
                        
                        <form action="${pageContext.request.contextPath}/productos/registrar" method="post" onsubmit="return validarFormulario()">
                            <div class="mb-3">
                                <label for="nombre" class="form-label">Nombre *</label>
                                <input type="text" class="form-control" id="nombre" name="nombre" required>
                            </div>
                            <div class="mb-3">
                                <label for="codigo" class="form-label">Código *</label>
                                <input type="text" class="form-control" id="codigo" name="codigo" placeholder="Ej: PROD-001" required>
                            </div>
                            <div class="mb-3">
                                <label for="precio" class="form-label">Precio *</label>
                                <div class="input-group">
                                    <span class="input-group-text">$</span>
                                    <input type="number" step="0.01" class="form-control" id="precio" name="precio" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <div class="form-check">
                                    <input type="checkbox" class="form-check-input" id="aplicaIva" name="aplicaIva" value="true" onclick="toggleIva()">
                                    <label class="form-check-label" for="aplicaIva">Aplica IVA</label>
                                </div>
                            </div>
                            <div class="mb-3" id="divPorcentajeIva" style="display:none">
                                <label for="porcentajeIva" class="form-label">Porcentaje IVA (%)</label>
                                <div class="input-group">
                                    <input type="number" step="0.01" class="form-control" id="porcentajeIva" name="porcentajeIva" value="19.0">
                                    <span class="input-group-text">%</span>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label for="stock" class="form-label">Stock</label>
                                <input type="number" class="form-control" id="stock" name="stock" value="0">
                            </div>
                            <div class="mb-3">
                                <label for="categoria" class="form-label">Categoría</label>
                                <select class="form-select" id="categoria" name="categoria">
                                    <option value="">Seleccione una categoría...</option>
                                    <option value="Electrónica">Electrónica</option>
                                    <option value="Accesorios">Accesorios</option>
                                    <option value="Mobiliario">Mobiliario</option>
                                    <option value="Servicios">Servicios</option>
                                    <option value="Alimentos">Alimentos</option>
                                    <option value="Ropa">Ropa</option>
                                    <option value="Otros">Otros</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> Guardar Producto
                            </button>
                            <a href="${pageContext.request.contextPath}/productos" class="btn btn-secondary">
                                <i class="bi bi-x-circle"></i> Cancelar
                            </a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Información de ayuda -->
        <div class="row justify-content-center mt-3">
            <div class="col-md-6">
                <div class="alert alert-info">
                    <i class="bi bi-info-circle"></i> <strong>Consejo:</strong>
                    <ul class="mb-0 mt-2">
                        <li>El código del producto debe ser único en el sistema</li>
                        <li>Si el producto no aplica IVA, deje sin marcar la casilla</li>
                        <li>El stock inicial puede ser modificado posteriormente</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>