package br.com.kg.estoque.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.DataTableRequest.Order;

/**
 * Classe utilitária para processar e normalizar os parâmetros de uma requisição
 * do componente jQuery DataTables, encapsulados no objeto {@link DataTableRequest}.
 * <p>
 * Fornece métodos estáticos para extrair e tratar informações de ordenação,
 * colunas e paginação, garantindo que valores nulos ou ausentes sejam
 * substituídos por padrões seguros.
 */
@Service
public class DataTableUtils {

    /**
     * Extrai e normaliza as informações de ordenação da requisição do DataTables.
     * <p>
     * Itera sobre a lista de ordens da requisição, garantindo que a coluna e a direção
     * da ordenação tenham valores padrão (coluna 0 e direção "asc") caso sejam nulos.
     * @param dataTableRequest O objeto {@link DataTableRequest} contendo os parâmetros da requisição.
     * @return Uma lista de objetos {@link Order} com os dados de ordenação normalizados.
     */
    public static List<Order> parseOrder(DataTableRequest dataTableRequest) {
        List<Order> orders = new ArrayList<>();

        dataTableRequest.getOrder().forEach(orderRequest -> {
            Order order = new Order();
            order.setColumn(Optional.ofNullable(orderRequest.getColumn()).orElse(0));
            order.setDir(Optional.ofNullable(orderRequest.getDir()).orElse("asc"));
            orders.add(order);
        });
        
        return orders;
    }


    /**
     * Extrai os nomes de dados das colunas da requisição do DataTables.
     * <p>
     * Para cada coluna na requisição, este método extrai o valor do campo 'data',
     * que corresponde ao nome da propriedade no objeto de dados. Se o valor for nulo, uma string vazia é usada como padrão.
     * @param dataTableRequest O objeto {@link DataTableRequest} contendo os parâmetros da requisição.
     * @return Uma lista de strings com os nomes de dados de cada coluna.
     */
    public static List<String> parseColumns(DataTableRequest dataTableRequest) {
        List<String> colunas = new ArrayList<>();
        dataTableRequest.getColumns().forEach(columnRequest -> {
            if (!Auxiliar.isEmptyOrNull(columnRequest.getData())) {
                colunas.add(columnRequest.getData());
            }
        });
        return colunas;
    }

    /**
     * Normaliza os parâmetros principais da requisição do DataTables, como paginação e busca.
     * <p>
     * Este método atribui valores padrão para os campos 'draw' (1), 'start' (1), e 'length' (10)
     * se eles forem nulos. Também garante que o valor de busca não seja nulo (substituindo por uma string vazia)
     * e define o parâmetro 'regex' da busca como 'false' se for nulo.
     * @param dataTableRequest O objeto {@link DataTableRequest} a ser normalizado.
     */
    public static void parseParams(DataTableRequest dataTableRequest) {
        dataTableRequest.setDraw(Optional.ofNullable(dataTableRequest.getDraw()).orElse(1));
        dataTableRequest.setStart(Optional.ofNullable(dataTableRequest.getStart()).orElse(1));
        dataTableRequest.setLength(Optional.ofNullable(dataTableRequest.getLength()).orElse(10));
        if (Auxiliar.isEmptyOrNull(dataTableRequest.getSearch().getValue())) {
            dataTableRequest.getSearch().setValue("");
        }
        dataTableRequest.getSearch().setRegex(Optional.ofNullable(dataTableRequest.getSearch().isRegex()).orElse(false));
    }
}
