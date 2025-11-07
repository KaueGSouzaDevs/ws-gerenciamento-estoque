package br.com.kg.estoque.enuns;

import br.com.kg.estoque.custom.EnumComDescricao;

/**
 * Enum que representa as diversas unidades de medida utilizadas para os materiais no estoque.
 * Cada unidade de medida possui um símbolo e uma descrição associada.
 */
public enum UnidadeMedida implements EnumComDescricao {

    // Comprimento
    /** Metro (m) */
    METRO("m", "Metro"),
    /** Centímetro (cm) */
    CENTIMETRO("cm", "Centímetro"),
    /** Milímetro (mm) */
    MILIMETRO("mm", "Milímetro"),
    /** Quilômetro (km) */
    QUILOMETRO("km", "Quilômetro"),

    // Massa
    /** Quilograma (kg) */
    QUILOGRAMA("kg", "Quilograma"),
    /** Grama (g) */
    GRAMA("g", "Grama"),
    /** Miligrama (mg) */
    MILIGRAMA("mg", "Miligrama"),
    /** Tonelada (t) */
    TONELADA("t", "Tonelada"),

    // Volume
    /** Litro (L) */
    LITRO("L", "Litro"),
    /** Mililitro (mL) */
    MILILITRO("mL", "Mililitro"),
    /** Metro Cúbico (m³) */
    METRO_CUBICO("m³", "Metro Cúbico"),

    // Área
    /** Metro Quadrado (m²) */
    METRO_QUADRADO("m²", "Metro Quadrado"),

    // Peças
    /** Unidade (un) */
    UNIDADE("un", "Unidade"),
    /** Caixa (cx) */
    CAIXA("cx", "Caixa"),
    /** Pacote (pct) */
    PACOTE("pct", "Pacote"),
    /** Lote (lt) */
    LOTE("lt", "Lote");

    private final String simbolo;
    private final String descricao;

    /**
     * Construtor para o enum, associando um símbolo e uma descrição a cada unidade de medida.
     *
     * @param simbolo   A abreviação ou símbolo da unidade (ex: "kg").
     * @param descricao O nome por extenso da unidade (ex: "Quilograma").
     */
    UnidadeMedida(String simbolo, String descricao) {
        this.simbolo = simbolo;
        this.descricao = descricao;
    }

    /**
     * Retorna o símbolo da unidade de medida.
     *
     * @return O símbolo como uma String.
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Retorna a descrição completa da unidade de medida.
     *
     * @return A descrição como uma String.
     */
    @Override
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna uma representação textual formatada da unidade de medida.
     *
     * @return Uma String no formato "Descrição (Símbolo)".
     */
    @Override
    public String toString() {
        return descricao + " (" + simbolo + ")";
    }
}
