package org.aradin.spring.velocity.starter.properties;

import java.util.HashMap;
import java.util.Map;

import static org.aradin.spring.velocity.template.VelocityTemplateAvailabilityProvider.*;

import org.aradin.spring.velocity.view.VelocityViewResolver;
import org.springframework.boot.autoconfigure.template.AbstractTemplateViewResolverProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for configuring Velocity.
 *
 * @author Andy Wilkinson
 * @since 1.1.0
 * 4.3
 */
@ConfigurationProperties(prefix = "spring.velocity")
public class VelocityProperties extends AbstractTemplateViewResolverProperties{
	
	/**
	 * Name of the DateTool helper object to expose in the Velocity context of the view.
	 */
	private String dateToolAttribute;

	/**
	 * Name of the NumberTool helper object to expose in the Velocity context of the view.
	 */
	private String numberToolAttribute;

	/**
	 * Additional velocity properties.
	 */
	private Map<String, String> properties = new HashMap<String, String>();

	/**
	 * Template path.
	 */
	private String resourceLoaderPath = DEFAULT_RESOURCE_LOADER_PATH;

	/**
	 * Velocity Toolbox config location, for example "/WEB-INF/toolbox.xml". Automatically
	 * loads a Velocity Tools toolbox definition file and expose all defined tools in the
	 * specified scopes.
	 */
	private String toolboxConfigLocation;

	/**
	 * Prefer file system access for template loading. File system access enables hot
	 * detection of template changes.
	 */
	private boolean preferFileSystemAccess = true;

	public VelocityProperties() {
		super(DEFAULT_PREFIX, DEFAULT_SUFFIX);
	}

	public String getDateToolAttribute() {
		return this.dateToolAttribute;
	}

	public void setDateToolAttribute(String dateToolAttribute) {
		this.dateToolAttribute = dateToolAttribute;
	}

	public String getNumberToolAttribute() {
		return this.numberToolAttribute;
	}

	public void setNumberToolAttribute(String numberToolAttribute) {
		this.numberToolAttribute = numberToolAttribute;
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getResourceLoaderPath() {
		return this.resourceLoaderPath;
	}

	public void setResourceLoaderPath(String resourceLoaderPath) {
		this.resourceLoaderPath = resourceLoaderPath;
	}

	public String getToolboxConfigLocation() {
		return this.toolboxConfigLocation;
	}

	public void setToolboxConfigLocation(String toolboxConfigLocation) {
		this.toolboxConfigLocation = toolboxConfigLocation;
	}

	public boolean isPreferFileSystemAccess() {
		return this.preferFileSystemAccess;
	}

	public void setPreferFileSystemAccess(boolean preferFileSystemAccess) {
		this.preferFileSystemAccess = preferFileSystemAccess;
	}

	@Override
	public void applyToMvcViewResolver(Object viewResolver) {
		super.applyToMvcViewResolver(viewResolver);
		VelocityViewResolver resolver = (VelocityViewResolver) viewResolver;
		resolver.setToolboxConfigLocation(getToolboxConfigLocation());
		resolver.setDateToolAttribute(getDateToolAttribute());
		resolver.setNumberToolAttribute(getNumberToolAttribute());
	}
}
