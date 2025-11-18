# TP Integrador - Programación II (UTN)

Aplicación Java (Maven) para gestionar empresas y sus domicilios fiscales.
Proyecto educativo correspondiente al Trabajo Final Integrador de la materia Programación II.

---

## Link al video

- https://www.youtube.com/watch?v=27YGGctUmhI

---
  
## Resumen

- Lenguaje: Java 21
- Build: Maven
- Base de datos: MySQL
- Arquitectura: Capas (DAO → Service → Main/menu)

La aplicación ofrece un menú de consola para crear, leer, actualizar y eliminar empresas y domicilios fiscales.

---

## Estructura principal

- `/config` - Configuración y conexión a la base de datos.
- `/entities` - Entidades `Empresa` y `DomicilioFiscal`.
- `/dao` - DAOs para persistencia (MySQL).
- `/service` - Lógica de negocio (servicios).
- `/main` - `Main`, `AppMenu`, `ServiceManager` (entrada y menú de la aplicación).
- `/db` - Scripts SQL para crear y cargar la base de datos (`V1__init.sql`, `V1__load.sql`).

---

## Dominios (Entidades)

### Empresa
Representa una empresa registrada. Atributos principales:
- `id` (Long) - Identificador único auto-incrementado.
- `razonSocial` (String) - Nombre legal de la empresa. **Obligatorio.**
- `cuit` (String) - Código Único de Identificación Tributaria (11 dígitos numéricos). **Obligatorio y único.**
- `email` (String) - Correo electrónico. **Opcional.** Validación de formato.
- `actividadPrincipal` (String) - Rubro o actividad económica. **Opcional.**
- `domicilioFiscal` (DomicilioFiscal) - Relación 1:1 con el domicilio fiscal asociado. **Obligatorio.**
- `eliminado` (Boolean) - Bandera para eliminación lógica (soft delete).

### DomicilioFiscal
Representa el domicilio fiscal de una empresa. Atributos principales:
- `id` (Long) - Identificador único auto-incrementado.
- `calle` (String) - Nombre de la calle. **Obligatorio.**
- `numero` (String) - Número de la calle. **Opcional.**
- `ciudad` (String) - Ciudad o localidad. **Obligatorio.**
- `provincia` (String) - Provincia o estado. **Obligatorio.**
- `codigoPostal` (String) - Código postal (máximo 8 caracteres). **Opcional.**
- `pais` (String) - País. **Obligatorio.**
- `eliminado` (Boolean) - Bandera para eliminación lógica (soft delete).

### Relación entre dominios
- Una empresa tiene **exactamente un** domicilio fiscal (1:1).
- Un domicilio fiscal puede estar asociado a **una sola** empresa (constraint UNIQUE).
- Si se elimina una empresa, su domicilio fiscal se desvincula (SET NULL).

---

## Requisitos

- Java 21 (JDK) instalado y `java` en el PATH.
- Maven (recomendado) instalado y `mvn` en el PATH.
- MySQL (o MariaDB) corriendo localmente o remotamente.

---

## Uso (rápido)

Al ejecutar la aplicación se muestra un menú por consola. Algunas opciones típicas:

- Crear empresa: solicita razón social, CUIT, email, actividad y datos de domicilio fiscal.
- Listar empresas: muestra las empresas registradas.
- Leer empresa por ID: muestra una empresa específica.
- Actualizar/Eliminar: operaciones CRUD disponibles.

Sigue las indicaciones que aparecen en pantalla.


