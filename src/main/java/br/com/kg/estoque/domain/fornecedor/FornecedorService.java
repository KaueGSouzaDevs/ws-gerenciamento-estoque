package br.com.kg.estoque.domain.fornecedor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.enuns.SituacaoFornecedor;

/**
 * Serviço de negócios para a entidade {@link Fornecedor}.
 * Esta classe encapsula a lógica de negócios, validações e interação com o repositório
 * para a entidade Fornecedor.
 */
@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    /**
     * Constrói o serviço com a dependência do repositório de fornecedor.
     *
     * @param fornecedorRepository O repositório para acesso aos dados de fornecedores.
     */
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    /**
     * Salva ou atualiza uma entidade de fornecedor no banco de dados.
     * 
     * @param fornecedor A entidade {@link Fornecedor} a ser salva.
     */
    public void salvar(Fornecedor fornecedor) {
        fornecedorRepository.save(fornecedor);
    }

    /**
     * Busca um fornecedor específico pelo seu identificador único.
     * 
     * @param id O ID do fornecedor a ser buscado.
     * @return Um {@link Optional} contendo o {@link Fornecedor} se encontrado, ou vazio caso contrário.
     */
    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }

    /**
     * Busca todos os fornecedores cadastrados.
     * 
     * @return Uma {@link List} de todas as entidades {@link Fornecedor}.
     */
    public List<Fornecedor> buscarTodos() {
        return fornecedorRepository.findAll();
    }

    /**
     * Busca um fornecedor pelo seu número de CNPJ.
     * 
     * @param cnpj O CNPJ do fornecedor a ser buscado.
     * @return Um {@link Optional} contendo o {@link Fornecedor} se encontrado.
     */
    public Optional<Fornecedor> buscaPorCnpj(String cnpj) {
        return fornecedorRepository.findByCnpjCpf(cnpj);
    }

    /**
     * Busca um fornecedor por CNPJ, garantindo que não seja o mesmo fornecedor (pelo ID).
     * Útil para validações de unicidade ao editar um fornecedor existente.
     * 
     * @param cnpj O CNPJ a ser buscado.
     * @param id O ID do fornecedor a ser ignorado na busca.
     * @return Um {@link Optional} contendo um fornecedor com o mesmo CNPJ, mas ID diferente, se houver.
     */
    public Optional<Fornecedor> buscarPorCnpjEIdDiferenteDoMeu(String cnpj, Long id) {
        return fornecedorRepository.findByCnpjCpfAndIdNot(cnpj, id);
    }

    /**
     * Exclui um fornecedor do banco de dados com base no seu ID.
     * 
     * @param idFornecedor O ID do fornecedor a ser excluído.
     */
    public void excluir(Long idFornecedor) {
        fornecedorRepository.deleteById(idFornecedor);
    }

    /**
     * Valida se o CNPJ de um fornecedor sendo alterado já existe em outro cadastro.
     * Adiciona um erro ao {@link BindingResult} se a validação falhar.
     * 
     * @param fornecedor O fornecedor com os dados atualizados.
     * @param result O objeto BindingResult para registrar erros de validação.
     */
    public void validaAlteracao(Fornecedor fornecedor, BindingResult result) {
        if (fornecedor.getId() != null && (buscarPorCnpjEIdDiferenteDoMeu(fornecedor.getCnpjCpf(), fornecedor.getId()).isPresent())) {
            result.rejectValue("cnpj", "", "CNPJ já cadastrado");
        }
    }

    /**
     * Valida se o CNPJ de um novo fornecedor já existe.
     * Adiciona um erro ao {@link BindingResult} se a validação falhar.
     * 
     * @param fornecedor O novo fornecedor a ser validado.
     * @param result O objeto BindingResult para registrar erros de validação.
     */
    public void validaInclusao(Fornecedor fornecedor, BindingResult result) {
        if (fornecedor.getId() == null && (buscaPorCnpj(fornecedor.getCnpjCpf()).isPresent())) {
            result.rejectValue("cnpj", "null", "CNPJ já cadastrado");
        }
    }

    /**
     * Prepara os dados do fornecedor para serem exibidos em um componente DataTables.
     * 
     * @param params Os parâmetros da requisição do DataTables.
     * @return Um objeto {@link DataTableResult} pronto para ser serializado em JSON.
     */
    public DataTableResult dataTableFornecedores(DataTableParams params)  {

		String[] colunas = {"id", "nome", "telefone", "email", "contato", "situacao", "cnpjCpf"};
				
		List<Fornecedor> fornecedoresList = fornecedorRepository.listFornecedoresToDataTable(colunas, params);
        long registrosFiltrados = fornecedorRepository.totalFornecedoresToDataTable(colunas, Auxiliar.removeAcentos(params.getSearchValue()));
		
		DataTableResult dataTable = new DataTableResult();
		dataTable.setDraw(params.getDraw());
		dataTable.setRecordsTotal((int) fornecedorRepository.count());
		dataTable.setRecordsFiltered(registrosFiltrados);
		dataTable.setData(fornecedoresList.stream()
				.map(c -> new Object[]{
						c.getId(),
						c.getNome(),
                        c.getTelefone(),
                        c.getEmail(),
                        c.getContato(),
						c.getSituacao().getDescricao(),
                        c.getCnpjCpf()
				}).toList());

		return dataTable;
	}

    public List<Fornecedor> buscarTodosAtivos() {
        return fornecedorRepository.findAllBySituacao(SituacaoFornecedor.ATIVO);
    }
}
