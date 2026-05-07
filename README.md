# 🏢 GESTOR EMPRESARIAL INTEGRADO

Sistema de gestión contable y administrativa para pequeñas y medianas empresas, desarrollado como proyecto formativo del SENA.

## 📋 Descripción

El Gestor Empresarial Integrado es una solución tecnológica que automatiza y centraliza los procesos administrativos, contables y de gestión documental de una empresa. El sistema permite:

- ✅ Emisión y gestión de facturas electrónicas
- ✅ Control de inventario y productos
- ✅ Gestión de clientes y proveedores
- ✅ Generación de reportes contables
- ✅ Control de gastos y viáticos (en desarrollo)
- ✅ Gestión de nómina y seguridad social (en desarrollo)

## 🚀 Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 11 | Lenguaje principal |
| MySQL | 8.0 | Base de datos |
| JDBC | - | Conexión a base de datos |
| Git | - | Control de versiones |

## 📁 Estructura del Proyecto
GESTOR_EMPRESARIAL_INTEGRADO/
│
├── src/
│ └── main/
│ ├── java/
│ │ └── com/
│ │ └── gestorempresarial/
│ │ ├── controlador/ # Lógica de negocio
│ │ │ ├── FacturaControlador.java
│ │ │ └── ClienteControlador.java
│ │ ├── dao/ # Acceso a datos (JDBC)
│ │ │ ├── ConexionBD.java
│ │ │ ├── ClienteDAO.java
│ │ │ ├── ProductoDAO.java
│ │ │ ├── FacturaDAO.java
│ │ │ └── DetalleFacturaDAO.java
│ │ ├── modelo/ # Clases de dominio
│ │ │ ├── Cliente.java
│ │ │ ├── Producto.java
│ │ │ ├── Factura.java
│ │ │ └── DetalleFactura.java
│ │ └── vista/ # Interfaz de usuario
│ │ └── MenuPrincipal.java
│ └── resources/ # Recursos y configuraciones
│
├── sql/
│ └── schema.sql # Script de base de datos
├── lib/ # Librerías externas JAR
├── docs/ # Documentación del proyecto
├── .gitignore
└── README.md

## Autor
- Andres Felipe Corzo Angarita

## Instructor
Francisco Arnaldo Vargas Bermudez

## Institución
SENA - Análisis y Desarrollo de Software (Ficha: 3070323)
"@ | Out-File -FilePath README.md -Encoding UTF8