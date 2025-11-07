document.addEventListener('DOMContentLoaded', function () {

    let table = new DataTable(`#datatable`, {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        processing: true,
        ajax: {
            url: `/categorias/dataTable`,
            type: 'POST',
            contentType: 'application/json',
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        serverSide: true,
        columns: [
            //  { data: 'id', visible: false }, // id
            { data: 'nome' }, // nome
            { data: 'situacao', width: "200px" }, // situação
            {
                data: 'id', class: "text-center coluna-acoes", orderable: false, width: "100px", // ações
                render: function (data, type, row, meta) {
                    return `

                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown" aria-expanded="false"><i class="icon-base bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu" style="">
                                <a class="dropdown-item" href="javascript:editar(${data});"><i class="icon-base bx bx-pencil"></i> Editar</a>

                                <a class="dropdown-item" href="javascript:excluir(${data});"><i class="icon-base bx bx-trash"></i> Excluir</a>
                            </div>
                        </div>
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



function novaCategoria() {
    fetch('/categorias/novo')
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
            iniciarSelects2('modal-categoria');
        });

    document.getElementById('titulo-modal').innerHTML = 'Adicionar Categoria';
    var modal = new bootstrap.Modal(document.getElementById('modal-categoria'));
    modal.show();
}



function salvarCategoria() {
    fetch('/categorias/salvar', {
        method: 'POST',
        body: new FormData(document.getElementById('formulario'))
    })
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
            iniciarSelects2('modal-categoria');
            executeScripts(bodyModal);
        });
}



function editar(idCategoria) {
    fetch('/categorias/' + idCategoria + '/editar')
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
            iniciarSelects2('modal-categoria');
        });

    document.getElementById('titulo-modal').innerHTML = 'Editar Categoria';
    var modal = new bootstrap.Modal(document.getElementById('modal-categoria'));

    modal.show();
}



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
