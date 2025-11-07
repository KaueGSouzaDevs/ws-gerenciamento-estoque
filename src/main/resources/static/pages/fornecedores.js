document.addEventListener("DOMContentLoaded", function () {

    let table = new DataTable(`#datatable`, {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        processing: true,
        ajax: {
            url: `/fornecedores/jsonDataTable`,
            type: 'POST', // ou 'POST'
            contentType: 'application/json',
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        serverSide: true,
        columns: [
            // { data: 'id' }, // id
            { data: 'nome' }, // nome
            { data: 'cnpjCpf' }, // cnpjCpf
            { data: 'telefone' }, // telefone
            { data: 'email' }, // email
            { data: 'contato' }, // contato
            { data: 'situacao' }, // situação
            {
                data: 'id', class: "text-center coluna-acoes", orderable: false, width: "100px", // acoes
                render: function (data, type, row, meta) {
                    return `
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown" aria-expanded="false"><i class="icon-base bx bx-dots-vertical-rounded"></i></button>
                            <div class="dropdown-menu" style="">
                                <a class="dropdown-item" href="/fornecedores/${data}/editar"><i class="icon-base bx bx-pencil"></i> Editar</a>

                                <a class="dropdown-item" href="javascript:excluir(${data});"><i class="icon-base bx bx-trash"></i> Excluir</a>
                            </div>
                        </div>
                        `;
                }
            },
        ],
        dom: 'rt' +
            '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>'
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

// Função para excluir um fornecedor
function excluir(idFornecedor) {

    Swal.fire({
        title: "Tem certeza que deseja excluir o fornecedor?",
        text: "Voce não poderá reverter isso!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {

            fetch("/fornecedores/" + idFornecedor + "/excluir", { method: 'DELETE' })
                .then(response => response.text())
                .then(retorno => {
                    if (retorno == 'OK') {
                        Swal.fire({
                            title: "Sucesso!",
                            text: "Seu fornecedor foi deletado com sucesso!",
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
