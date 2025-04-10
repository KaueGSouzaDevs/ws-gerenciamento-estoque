window.addEventListener("load", function(){ 
    $("#datatable").DataTable({
        language: {url: 'https://cdn.datatables.net/plug-ins/1.11.5/i18n/pt-BR.json'},
        processing: true,        
        sAjaxSource: '/movimentos/jsonDataTable',
        bServerSide: true,
        drawCallback: function( settings ) {		    	
        },
        columns: [
            {data: 0}, // id
            {data: 1}, // data
            {data: 2}, // tipo
            {data: 3}, // material
            {data: 4}, // quantidade
            {data: 5}, // responsavel
            {data: 6,  /**ações */  orderable: false, class:"text-center",
                render: function(data, type, row, meta) {
                        return `

                        <a href="/movimentos/${data}/editar" class="btn btn-primary btn-sm">
                            <i class="mdi mdi-pencil me-1"></i> Editar
                        </a>
                        
                        <a href="javascript:excluir(${data});" class="btn btn-danger btn-sm">
                            <i class="mdi mdi-delete me-1"></i> Excluir
                        </a> 

                        `;
                }        
            },
        ],
    });
    $(".dataTables_length select").addClass("form-select form-select-sm");
});



document.getElementById("tipo").addEventListener("change", function(){

    var tipo = this.value;

    if (tipo==="Saida") {
        document.getElementById("notaFiscal").setAttribute('disabled', 'disabled');
        document.getElementById("fornecedor").setAttribute('disabled', 'disabled');
    }else if (tipo==="Entrada") {
        document.getElementById("notaFiscal").removeAttribute('disabled');
        document.getElementById("fornecedor").removeAttribute('disabled');
    }
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