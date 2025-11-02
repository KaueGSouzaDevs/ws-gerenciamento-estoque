document.addEventListener('DOMContentLoaded', function () {
    let selects = document.querySelectorAll(`.select2`);
    selects.forEach(select2 => {
        $(select2).select2({
            theme: "bootstrap-5",
            placeholder: "Selecione uma opção"
        });
    });
    let selectsMultiple = document.querySelectorAll(`.select2Multiple`);
    selectsMultiple.forEach(select2 => {
        $(select2).select2({
            theme: "bootstrap-5",
            placeholder: "Selecione uma opção"
        });
    });
})
