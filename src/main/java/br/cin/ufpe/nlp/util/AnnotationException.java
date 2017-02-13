package br.cin.ufpe.nlp.util;

public class AnnotationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1639320496233506058L;

	public AnnotationException() {
	}

	public AnnotationException(String message) {
		super(message);
	}

	public AnnotationException(Throwable cause) {
		super(cause);
	}

	public AnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnnotationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
