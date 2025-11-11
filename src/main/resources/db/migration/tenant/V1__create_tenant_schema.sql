CREATE TABLE fornecedores (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE materiais (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    categoria_id BIGINT,
    fornecedor_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    FOREIGN KEY (fornecedor_id) REFERENCES fornecedores(id)
);

CREATE TABLE grupos_acesso (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE permissoes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE movimentos (
    id SERIAL PRIMARY KEY,
    material_id BIGINT,
    quantity INTEGER,
    type VARCHAR(255),
    FOREIGN KEY (material_id) REFERENCES materiais(id)
);
