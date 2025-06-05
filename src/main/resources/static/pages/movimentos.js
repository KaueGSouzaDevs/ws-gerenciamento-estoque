document.addEventListener("DOMContentLoaded", function () {
    let table = new DataTable('#datatable', {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        processing: true,
        ajax: {
            url: '/movimentos/jsonDataTable',
            type: 'GET' // ou 'POST'
        },
        serverSide: true,
        columns: [
            { data: 0 }, // id
            { data: 1 }, // data
            { data: 2 }, // tipo
            { data: 3 }, // material
            { data: 4 }, // quantidade
            { data: 5 }, // responsavel
            {
                data: 6, class: "text-center coluna-acoes", orderable: false, width: "100px",
                render: function (data, type, row, meta) {
                    return `
                        <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown" aria-expanded="false"><i class="icon-base bx bx-dots-vertical-rounded"></i></button>
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


if (document.getElementById("tipo")) {
    document.getElementById("tipo").addEventListener("change", function () {

        var tipo = this.value;

        if (tipo === "Saida") {
            document.getElementById("notaFiscal").setAttribute('disabled', 'disabled');
            document.getElementById("fornecedor").setAttribute('disabled', 'disabled');
        } else if (tipo === "Entrada") {
            document.getElementById("notaFiscal").removeAttribute('disabled');
            document.getElementById("fornecedor").removeAttribute('disabled');
        }
    });
};

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
