package br.com.kg.estoque.custom;

import java.text.Normalizer;

public class Auxiliar {

	private Auxiliar() {
		throw new IllegalStateException("Utility class");
	}

    /**
	 * Remove os acentos de uma String utilizando Normalizer
	 */
	public static String removeAcentos(String string) {
		if (string != null) {
			string = Normalizer.normalize(string, Normalizer.Form.NFD);
			string = string.replaceAll("[^\\p{ASCII}]", "");
		}
		return string;
	}
    
}
