package br.cin.ufpe.nlp.util;

import java.util.LinkedList;
import java.util.List;

public class AnnotatedDocument {
	private List<AnnotatedSentence> sentences;
	
	public AnnotatedDocument() {
		this.sentences = new LinkedList<AnnotatedSentence>();
	}
	
	public void addSentence(AnnotatedSentence sentence) {
		this.sentences.add(sentence);
	}

	public List<AnnotatedSentence> getSentences() {
		return sentences;
	}
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		if (sentences.size() > 0) {
			result.append("{\n");
			for (AnnotatedSentence annotatedSentence : sentences) {
				result.append("   ");
				result.append(annotatedSentence.toString());
				result.append("\n");
			}
			result.append("}\n");
		} else result.append("{ }\n");
		return result.toString();
	}

	public void setSentences(List<AnnotatedSentence> sentences) {
		this.sentences = sentences;
	}
}
