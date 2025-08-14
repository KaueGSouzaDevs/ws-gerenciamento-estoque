package br.com.kg.estoque.domain.movimento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.material.Material;
import br.com.kg.estoque.domain.material.MaterialService;

@Service
public class MovimentoService {

    private MovimentoRepository movimentoRepository;
    private MaterialService materialService; 
    private MovimentoCustomRepository movimentoCustomRepository;

    public MovimentoService(MovimentoRepository movimentoRepository, MaterialService materialService,
            MovimentoCustomRepository movimentoCustomRepository) {
        this.movimentoRepository = movimentoRepository;
        this.materialService = materialService;
        this.movimentoCustomRepository = movimentoCustomRepository;
    }



    /**
     * Função para salvar o movimento cadastrado
     * @param movimento
     */
    public void salvarMovimento(Movimento movimento) {
        movimentoRepository.save(movimento);
        Optional<Material> materialOptional = materialService.buscarPorId(movimento.getMaterial().getId());

        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();

            if (movimento.getTipo().equalsIgnoreCase("entrada")) {
                material.setSaldo(material.getSaldo() + movimento.getQuantidade());
                if (material.getSaldo() > material.getEstoqueMaximo()) {
                    System.out.println("Disparo de e-mail: O estoque do material " + material.getNome() + " excedeu o máximo permitido.");                 
                }
                material.setFornecedor(movimento.getFornecedor());
    
            } else if (movimento.getTipo().equalsIgnoreCase("saida")) {
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
     * Retorna uma lista de todos os movimentos cadastrados.
     * 
     * @return Uma lista de movimentos.
     */
    public List<Movimento> buscarTodos() {
        return movimentoRepository.findAll();
    }



    /**
     * Retorna um movimento com base no id.
     * 
     * @param id O id do movimento a ser encontrado.
     * @return Um movimento com o id informado, ou null se o movimento não for encontrado.
     */
    public Movimento buscarPorId(Long id) {
        return movimentoRepository.findById(id).orElse(null);
    }



    /**
     * Retorna um Optional contendo um movimento com base no id,
     * ou um Optional vazio se o movimento não for encontrado.
     * 
     * @param idMovimento O id do movimento a ser encontrado.
     * @return Um Optional contendo um movimento com o id informado, ou um Optional vazio
     *         se o movimento não for encontrado.
     */
        public Optional<Movimento> buscarPorIdOptional(Long idMovimento) {
        return movimentoRepository.findById(idMovimento);
    }



    /**
     * Realiza a exclusão da movimentação fazendo a atualização do saldo de material
     * @param id
     */
    public void excluir(Long id) {

        Movimento movimento = buscarPorId(id);
        Material material = materialService.buscarPorId(movimento.getMaterial().getId()).get();
        if (movimento.getTipo().equalsIgnoreCase("Entrada")) {
            material.setSaldo(material.getSaldo() - movimento.getQuantidade());
        } else if(movimento.getTipo().equalsIgnoreCase("Saida")) {
            material.setSaldo(material.getSaldo() + movimento.getQuantidade());
        }
        materialService.salvar(material);
        movimentoRepository.deleteById(id);
    }



    /**
     * !Validação de CADASTRO
     * 
     * !Valida pelo tipo de movimentação (Entrada) os campos "Nota Fiscal" e "Fornecedor"
     * @param movimento
     * @param result
     */
    public void validaEntradaMaterial(Movimento movimento, BindingResult result) {
        if (movimento.getFornecedor() == null) {
            result.rejectValue("fornecedor", "","Fornecedor é obrigatório!");
        }
        if (movimento.getNotaFiscal() == null || movimento.getNotaFiscal().isEmpty()) {
            result.rejectValue("notaFiscal","" , "Fornecedor é obrigatório!");
        }
    }



    /**
     * Gera um DataTableResult para o CRUD de movimentações, com base nos parâmetros
     * de configuração do DataTable.
     * 
     * @param params Os parâmetros de configuração do DataTable.
     * @return Um DataTableResult contendo as informações para o CRUD de movimentações.
     */
    public DataTableResult dataTableMovimento(DataTableParams params){

        String[] colunas={"id", "data", "tipo", "material.nome", "quantidade", "responsavel"};
        var movimentoList = movimentoCustomRepository.listMovimentosToDataTable(colunas, params);
        List<Object[]> listaObjects = new ArrayList<Object[]>();

        movimentoList.forEach( movimento -> {
            
            Object[] linha = {
                movimento.getId(),
                movimento.getData(),
                movimento.getTipo(),
                movimento.getMaterial().getNome(),
                movimento.getQuantidade(),
                movimento.getResponsavel(),
                movimento.getId(),
            };
            listaObjects.add(linha);
        });

        Long registrosFiltrados = movimentoCustomRepository.totalMovimentosToDataTable(colunas, Auxiliar.removeAcentos(params.getSearchValue()));
        DataTableResult dataTable = new DataTableResult();
        dataTable.setDraw(String.valueOf(System.currentTimeMillis()));
        dataTable.setRecordsTotal(movimentoList.size());
        dataTable.setRecordsFiltered(registrosFiltrados);
        dataTable.setData(listaObjects.stream().toList());
        return dataTable;
    }




}
