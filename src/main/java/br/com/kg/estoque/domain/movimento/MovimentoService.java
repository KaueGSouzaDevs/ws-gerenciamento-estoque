package br.com.kg.estoque.domain.movimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.kg.estoque.custom.DataTableRequest;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.custom.DataTableUtils;
import br.com.kg.estoque.domain.material.Material;
import br.com.kg.estoque.domain.material.MaterialService;
import br.com.kg.estoque.enuns.TipoMovimento;

/**
 * Serviço de negócios para a entidade {@link Movimento}.
 * Esta classe encapsula a lógica de negócios para registrar movimentos de estoque,
 * atualizar saldos de materiais e interagir com os repositórios.
 */
@Service
public class MovimentoService {

    private final MovimentoRepository movimentoRepository;
    private final MovimentoCustomRepository movimentoCustomRepository;
    private final MaterialService materialService;

    @Autowired
    private ModelMapper mapper;

    /**
     * Constrói o serviço com as dependências necessárias.
     *
     * @param movimentoRepository       O repositório para operações CRUD em Movimento.
     * @param materialService           O serviço para acessar e modificar dados de Material.
     * @param movimentoCustomRepository O repositório customizado para consultas dinâmicas de Movimento.
     */
    public MovimentoService(MovimentoRepository movimentoRepository, MaterialService materialService, MovimentoCustomRepository movimentoCustomRepository) {
        this.movimentoRepository = movimentoRepository;
        this.materialService = materialService;
        this.movimentoCustomRepository = movimentoCustomRepository;
    }

    /**
     * Salva um movimento e atualiza o saldo do material correspondente.
     * Se o tipo de movimento for "entrada", o saldo é incrementado.
     * Se for "saída", o saldo é decrementado.
     * Alertas de sistema são impressos se o estoque exceder o máximo ou cair abaixo do mínimo.
     *
     * @param movimento O objeto {@link Movimento} a ser salvo.
     */
    public void salvarMovimento(Movimento movimento) {
        movimentoRepository.save(movimento);
        Optional<Material> materialOptional = materialService.buscarPorId(movimento.getMaterial().getId());

        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();

            if (movimento.getTipoMovimento() == TipoMovimento.ENTRADA) {
                material.setSaldo(material.getSaldo() + movimento.getQuantidade());
                if (material.getSaldo() > material.getEstoqueMaximo()) {
                    System.out.println("Disparo de e-mail: O estoque do material " + material.getNome() + " excedeu o máximo permitido.");
                }
                material.setFornecedor(movimento.getFornecedor());
    
            } else if (movimento.getTipoMovimento() == TipoMovimento.SAIDA) {
                material.setSaldo(material.getSaldo() - movimento.getQuantidade());
                if (material.getSaldo() < material.getEstoqueMinimo()) {
                    System.out.println("Disparo de e-mail: O estoque do material " + material.getNome() + " está abaixo do mínimo permitido.");
                }
            }
            materialService.salvar(material); // Salva as alterações no material
        } else {
            System.out.println("Material com ID (" + movimento.getMaterial().getId() + ") não encontrado.");
        }
    }

    /**
     * Busca todos os movimentos cadastrados.
     * 
     * @return Uma {@link List} de todas as entidades {@link Movimento}.
     */
    public List<Movimento> buscarTodos() {
        return movimentoRepository.findAll();
    }

    /**
     * Busca um movimento específico pelo seu ID.
     * 
     * @param id O ID do movimento a ser buscado.
     * @return O {@link Movimento} encontrado, ou `null` se não existir.
     */
    public Movimento buscarPorId(Long id) {
        return movimentoRepository.findById(id).orElse(null);
    }

    /**
     * Busca um movimento específico pelo seu ID, retornando um {@link Optional}.
     * 
     * @param idMovimento O ID do movimento a ser buscado.
     * @return Um {@link Optional} contendo o {@link Movimento} se encontrado, ou vazio caso contrário.
     */
    public Optional<Movimento> buscarPorIdOptional(Long idMovimento) {
        return movimentoRepository.findById(idMovimento);
    }

    /**
     * Exclui um movimento e reverte a alteração no saldo do material correspondente.
     * Se o movimento era uma entrada, a quantidade é subtraída do saldo.
     * Se era uma saída, a quantidade é adicionada de volta ao saldo.
     *
     * @param id O ID do movimento a ser excluído.
     */
    public void excluir(Long id) {

        Movimento movimento = buscarPorId(id);
        if (movimento != null && movimento.getMaterial() != null) {
            materialService.buscarPorId(movimento.getMaterial().getId()).ifPresent(material -> {
                if (movimento.getTipoMovimento() == TipoMovimento.ENTRADA) {
                    material.setSaldo(material.getSaldo() - movimento.getQuantidade());
                } else if(movimento.getTipoMovimento() == TipoMovimento.SAIDA) {
                    material.setSaldo(material.getSaldo() + movimento.getQuantidade());
                }
                materialService.salvar(material);
                movimentoRepository.deleteById(id);
            });
        }
    }

    /**
     * Valida os campos obrigatórios para um movimento de entrada.
     * Verifica se o fornecedor e a nota fiscal foram preenchidos.
     *
     * @param movimento O movimento a ser validado.
     * @param result O objeto {@link BindingResult} para registrar erros de validação.
     */
    public void validaEntradaMaterial(Movimento movimento, BindingResult result) {
        if (movimento.getFornecedor() == null) {
            result.rejectValue("fornecedor", "","Fornecedor é obrigatório!");
        }
        if (movimento.getNotaFiscal() == null || movimento.getNotaFiscal().isEmpty()) {
            result.rejectValue("notaFiscal","" , "Nota Fiscal é obrigatória!");
        }
    }

    /**
     * Prepara os dados do movimento para serem exibidos em um componente DataTables.
     *
     * @param params Os parâmetros da requisição do DataTables.
     * @return Um objeto {@link DataTableResult} pronto para ser serializado em JSON.
     */
    public DataTableResult dataTableMovimento(DataTableRequest params){

        DataTableUtils.parseParams(params);
        List<Movimento> movimentoList = movimentoCustomRepository.listEntitiesToDataTable(DataTableUtils.parseColumns(params), params, Movimento.class);
        Integer registrosFiltrados = movimentoCustomRepository.totalEntitiesToDataTable(DataTableUtils.parseColumns(params), params.getSearch().getValue(), Movimento.class);

        DataTableResult dataTable = new DataTableResult();
        dataTable.setDraw(params.getDraw());
        dataTable.setRecordsTotal((int) movimentoRepository.count());
        dataTable.setRecordsFiltered(registrosFiltrados);
        dataTable.setData(movimentoList.stream().map(c -> mapper.map(c, MovimentoDTO.class)).toList());
        return dataTable;
    }

    public BigDecimal getValorSaidasMes() {
        LocalDateTime dataInicioMes = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime dataFimMes = dataInicioMes.plusMonths(1).minusDays(1);
        return movimentoRepository.getValorSaidasMes(dataInicioMes, dataFimMes, TipoMovimento.SAIDA);
    }
}
