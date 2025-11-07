document.addEventListener('DOMContentLoaded', function () {

    let table = new DataTable(`#datatable`, {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        processing: true,
        ajax: {
            url: '/movimentos/jsonDataTable',
            type: 'POST', // ou 'POST'
            contentType: 'application/json',
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        serverSide: true,
        columns: [
            //{ data: 'id' },
            {
                data: 'dataMovimento',
                render: function (data, type, row, meta) {
                    return `${Intl.DateTimeFormat('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' }).format(new Date(data))}`;
                }
            },
            { data: 'tipoMovimento' },
            { data: 'material.nome' },
            { data: 'quantidade' },
            { data: 'responsavel' },
            {
                data: 'id', class: "text-center coluna-acoes", orderable: false, width: "100px",
                render: function (data, type, row, meta) {
                    return `
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="icon-base bx bx-dots-vertical-rounded"></i>
                            </button>
                            <div class="dropdown-menu" style="">
                                <a class="dropdown-item" href="/movimentos/${data}/editar"><i class="icon-base bx bx-pencil"></i> Editar</a>

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



function excluir(idMovimento) {
    Swal.fire({
        title: "Deseja realmente deletar o movimento?",
        text: "Essa ação não poderá ser desfeita!",
        icon: "warning",
        showCancelButton: true,
        cancelButtonText: "Cancelar",
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sim, deletar!"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/movimentos/' + idMovimento + '/excluir', { method: 'DELETE' })
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



function camposPorTipoMovimento(value) {
    let notaFiscal = document.getElementById("notaFiscal");
    let fornecedor = document.getElementById("fornecedor");
    console.log(value);
    if (value === "SAIDA") {
        notaFiscal.setAttribute('disabled', 'true');
        fornecedor.setAttribute('disabled', 'true');
    } else {
        notaFiscal.removeAttribute('disabled');
        fornecedor.removeAttribute('disabled');
    }
}
