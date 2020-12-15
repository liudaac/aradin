package cn.aradin.spring.velocity.ui;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

/**
 * Utility class for working with a VelocityEngine.
 * Provides convenience methods to merge a Velocity template with a model.
 *
 * @author Juergen Hoeller
 * @since 22.01.2004
 * @deprecated as of Spring 4.3, in favor of FreeMarker
 */
@Deprecated
public abstract class VelocityEngineUtils {

	/**
	 * Merge the specified Velocity template with the given model and write
	 * the result to the given Writer.
	 * @param velocityEngine VelocityEngine to work with
	 * @param templateLocation the location of template, relative to Velocity's resource loader path
	 * @param model the Map that contains model names as keys and model objects as values
	 * @param writer the Writer to write the result to
	 * @throws VelocityException if the template wasn't found or rendering failed
	 * @deprecated Use {@link #mergeTemplate(VelocityEngine, String, String, Map, Writer)}
	 * instead, following Velocity 1.6's corresponding deprecation in its own API.
	 */
	@Deprecated
	public static void mergeTemplate(
			VelocityEngine velocityEngine, String templateLocation, Map<String, Object> model, Writer writer)
			throws VelocityException {

		VelocityContext velocityContext = new VelocityContext(model);
		velocityEngine.mergeTemplate(templateLocation, velocityContext, writer);
	}

	/**
	 * Merge the specified Velocity template with the given model and write the result
	 * to the given Writer.
	 * @param velocityEngine VelocityEngine to work with
	 * @param templateLocation the location of template, relative to Velocity's resource loader path
	 * @param encoding the encoding of the template file
	 * @param model the Map that contains model names as keys and model objects as values
	 * @param writer the Writer to write the result to
	 * @throws VelocityException if the template wasn't found or rendering failed
	 */
	public static void mergeTemplate(
			VelocityEngine velocityEngine, String templateLocation, String encoding,
			Map<String, Object> model, Writer writer) throws VelocityException {

		VelocityContext velocityContext = new VelocityContext(model);
		velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
	}

	/**
	 * Merge the specified Velocity template with the given model into a String.
	 * <p>When using this method to prepare a text for a mail to be sent with Spring's
	 * mail support, consider wrapping VelocityException in MailPreparationException.
	 * @param velocityEngine VelocityEngine to work with
	 * @param templateLocation the location of template, relative to Velocity's resource loader path
	 * @param model the Map that contains model names as keys and model objects as values
	 * @return the result as String
	 * @throws VelocityException if the template wasn't found or rendering failed
	 * @deprecated Use {@link #mergeTemplateIntoString(VelocityEngine, String, String, Map)}
	 * instead, following Velocity 1.6's corresponding deprecation in its own API.
	 */
	@Deprecated
	public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation,
			Map<String, Object> model) throws VelocityException {

		StringWriter result = new StringWriter();
		mergeTemplate(velocityEngine, templateLocation, model, result);
		return result.toString();
	}

	/**
	 * Merge the specified Velocity template with the given model into a String.
	 * <p>When using this method to prepare a text for a mail to be sent with Spring's
	 * mail support, consider wrapping VelocityException in MailPreparationException.
	 * @param velocityEngine VelocityEngine to work with
	 * @param templateLocation the location of template, relative to Velocity's resource loader path
	 * @param encoding the encoding of the template file
	 * @param model the Map that contains model names as keys and model objects as values
	 * @return the result as String
	 * @throws VelocityException if the template wasn't found or rendering failed
	 */
	public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation,
			String encoding, Map<String, Object> model) throws VelocityException {

		StringWriter result = new StringWriter();
		mergeTemplate(velocityEngine, templateLocation, encoding, model, result);
		return result.toString();
	}

}
