window.addEventListener("DOMContentLoaded", function () {

    let table = new DataTable('#datatable', {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        ajax: {
            url: `/categorias/dataTable`,
            data: function (d) {
                // Converter os parâmetros novos para o formato legado
                return {
                    iDisplayStart: d.start,
                    iDisplayLength: d.length,
                    sSortDir_0: d.order && d.order[0] ? d.order[0].dir : 'asc',
                    iSortCol_0: d.order && d.order[0] ? d.order[0].column : 0,
                    sSearch: d.search ? d.search.value : ""
                };
            },
            dataSrc: function (json) {
                return json.data;
            }
        },
        bServerSide: true,
        columns: [
            { data: 'id' },
            { data: 'nome' },
            { data: 'situacao' },
            {
                data: 'id', class: "text-center", orderable: false,
                render: function (data, type, row, meta) {
                    return `
                        <a href="javascript:editar(${data});" class="btn btn-primary btn-sm">
                            <i class="mdi mdi-pencil me-1"></i> Editar
                        </a>
                        
                        <a href="javascript:excluir(${data});" class="btn btn-danger btn-sm">
                            <i class="mdi mdi-delete me-1"></i> Excluir
                        </a>
                        `;
                }
            },
        ],
        dom: 'rt' +
            '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>',
    });

    //? Campo de busca customizado
    if (document.getElementById('customSearchBox')) {
        document.getElementById('customSearchBox').addEventListener('input', function () {
            table.search(this.value).draw();
        });
    };


    //? Seletor de registros por página customizado
    if (document.getElementById('customPageLength')) {
        document.getElementById('customPageLength').addEventListener('change', function () {
            table.page.len(this.value).draw();
        });
    };

});

// executa os scripts que estão na página de retorno do formulário
function executeScripts(element) {
    Array.from(element.getElementsByTagName("script")).forEach((oldScript) => {
        const newScript = document.createElement("script");
        Array.from(oldScript.attributes)
            .forEach(attr => newScript.setAttribute(attr.name, attr.value));
        newScript.appendChild(document.createTextNode(oldScript.innerHTML));
        oldScript.parentNode.replaceChild(newScript, oldScript);
    });
}

// Invoca o formulário de categorias na janela modal
document.getElementById('btn-adicionar').addEventListener('click', function () {
    fetch('/categorias/novo')
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
        });

    document.getElementById('titulo-modal').innerHTML = 'Adicionar Categoria';
    var modal = new bootstrap.Modal(document.getElementById('modal-categoria'));
    modal.show();
});

// salva a categoria
document.getElementById('btn-salvar').addEventListener('click', function () {
    fetch('/categorias/salvar', {
        method: 'POST',
        body: new FormData(document.getElementById('formulario'))
    })
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
            executeScripts(bodyModal);
        });
});

// chama a modal com formulário para alterar uma categoria
function editar(idCategoria) {
    fetch('/categorias/' + idCategoria + '/editar')
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
        });

    document.getElementById('titulo-modal').innerHTML = 'Editar Categoria';
    var modal = new bootstrap.Modal(document.getElementById('modal-categoria'));

    modal.show();
}

// Exclui uma categoria
function excluir(idCategoria) {
    Swal.fire({
        title: "Deseja realmente deletar a categoria?",
        text: "Essa ação não poderá ser desfeita!",
        icon: "warning",
        showCancelButton: true,
        cancelButtonText: "Cancelar",
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sim, deletar!"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/categorias/' + idCategoria + '/excluir', { method: 'DELETE' })
                .then(response => response.text())
                .then(retorno => {
                    if (retorno == 'OK') {
                        Swal.fire({
                            title: "Sucesso!",
                            text: "Registro excluído com sucesso!",
                            icon: "success"
                        }).then((result) => {
                            window.location.reload();
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
