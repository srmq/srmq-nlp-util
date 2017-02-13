package br.cin.ufpe.nlp.util;

import java.util.LinkedList;
import java.util.List;

public class AnnotatedSentence {
	private List<AnnotatedToken> tokens;
	public AnnotatedSentence() {
		this.tokens = new LinkedList<AnnotatedToken>();
	}
	public void addToken(AnnotatedToken token) {
		this.tokens.add(token);
	}
	public List<AnnotatedToken> getTokens() {
		return tokens;
	}
	@Override
	public String toString() {
		return tokens.toString();
	}
	public void setTokens(List<AnnotatedToken> tokens) {
		this.tokens = tokens;
	}
}
