package br.cin.ufpe.nlp.util;

public enum TokenAnnotation {
	POSTAG("Parts of Speech"), SUPERSENSE("Supersenses"), LEMMA("Word Lemma"), NER("Named entity recognized"), CHAROFFSETBEGIN("Char offset begin");
	
	TokenAnnotation(String description) {
		this.desc = description;
	}
	private String desc;
	
	@Override
	public String toString() {
		return "[" + desc + "]";
	}
	
}
