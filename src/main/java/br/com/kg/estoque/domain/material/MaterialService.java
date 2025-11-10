package br.com.kg.estoque.domain.material;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.kg.estoque.custom.DataTableRequest;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.custom.DataTableUtils;
import br.com.kg.estoque.enuns.SituacaoMaterial;

/**
 * Serviço de negócios para a entidade {@link Material}.
 * Esta classe encapsula a lógica de negócios e a interação com os repositórios
 * para a entidade Material.
 */
@Service
public class MaterialService {
    
    
    private final MaterialRepository materialRepository;
    private final MaterialCustomRepository materialCustomRepository;

    @Autowired
    private ModelMapper mapper;

    /**
     * Constrói o serviço com as dependências de repositório necessárias.
     *
     * @param materialRepository       O repositório JPA padrão para operações CRUD.
     * @param materialCustomRepository O repositório customizado para consultas dinâmicas.
     */
    public MaterialService(MaterialRepository materialRepository, MaterialCustomRepository materialCustomRepository) {
        this.materialRepository = materialRepository;
        this.materialCustomRepository = materialCustomRepository;
    }

    /**
     * Salva ou atualiza uma entidade de material no banco de dados.
     * 
     * @param material A entidade {@link Material} a ser salva.
     */
    public void salvar(Material material) {
        materialRepository.save(material);
    }

    /**
     * Busca todos os materiais cadastrados, ordenados pelo nome em ordem ascendente.
     * 
     * @return Uma {@link List} de todas as entidades {@link Material}.
     */
    public List<Material> buscarTodos() {
        return materialRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    /**
     * Busca um material específico pelo seu identificador único.
     * 
     * @param idMaterial O ID do material a ser buscado.
     * @return Um {@link Optional} contendo o {@link Material} se encontrado, ou vazio caso contrário.
     */
    public Optional<Material> buscarPorId(Long idMaterial) {
        return materialRepository.findById(idMaterial);
    }

    /**
     * Exclui um material do banco de dados com base no seu ID.
     * 
     * @param idMaterial O ID do material a ser excluído.
     */
    public void excluir(Long idMaterial) {
        materialRepository.deleteById(idMaterial);
    }

    /**
     * Prepara os dados do material para serem exibidos em um componente DataTables.
     *
     * @param params Os parâmetros da requisição do DataTables.
     * @return Um objeto {@link DataTableResult} pronto para ser serializado em JSON.
     */
    public DataTableResult dataTableMaterial(DataTableRequest params) {

        DataTableUtils.parseParams(params);
        List<Material> materiaisList = materialCustomRepository.listEntitiesToDataTable(DataTableUtils.parseColumns(params), params, Material.class);
        Integer registrosFiltrados = materialCustomRepository.totalEntitiesToDataTable(DataTableUtils.parseColumns(params), params.getSearch().getValue(), Material.class);

        DataTableResult dataTable = new DataTableResult();
        dataTable.setDraw(params.getDraw());
        dataTable.setRecordsTotal((int)materialRepository.count());
        dataTable.setRecordsFiltered(registrosFiltrados);
        dataTable.setData(materiaisList.stream().map(c -> mapper.map(c, MaterialDTO.class)).toList());
        dataTable.setError(null);
        return dataTable;
    }

    public List<Material> buscarTodosAtivos() {
        return materialRepository.findAllBySituacao(SituacaoMaterial.ATIVO);
    }

    public Long getMateriaisEmEstoqueBaixo() {
        return materialRepository.getMateriaisEmEstoqueBaixo();
    }

    public BigDecimal getValorTotalEstoque() {
        return materialRepository.getValorTotalEstoque();
    }

    public Long getMateriaisEmEstoque() {
        return materialRepository.getMateriaisEmEstoque(SituacaoMaterial.ATIVO);
    }
}
