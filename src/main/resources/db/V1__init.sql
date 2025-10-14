DROP DATABASE IF EXISTS UTN_integradorProg2;

CREATE DATABASE IF NOT EXISTS UTN_integradorProg2
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE UTN_integradorProg2;

CREATE TABLE IF NOT EXISTS domicilio_fiscal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    calle VARCHAR(100) NOT NULL,
    numero INT,
    ciudad VARCHAR(80) NOT NULL,
    provincia VARCHAR(80) NOT NULL,
    codigo_postal VARCHAR(10),
    pais VARCHAR(80) NOT NULL,

    INDEX idx_domicilio_ciudad (ciudad),
    INDEX idx_domicilio_provincia (provincia),
    INDEX idx_domicilio_eliminado (eliminado)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS empresa (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    razon_social VARCHAR(120) NOT NULL,
    cuit VARCHAR(11) NOT NULL,
    actividad_principal VARCHAR(80),
    email VARCHAR(120),
    domicilio_fiscal_id BIGINT,
    -- constraints
    CONSTRAINT uk_empresa_cuit UNIQUE (cuit),
    CONSTRAINT chk_empresa_cuit_formato CHECK (cuit REGEXP '^[0-9]{11}$'),
    CONSTRAINT chk_empresa_email_formato CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$'),
    -- Clave foránea hacia domicilio_fiscal
    CONSTRAINT fk_empresa_domicilio FOREIGN KEY (domicilio_fiscal_id) REFERENCES domicilio_fiscal(id)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
    CONSTRAINT uk_empresa_domicilio UNIQUE (domicilio_fiscal_id),

    INDEX idx_empresa_razon_social (razon_social),
    INDEX idx_empresa_cuit (cuit),
    INDEX idx_empresa_eliminado (eliminado)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SELECT
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME
FROM
    information_schema.TABLES
WHERE
    TABLE_SCHEMA = 'UTN_integradorProg2'
ORDER BY
    TABLE_NAME;