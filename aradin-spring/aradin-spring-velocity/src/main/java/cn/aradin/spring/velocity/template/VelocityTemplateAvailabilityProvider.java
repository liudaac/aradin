package cn.aradin.spring.velocity.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.template.PathBasedTemplateAvailabilityProvider;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;

/**
 * {@link TemplateAvailabilityProvider} that provides availability information for
 * Velocity view templates.
 *
 * @author Andy Wilkinson
 * @since 1.1.0
 * 4.3
 */
public class VelocityTemplateAvailabilityProvider
		extends PathBasedTemplateAvailabilityProvider {

	public static final String DEFAULT_RESOURCE_LOADER_PATH = "classpath:/templates/";

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = ".vm";
	
	public VelocityTemplateAvailabilityProvider() {
		super("org.apache.velocity.app.VelocityEngine",
				VelocityTemplateAvailabilityProperties.class, "spring.velocity");
	}

	static class VelocityTemplateAvailabilityProperties
			extends TemplateAvailabilityProperties {

		private List<String> resourceLoaderPath = new ArrayList<String>(
				Arrays.asList(DEFAULT_RESOURCE_LOADER_PATH));

		VelocityTemplateAvailabilityProperties() {
			super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
		}

		@Override
		protected List<String> getLoaderPath() {
			return this.resourceLoaderPath;
		}

		public List<String> getResourceLoaderPath() {
			return this.resourceLoaderPath;
		}

		public void setResourceLoaderPath(List<String> resourceLoaderPath) {
			this.resourceLoaderPath = resourceLoaderPath;
		}

	}

}