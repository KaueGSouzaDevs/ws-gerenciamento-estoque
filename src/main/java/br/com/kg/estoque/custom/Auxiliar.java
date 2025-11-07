package br.com.kg.estoque.custom;

import java.text.Normalizer;

/**
 * Classe utilitária com métodos auxiliares para a aplicação.
 * Esta classe não pode ser instanciada.
 */
public class Auxiliar {

	/**
	 * Construtor privado para prevenir a instanciação da classe utilitária.
	 */
	private Auxiliar() {
		throw new IllegalStateException("Utility class");
	}

    /**
	 * Remove os acentos de uma string.
	 * Utiliza a classe {@link Normalizer} para decompor os caracteres acentuados
	 * e depois remove os caracteres não-ASCII.
	 *
	 * @param string A string de entrada da qual os acentos serão removidos.
	 * @return A string resultante sem acentos, ou a string original se for nula.
	 */
	public static String removeAcentos(String string) {
		if (string != null) {
			string = Normalizer.normalize(string, Normalizer.Form.NFD);
			string = string.replaceAll("[^\\p{ASCII}]", "");
		}
		return string;
	}

	public static boolean isEmptyOrNull(String value) {
		return value == null || value.isEmpty();
	}
    
}
