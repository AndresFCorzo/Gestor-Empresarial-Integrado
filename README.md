# 🏢 GESTOR EMPRESARIAL INTEGRADO

## Descripción
Sistema de gestión contable y administrativa para pequeñas y medianas empresas. Incluye dos versiones:
- **Versión Consola**: Aplicación de línea de comandos para pruebas y desarrollo
- **Versión Web**: Aplicación web con Servlets, JSP y Bootstrap

## Tecnologías
- Java 11
- MySQL 8.0
- JDBC
- Servlets 4.0
- JSP 2.3 / JSTL 1.2
- Bootstrap 5
- Git

## Estructura del Proyecto
GESTOR_EMPRESARIAL/
├── src/main/java/com/gestorempresarial/
│ ├── modelo/ # Clases de dominio (COMPARTIDO)
│ ├── dao/ # Acceso a datos (COMPARTIDO)
│ ├── utils/ # Utilidades (COMPARTIDO)
│ ├── console/ # Versión consola
│ └── web/ # Versión web (servlets)
├── src/main/webapp/ # JSPs y recursos web
└── sql/ # Scripts de base de datos


## Requisitos
- JDK 11+
- MySQL 8.0+
- Apache Tomcat 9+ (para versión web)

## Instalación

### 1. Configurar Base de Datos
```bash
mysql -u root -p < sql/schema.sql