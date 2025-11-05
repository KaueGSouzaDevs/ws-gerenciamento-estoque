document.addEventListener('DOMContentLoaded', function () {
    iniciarSelects2();
})

function iniciarSelects2(idModal) {
    if (idModal) {
        $('.select2').select2({
            theme: "bootstrap-5",
            placeholder: "Selecione uma opção",
            width: 'resolve',
            dropdownParent: $('#' + idModal)
        });

        $('.select2Multiple').select2({
            theme: "bootstrap-5",
            placeholder: "Selecione uma opção",
            width: 'resolve',
            dropdownParent: $('#' + idModal)
        });
    } else {
        $('.select2').select2({
            theme: "bootstrap-5",
            placeholder: "Selecione uma opção",
            width: 'resolve',
        });

        $('.select2Multiple').select2({
            theme: "bootstrap-5",
            placeholder: "Selecione uma opção",
            width: 'resolve',
        });
    }
}



function executeScripts(element) {
    Array.from(element.getElementsByTagName("script")).forEach((oldScript) => {
        const newScript = document.createElement("script");
        Array.from(oldScript.attributes)
            .forEach(attr => newScript.setAttribute(attr.name, attr.value));
        newScript.appendChild(document.createTextNode(oldScript.innerHTML));
        oldScript.parentNode.replaceChild(newScript, oldScript);
    });
}
