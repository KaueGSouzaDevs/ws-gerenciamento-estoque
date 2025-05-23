package br.com.kg.estoque.custom;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@JsonSerialize
public class DataTableResult {

	@Getter @Setter
	@JsonProperty("draw")
	private String draw;

	@Getter @Setter
	@JsonProperty("recordsTotal")
	private Integer recordsTotal;

	@Getter @Setter
	@JsonProperty("recordsFiltered")
	private Long recordsFiltered;

	@Getter @Setter
	@JsonProperty("data")
	private List<Object[]> data;

  // getters e setters omitidos para brevidade
}
