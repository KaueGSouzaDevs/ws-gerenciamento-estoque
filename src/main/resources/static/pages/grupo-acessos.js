document.addEventListener('DOMContentLoaded', function () {

    // DataTable with Buttons
    let table = new DataTable('#datatable', {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        ajax: {
            url: `/grupos-acessos/dataTable`,
            type: 'GET' // ou 'POST'
        },
        serverSide: true,
        columns: [
            { data: 0 },
            { data: 1 },
            { data: 2 },
            {
                data: 3, class: "text-center coluna-acoes", orderable: false, width: "100px",
                render: function (data, type, row, meta) {
                    return `
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown" aria-expanded="false"><i class="icon-base bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu" style="">
                                <a class="dropdown-item" href="/grupos-acessos/${data}/editar"><i class="icon-base bx bx-pencil"></i> Editar</a>
                                <a class="dropdown-item" href="javascript:excluirGrupoAcesso(${data});"><i class="icon-base bx bx-trash"></i> Excluir</a>
                            </div>
                        </div>
                    `;
                }
            },
        ],
        dom: 'rt' +
            '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>',
    })

    if (document.getElementById('customSearchBox')) {
        document.getElementById('customSearchBox').addEventListener('input', function () {
            table.search(this.value).draw();
        });
    }

    if (document.getElementById('customPageLength')) {
        document.getElementById('customPageLength').addEventListener('change', function () {
            table.page.len(this.value).draw();
        });
    }



    // Eventos para o checkbox "selecionar todas as permissões"
    if (document.getElementById('tabela-permissoes')) {
        const checkBoxSelecionarTodos = document.getElementById('selecionar-todas-permissoes');
        const checkBoxesPermissoes = document.querySelectorAll('#tabela-permissoes input[type="checkbox"]');

        // Função para atualizar o estado do checkbox "selecionar todos"
        const updateEstadoSelecionarTodos = () => {
            // Converte NodeList para Array para usar o método 'every'
            const allChecked = Array.from(checkBoxesPermissoes).every(checkbox => checkbox.checked);
            checkBoxSelecionarTodos.checked = allChecked;
        };

        // Evento para o checkbox "selecionar todos"
        checkBoxSelecionarTodos.addEventListener('change', (e) => {
            checkBoxesPermissoes.forEach(checkbox => {
                checkbox.checked = e.target.checked;
            });
        });

        // Eventos para os checkboxes individuais de permissão
        checkBoxesPermissoes.forEach(checkbox => {
            checkbox.addEventListener('change', updateEstadoSelecionarTodos);
        });

        // Verifica o estado inicial ao carregar a página
        updateEstadoSelecionarTodos();
    }
});




function excluirGrupoAcesso(idGrupoAcesso) {
    Swal.fire({
        title: "Deseja excluir o grupo de acesso?",
        text: "Voce não poderá reverter isso!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sim!",
        cancelButtonText: "Cancelar"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch("/grupos-acessos/" + idGrupoAcesso + "/excluir", { method: 'DELETE' })
                .then(response => response.text())
                .then(data => {
                    console.log(data);
                    if (data === 'OK') {
                        Swal.fire({
                            title: "Sucesso!",
                            text: "Seu grupo de acesso foi deletado com sucesso!",
                            icon: "success"
                        }).then((result) => {
                            new DataTable('#datatable').ajax.reload();
                        });
                    } else {
                        Swal.fire({
                            title: "Atenção!",
                            text: "Não foi possível deletar o registro.",
                            icon: "warning"
                        });
                    }
                });
        }
    });
}
