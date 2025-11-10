package br.com.kg.estoque.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        /*
         *TODO: CRIAR UM CONVERSOR PARA ENUM COM DESCRICAO
        // Um conversor que sabe transformar QUALQUER EnumComDescricao em String
        Converter<EnumComDescricao, String> enumParaStringConverter = new AbstractConverter<EnumComDescricao, String>() {
            protected String convert(EnumComDescricao source) {
                // Se a origem for nula, retorna nulo.
                // Senão, chama o método da interface!
                return source == null ? null : source.getDescricao();
            }
        };

        // Adiciona o conversor genérico
        mapper.addConverter(enumParaStringConverter);
        */

        return mapper;
    }
}
