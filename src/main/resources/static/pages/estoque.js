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



let cleaveTelefoneDinamico;

function setPhoneMask(input) {
    const rawValue = input.value.replace(/\D/g, '');

    // Destroi a instância anterior (se existir)
    if (cleaveTelefoneDinamico) {
        cleaveTelefoneDinamico.destroy();
    }

    // Aplica máscara dinâmica dependendo do comprimento
    if (rawValue.length > 10) {
        // Celular: (99) 99999-9999
        cleaveTelefoneDinamico = new Cleave(input, {
            delimiters: ['(', ') ', '-'],
            blocks: [0, 2, 5, 4],
            numericOnly: true
        });
    } else {
        // Fixo: (99) 9999-9999
        cleaveTelefoneDinamico = new Cleave(input, {
            delimiters: ['(', ') ', '-'],
            blocks: [0, 2, 4, 4],
            numericOnly: true
        });
    }
}


/**
 * Máscaras para CPF e CNPJ na mesma função
 */
let cleaveDoc;

// função que (re)aplica a máscara certa
function setDocMask(input) {
    const raw = input.value.replace(/\D/g, "");
    if (cleaveDoc) cleaveDoc.destroy();

    if (raw.length > 11) {
        // CNPJ: 14 dígitos
        cleaveDoc = new Cleave(input, {
            blocks: [2, 3, 3, 4, 2],
            delimiters: [".", ".", "/", "-"],
            numericOnly: true
        });
    } else {
        // CPF: até 11 dígitos
        cleaveDoc = new Cleave(input, {
            blocks: [3, 3, 3, 2],
            delimiters: [".", ".", "-"],
            numericOnly: true
        });
    }
}


/**
 * Formata um valor numérico para o padrão de moeda brasileira (R$ XX,XX)
 * @param {number} valor - Valor a ser formatado
 * @return {string} Valor formatado como moeda (ex: "12,34")
 */
function formatarParaReais(valor) {
    if (!valor) return '0,00'; // Evita erro caso seja null ou undefined

    return parseFloat(valor)
        .toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}


/**
 * Função para resetar o campo removendo toda a formatação,
 * deixando apenas os dígitos.
 * @param {HTMLInputElement} inputElement - Elemento input que será resetado.
 */
function resetCampo(inputElement) {
    // Remove todos os caracteres que não são dígitos
    inputElement.value = inputElement.value.replace(/\D/g, '');
};

