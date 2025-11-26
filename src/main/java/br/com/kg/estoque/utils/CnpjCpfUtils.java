package br.com.kg.estoque.utils;

public class CnpjCpfUtils {

    private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
    private static final int[] pesoCNPJ = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };



    /**
     * Verifica se um CPF ou CNPJ é válido.
     * 
     * @param cnpjCpf o CPF ou CNPJ a ser verificado
     * @return true se o CPF ou CNPJ for válido, false caso contrário
     */
    public static boolean isValid(String cnpjCpf) {
        return (isValidCPF(cnpjCpf) || isValidCNPJ(cnpjCpf));
    }



    /**
     * Calcula o dígito verificador de um CPF ou CNPJ, com base no peso informado.
     * 
     * @param str a string do CPF ou CNPJ
     * @param peso o peso a ser utilizado
     * @return o dígito verificador calculado
     */
    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }



    /**
     * Preenche a string com um caractere informado.
     * 
     * @param text a string a ser preenchida
     * @param character o caractere a ser utilizado para preencher
     * @return a string preenchida com o caractere informado
     */
    private static String padLeft(String text, char character) {
        return "%11s".formatted(text).replace(' ', character);
    }



    /**
     * Verifica se um CPF é válido.
     * 
     * @param cpf o CPF a ser verificado
     * @return true se o CPF for válido, false caso contrário
     */
    private static boolean isValidCPF(String cpf) {
        cpf = cpf.trim().replaceAll("\\D", "");
        // cpf = cpf.trim().replace(".", "").replace("-", "");
        if ((cpf == null) || (cpf.length() != 11))
            return false;

        for (int j = 0; j < 10; j++)
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf))
                return false;

        Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }



    /**
     * Verifica se um CNPJ é válido.
     * 
     * @param cnpj o CNPJ a ser verificado
     * @return true se o CNPJ for válido, false caso contrário
     */
    private static boolean isValidCNPJ(String cnpj) {
        // cnpj = cnpj.trim().replace(".", "").replace("-", "");
        cnpj = cnpj.trim().replace(".", "").replace("-", "").replace("/", "");
        cnpj = cnpj.trim().replaceAll("\\D", "");
        if ((cnpj == null) || (cnpj.length() != 14))
            return false;

        Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }
}
