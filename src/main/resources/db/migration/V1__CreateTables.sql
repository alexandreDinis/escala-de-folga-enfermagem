-- V1__CreateTables.sql
-- Script de criação das tabelas para o projeto EscalaDeFolga

-- =====================================
-- Tabela Colaborador
-- =====================================
CREATE TABLE colaborador (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    cargo VARCHAR(255),
    turno VARCHAR(255)
);

-- =====================================
-- Tabela Escala
-- =====================================
CREATE TABLE escala (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    mes INT,
    ano INT
);

-- =====================================
-- Tabela EscalaColaborador
-- =====================================
CREATE TABLE escala_colaborador (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    escala_id BIGINT,
    colaborador_id BIGINT,
    data DATE,
    turno VARCHAR(255),
    CONSTRAINT fk_escala FOREIGN KEY (escala_id) REFERENCES escala(id),
    CONSTRAINT fk_colaborador FOREIGN KEY (colaborador_id) REFERENCES colaborador(id)
);
