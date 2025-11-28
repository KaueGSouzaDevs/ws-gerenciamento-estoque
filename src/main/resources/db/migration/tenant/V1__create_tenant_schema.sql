CREATE TABLE IF NOT EXISTS fornecedores (
    id SERIAL PRIMARY KEY,
    cnpj_cpf VARCHAR(18) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(75) NOT NULL,
    contato VARCHAR(50) NOT NULL,
    situacao VARCHAR(10) NOT NULL,
    CONSTRAINT uk_fornecedores_cnpj_cpf UNIQUE (cnpj_cpf)
);

CREATE TABLE IF NOT EXISTS categorias (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    situacao VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS materiais (
    id SERIAL PRIMARY KEY,
    id_categoria BIGINT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    codigo_barras VARCHAR(13),
    fabricante VARCHAR(30),
    unidade_medida VARCHAR(20) NOT NULL,
    id_fornecedor BIGINT NOT NULL,
    preco_custo DECIMAL(11,2) NOT NULL,
    preco_venda DECIMAL(11,2) NOT NULL,
    local_armazenagem VARCHAR(30) NOT NULL,
    estoque_maximo INTEGER,
    estoque_minimo INTEGER NOT NULL,
    saldo INTEGER NOT NULL DEFAULT 0,
    situacao VARCHAR(10) NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id),
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id)
);

CREATE TABLE IF NOT EXISTS grupos_acessos (
    id SERIAL PRIMARY KEY,
    grupo VARCHAR(40) NOT NULL,
    descricao VARCHAR(100),
    situacao VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS permissao_grupo (
    id_grupo BIGINT NOT NULL,
    permissoes VARCHAR(255),
    FOREIGN KEY (id_grupo) REFERENCES grupos_acessos(id)
);

CREATE TABLE IF NOT EXISTS rel_usuarios_grupos_acessos (
    id_usuario BIGINT NOT NULL,
    id_grupo_acesso BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_grupo_acesso),
    FOREIGN KEY (id_grupo_acesso) REFERENCES grupos_acessos(id)
);

CREATE TABLE IF NOT EXISTS movimentos (
    id SERIAL PRIMARY KEY,
    data_movimento TIMESTAMP NOT NULL,
    id_material BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    tipo_movimento VARCHAR(10) NOT NULL,
    nota_fiscal VARCHAR(50),
    id_fornecedor BIGINT,
    responsavel VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_material) REFERENCES materiais(id),
    FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id)
);
