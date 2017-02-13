package br.cin.ufpe.nlp.util;

import java.util.Map;
import java.util.TreeMap;

public class AnnotatedToken {
	protected String tokenText;
	protected Map<TokenAnnotation, String> annotations;
	
	public AnnotatedToken() { }
	
	public AnnotatedToken(String tokenText) {
		this.tokenText = tokenText;
		this.annotations = new TreeMap<TokenAnnotation, String>();
	}
	
	public String addAnnotation(TokenAnnotation label, String value) {
		return this.annotations.put(label, value);
	}

	public String getTokenText() {
		return tokenText;
	}

	public Map<TokenAnnotation, String> getAnnotations() {
		return annotations;
	}
	@Override
	public String toString() {
		return this.tokenText + annotations.toString();
	}

	public void setTokenText(String tokenText) {
		this.tokenText = tokenText;
	}

	public void setAnnotations(Map<TokenAnnotation, String> annotations) {
		this.annotations = annotations;
	}
}
