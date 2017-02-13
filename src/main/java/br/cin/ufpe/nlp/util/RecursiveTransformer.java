package br.cin.ufpe.nlp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.cin.ufpe.nlp.api.transform.DocumentProcessor;
import br.cin.ufpe.nlp.api.transform.DocumentProcessorOneToN;

public class RecursiveTransformer {

	private static ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	private static Logger logger = LoggerFactory.getLogger(RecursiveTransformer.class);
	
	public static synchronized void recursiveProcess(File baseInputPath, final File[] baseOutputPaths,
			final DocumentProcessorOneToN docProcessor, double keepPercent) throws IOException {
		try {
			recursiveProcess(baseInputPath, baseOutputPaths,
					docProcessor, keepPercent, 0);
		} finally {
			try {
				exec.shutdown();
				exec.awaitTermination(100, TimeUnit.DAYS);
				logger.info("Finished recursiveProcess");
				exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("Could not process files", e);
				throw new IllegalStateException("Could not process files", e);
			}
		}
	}
	

	private static void recursiveProcess(File baseInputPath, final File[] baseOutputPaths,
			final DocumentProcessorOneToN docProcessor, double keepPercent, final int level) throws IOException {
		File[] stuffToProcess = baseInputPath.listFiles();
		Set<Integer> elimIndices = elimIndices(keepPercent, stuffToProcess);

		int elimFileIndex = 0;
		for (final File file : stuffToProcess) {
			final String name = file.getName();
			if (file.isDirectory()) {
				File[] newDir = new File[baseOutputPaths.length];
				for (int i = 0; i < baseOutputPaths.length; i++) {
					newDir[i] = new File(baseOutputPaths[i].getCanonicalFile().toString() + File.separator + name);
					newDir[i].mkdir();
				}
				recursiveProcess(file, newDir, docProcessor, keepPercent, level+1);
			} else {
				if (elimIndices == null || !elimIndices.contains(elimFileIndex)) {
					exec.submit(new Runnable() {
						public void run() {
							try {
								processOneFileMultiOutput(baseOutputPaths, docProcessor, file, name);
							} catch (IOException e) {
								throw new IllegalStateException("Error processing file " + file.getAbsolutePath() + "at level " + level, e);
							} catch (Throwable t) {
								logger.error("Error while trying to process file", t);
								throw new IllegalStateException("Error while trying to process file", t);
							}
						}
					});

				}
				elimFileIndex++;
			}
		}
	}

	private static void processOneFileMultiOutput(File[] baseOutputPaths, DocumentProcessorOneToN docProcessor,
			File file, String name) throws FileNotFoundException, IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(file));
		BufferedWriter bufw[] = new BufferedWriter[baseOutputPaths.length];
		for (int i = 0; i < bufw.length; i++) {
			bufw[i] = new BufferedWriter(
					new FileWriter(new File(baseOutputPaths[i].getCanonicalPath() + File.separator + name)));
		}
		docProcessor.processDocument(bufr, bufw);
		bufr.close();
		for (int i = 0; i < bufw.length; i++) {
			bufw[i].close();
		}
	}

	public static void recursiveProcess(File baseInputPath, File baseOutputPath, DocumentProcessor docProcessor,
			double keepPercent) throws IOException {
		File[] stuffToProcess = baseInputPath.listFiles();
		Set<Integer> elimIndices = elimIndices(keepPercent, stuffToProcess);

		int elimFileIndex = 0;
		for (File file : stuffToProcess) {
			final String name = file.getName();
			if (file.isDirectory()) {
				final File newDir = new File(baseOutputPath.getCanonicalFile().toString() + File.separator + name);
				newDir.mkdir();
				recursiveProcess(file, newDir, docProcessor, keepPercent);
			} else {
				if (elimIndices == null || !elimIndices.contains(elimFileIndex)) {
					processOneFile(baseOutputPath, docProcessor, file, name);
				}
				elimFileIndex++;
			}
		}

	}

	private static void processOneFile(File baseOutputPath, DocumentProcessor docProcessor, File file,
			final String name) throws FileNotFoundException, IOException {
		BufferedReader bufr = new BufferedReader(new FileReader(file));
		BufferedWriter bufw = new BufferedWriter(
				new FileWriter(new File(baseOutputPath.getCanonicalPath() + File.separator + name)));
		docProcessor.processDocument(bufr, bufw);
		bufw.close();
		bufr.close();
	}

	private static Set<Integer> elimIndices(double keepPercent, File[] stuffToProcess) {
		int totalFiles = 0;
		Set<Integer> elimIndices = null;
		if (keepPercent < 1.0) {
			for (File file : stuffToProcess) {
				if (file.isFile())
					totalFiles++;
			}
			final int keepFiles = Math.round(totalFiles * (float) keepPercent);
			final int excludeFiles = totalFiles - keepFiles;
			elimIndices = new HashSet<Integer>(excludeFiles);
			while (elimIndices.size() < excludeFiles) {
				final int elimIndex = Math.round((float) Math.random() * totalFiles);
				elimIndices.add(elimIndex);
			}
		}
		return elimIndices;
	}

}
