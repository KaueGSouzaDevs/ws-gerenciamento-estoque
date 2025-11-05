document.addEventListener('DOMContentLoaded', function () {
    let table = new DataTable(`#datatable`, {
        language: { url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json' },
        processing: true,
        ajax: {
            url: `/materiais/jsonDataTable`,
            type: 'GET' // ou 'POST'
        },
        serverSide: true,
        columns: [
            { data: 0 }, // id
            { data: 1 }, // nome
            { data: 2 }, // categoria
            { data: 3 }, // fabricante
            { data: 4 }, // fornecedor
            {
                data: 5,
                render: function (data, type, row, meta) {
                    return `R$ ${Intl.NumberFormat('pt-BR').format(data)}`;
                }
            }, // preco
            { data: 6 }, // saldo
            { data: 7 }, // status
            {
                data: 8, class: "text-center coluna-acoes", orderable: false, width: "100px",
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
})



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

// Escuta o click no botão para invocar o formulário de materiais na janela modal
document.getElementById('btn-adicionar').addEventListener('click', function () {
    fetch('/materiais/novo')//faz a requisição para o servidor para pegar o formulário
        .then(response => response.text()) // pega o texto da resposta
        .then(retorno => { // pega o texto e coloca em uma variável
            var bodyModal = document.getElementById('corpo-modal'); // pega o corpo da modal
            bodyModal.innerHTML = retorno; // Insere o formulário no corpo da modal
        });

    document.getElementById('titulo-modal').innerHTML = 'Adicionar Material';//altera o título da modal
    var modal = new bootstrap.Modal(document.getElementById('modal-material'));// pega a modal
    modal.show();// mostra a modal

});

// salva a material
document.getElementById('btn-salvar').addEventListener('click', function () {
    fetch('/materiais/salvar', {
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


// chama a modal com formulário para alterar uma material
function editar(idMaterial) {
    fetch('/materiais/' + idMaterial + '/editar')
        .then(response => response.text())
        .then(retorno => {
            var bodyModal = document.getElementById('corpo-modal');
            bodyModal.innerHTML = retorno;
        });

    document.getElementById('titulo-modal').innerHTML = 'Editar Material';
    var modal = new bootstrap.Modal(document.getElementById('modal-material'));

    modal.show();
}

function excluir(idMaterial) {
    Swal.fire({
        title: "Deseja realmente deletar o material?",
        text: "Essa ação não poderá ser desfeita!",
        icon: "warning",
        showCancelButton: true,
        cancelButtonText: "Cancelar",
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Sim, deletar!"
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('/materiais/' + idMaterial + '/excluir', { method: 'DELETE' })
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
