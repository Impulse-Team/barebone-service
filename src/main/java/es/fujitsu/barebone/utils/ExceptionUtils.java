package es.fujitsu.barebone.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import es.fujitsu.barebone.exception.FujitsuException;
import es.fujitsu.barebone.exception.FujitsuRuntimeException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ExceptionUtils {

	private static ResourceBundle msgs = ResourceBundle.getBundle("messages.exceptions", Locale.ROOT);

	public static String processMessage(String message) {
		return processMessage(message, new String[] {});
	}

	/**
	 * Procesar el mensaje de una excepcion trayendo el mensaje del properties de
	 * mensajes y formatearlo sustituyendo las variables usando los argumentos
	 * pasados. Si el mensaje proporcionado no es una clave sino el mensaje en si,
	 * se usa ese mismo mensaje sin leer del properties.
	 * 
	 * @param message Mensaje o clave
	 * @param args    Argumentos para usar en el formateo
	 * @return Mensaje procesado
	 */
	public static String processMessage(String message, String[] args) {
		return msgs.containsKey(message) ? new MessageFormat(msgs.getString(message)).format(args)
				: new MessageFormat(message).format(args);
	}

	/**
	 * <p>
	 * Registar en el log la excepcion proporcionada.
	 * </p>
	 * <p>
	 * Se imprime la traza en los siguientes casos:
	 * </p>
	 * <ul>
	 * <li>Log esta en nivel DEBUG</li>
	 * <li>Excepcion no controlada</li>
	 * <li>Es una excepcion controlada configurada para pintar su traza</li>
	 * </ul>
	 * 
	 * @param ex La excepcion a logar
	 */
	public static void log(Exception ex) {
		boolean logLevelDebug = log.isDebugEnabled();
		boolean controlledException = ex instanceof FujitsuException || ex instanceof FujitsuRuntimeException;
		boolean isPrintStackTrace = (ex instanceof FujitsuException && ((FujitsuException) ex).isPrintStacktrace()
				|| (ex instanceof FujitsuRuntimeException && ((FujitsuRuntimeException) ex).isPrintStacktrace()));

		if (logLevelDebug || !controlledException || isPrintStackTrace) {
			log.error(ex.getMessage(), ex);
		} else {
			log.error(ex.getMessage());
		}
	}

}