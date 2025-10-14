use UTN_integradorProg2;

INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Av. Corrientes', 1234, 'Ciudad Autónoma de Buenos Aires', 'Buenos Aires', 'C1043', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Av. Colón', 567, 'Córdoba', 'Córdoba', 'X5000', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Bv. Oroño', 890, 'Rosario', 'Santa Fe', 'S2000', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'San Martín', 1500, 'Mendoza', 'Mendoza', 'M5500', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Calle 7', 850, 'La Plata', 'Buenos Aires', 'B1900', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Av. Independencia', 2100, 'Mar del Plata', 'Buenos Aires', 'B7600', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Av. Mate de Luna', 1800, 'San Miguel de Tucumán', 'Tucumán', 'T4000', 'Argentina');
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Av. Entre Ríos', 950, 'Salta', 'Salta', 'A4400', 'Argentina');


INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'TechSolutions S.A.', '30712345678', 'Desarrollo de Software', 'contacto@techsolutions.com.ar', 1);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'Consultores Asociados S.R.L.', '30712345689', 'Consultoría Empresarial', 'info@consultores.com.ar', 2);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'Constructora del Litoral S.A.', '30712345690', 'Construcción de Obras', 'obras@constructora.com.ar', 3);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'AgroMendoza S.A.', '30712345701', 'Producción Agrícola', 'ventas@agromendoza.com.ar', 4);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'Distribuidora La Plata S.R.L.', '30712345712', 'Comercio Mayorista', 'administracion@distlaplata.com.ar', 5);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'Turismo Costa Atlántica S.A.', '30712345723', 'Servicios Turísticos', 'reservas@turismoatlantida.com.ar', 6);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'Alimentos del Norte S.A.', '30712345734', 'Industria Alimenticia', 'calidad@alimentosnorte.com.ar', 7);
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (FALSE, 'FinanSalta S.R.L.', '30712345745', 'Servicios Financieros', 'clientes@finansalta.com.ar', 8);

-- Domicilio sin empresa
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Av. Libertador', 3200, 'Ciudad Autónoma de Buenos Aires', 'Buenos Aires', 'C1425', 'Argentina');

INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (FALSE, 'Calle Rivadavia', 456, 'Neuquén', 'Neuquén', 'Q8300', 'Argentina');

-- Domicilio eliminado
INSERT INTO domicilio_fiscal (eliminado, calle, numero, ciudad, provincia, codigo_postal, pais)
VALUES (TRUE, 'Calle Eliminada', 999, 'Ciudad Eliminada', 'Provincia Eliminada', 'X9999', 'Argentina');

-- Empresa eliminada (sin domicilio)
INSERT INTO empresa (eliminado, razon_social, cuit, actividad_principal, email, domicilio_fiscal_id)
VALUES (TRUE, 'Empresa Inactiva S.A.', '30712345756', 'Actividad Cesada', NULL, NULL);


-- Contar domicilios fiscales activos
SELECT 'Domicilios Fiscales Activos' AS Concepto, COUNT(*) AS Total
FROM domicilio_fiscal
WHERE eliminado = FALSE;

-- Contar empresas activas
SELECT 'Empresas Activas' AS Concepto, COUNT(*) AS Total
FROM empresa
WHERE eliminado = FALSE;

-- Mostrar empresas con sus domicilios
SELECT
    e.id AS empresa_id,
    e.razon_social,
    e.cuit,
    e.actividad_principal,
    d.calle,
    d.numero,
    d.ciudad,
    d.provincia
FROM
    empresa e
        LEFT JOIN
    domicilio_fiscal d ON e.domicilio_fiscal_id = d.id
WHERE
    e.eliminado = FALSE
ORDER BY
    e.razon_social;

-- Mostrar domicilios sin empresa
SELECT
    d.id,
    d.calle,
    d.numero,
    d.ciudad,
    d.provincia,
    'Sin asignar' AS estado
FROM
    domicilio_fiscal d
        LEFT JOIN
    empresa e ON d.id = e.domicilio_fiscal_id
WHERE
    d.eliminado = FALSE
  AND e.id IS NULL;
