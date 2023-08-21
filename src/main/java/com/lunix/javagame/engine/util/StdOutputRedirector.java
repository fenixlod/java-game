package com.lunix.javagame.engine.util;

import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to redirect all messages printing in the console to the
 * log files.
 * 
 * @author vladislav.todorov
 *
 */
public class StdOutputRedirector {
	private final static Logger logger = LogManager.getLogger(StdOutputRedirector.class);

	public static void redirectSystemOutAndErrToLog4j() {
		System.setOut(createInfoLoggingProxy(System.out));
		System.setErr(createErrorLoggingProxy(System.err));
	}

	public static PrintStream createInfoLoggingProxy(final PrintStream realPrintStream) {
		return new PrintStream(realPrintStream) {

			@Override
			public void print(final String string) {
				logger.info(string);
			}

			@Override
			public void println(final String string) {
				logger.info(string);
			}

			@Override
			public void println(final Object obj) {
				logger.info(obj.toString());
			}
		};
	}

	public static PrintStream createErrorLoggingProxy(final PrintStream realPrintStream) {
		return new PrintStream(realPrintStream) {

			@Override
			public void print(final String string) {
				logger.error(string);
			}

			@Override
			public void println(final String string) {
				logger.error(string);
			}

			@Override
			public void println(final Object obj) {
				logger.error(obj.toString());
			}
		};
	}
}
