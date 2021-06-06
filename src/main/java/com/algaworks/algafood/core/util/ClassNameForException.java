package com.algaworks.algafood.core.util;

public class ClassNameForException {
	
	public static String getNameClass(Object classe) {
		return classe.getClass().getSimpleName().toLowerCase();
	}	

}
