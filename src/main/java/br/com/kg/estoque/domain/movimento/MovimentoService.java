package br.com.kg.estoque.domain.movimento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import br.com.kg.estoque.custom.Auxiliar;
import br.com.kg.estoque.custom.DataTableParams;
import br.com.kg.estoque.custom.DataTableResult;
import br.com.kg.estoque.domain.material.Material;
import br.com.kg.estoque.domain.material.MaterialService;

@Service
public class MovimentoService {

    @Autowired
    private MovimentoRepository movimentoRepository;

    @Autowired
    private MaterialService materialService;

    @Autowired 
    private MovimentoCustomRepository movimentoCustomRepository;
    

    /**
     * !Função para salvar o movimento cadastrado
     * @param movimento
     */
    public void salvarMovimento(Movimento movimento) {
        movimentoRepository.save(movimento);
        Material material = materialService.buscarPorId(movimento.getMaterial().getId()).get();


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

    }   

    public List<Movimento> buscarTodos() {
        return movimentoRepository.findAll();
    }

    public Movimento buscarPorId(Long id) {
        return movimentoRepository.findById(id).orElse(null);
    }

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

    public DataTableResult dataTableMovimento(DataTableParams params){

        String[] colunas={"id", "data", "tipo", "material", "quantidade", "responsavel"};

        var movimentoList = movimentoCustomRepository.listEntitiesToDataTable(colunas, params, Movimento.class);

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

        DataTableResult dataTable = new DataTableResult();
        dataTable.setSEcho(String.valueOf(System.currentTimeMillis()));
        dataTable.setITotalRecords(movimentoList.size());
        dataTable.setITotalDisplayRecords(
            movimentoCustomRepository.totalEntitiesToDataTable(colunas, Auxiliar.removeAcentos(params.sSearch()), Movimento.class)
        );
        dataTable.setAaData(listaObjects.toArray());
        return dataTable;

    }




}
