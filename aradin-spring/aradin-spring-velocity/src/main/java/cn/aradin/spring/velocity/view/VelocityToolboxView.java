package cn.aradin.spring.velocity.view;

import java.lang.reflect.Method;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ViewToolContext;
import org.apache.velocity.tools.view.ViewToolManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * {@link VelocityView} subclass which adds support for Velocity Tools toolboxes
 * and Velocity Tools ViewTool callbacks / Velocity Tools 1.3 init methods.
 *
 * <p>Specify a "toolboxConfigLocation", for example "/WEB-INF/toolbox.xml",
 * to automatically load a Velocity Tools toolbox definition file and expose
 * all defined tools in the specified scopes. If no config location is
 * specified, no toolbox will be loaded and exposed.
 *
 * <p>This view will always create a special Velocity context, namely an
 * instance of the ChainedContext class which is part of the view package
 * of Velocity tools. This allows to use tools from the view package of
 * Velocity Tools, like LinkTool, which need to be initialized with a special
 * context that implements the ViewContext interface (i.e. a ChainedContext).
 *
 * <p>This view also checks tools that are specified as "toolAttributes":
 * If they implement the ViewTool interface, they will get initialized with
 * the Velocity context. This allows tools from the view package of Velocity
 * Tools, such as LinkTool, to be defined as
 * {@link #setToolAttributes "toolAttributes"} on a VelocityToolboxView,
 * instead of in a separate toolbox XML file.
 *
 * <p>This is a separate class mainly to avoid a required dependency on
 * the view package of Velocity Tools in {@link VelocityView} itself.
 * As of Spring 3.0, this class requires Velocity Tools 1.3 or higher.
 *
 * @author Juergen Hoeller
 * @since 1.1.3
 * @see #setToolboxConfigLocation
 * @see #initTool
 * @see org.apache.velocity.tools.view.ViewToolContext
 */
public class VelocityToolboxView extends VelocityView {

	private String toolboxConfigLocation;


	/**
	 * Set a Velocity Toolbox config location, for example "/WEB-INF/toolbox.xml",
	 * to automatically load a Velocity Tools toolbox definition file and expose
	 * all defined tools in the specified scopes. If no config location is
	 * specified, no toolbox will be loaded and exposed.
	 * <p>The specified location string needs to refer to a ServletContext
	 * resource, as expected by ServletToolboxManager which is part of
	 * the view package of Velocity Tools.
	 * @param toolboxConfigLocation toolboxConfigLocation
	 */
	public void setToolboxConfigLocation(String toolboxConfigLocation) {
		this.toolboxConfigLocation = toolboxConfigLocation;
	}

	/**
	 * Return the Velocity Toolbox config location, if any.
	 * @return toolboxConfigLocation
	 */
	protected String getToolboxConfigLocation() {
		return this.toolboxConfigLocation;
	}


	/**
	 * Overridden to create a ChainedContext, which is part of the view package
	 * of Velocity Tools, as special context. ChainedContext is needed for
	 * initialization of ViewTool instances.
	 * @see #initTool
	 */
	@Override
	protected Context createVelocityContext(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (getToolboxConfigLocation() != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("初始化Context "+getToolboxConfigLocation());
			}
			ViewToolManager manager = new ViewToolManager(getServletContext(), false, false);
			manager.configure(toolboxConfigLocation);
			VelocityContext velocityContext = new VelocityContext(model, manager.createContext());
			return velocityContext;
		}
		return new ViewToolContext(getVelocityEngine(), request, response, getServletContext());
//		ViewToolContext context = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());
//		if (getToolboxConfigLocation() != null) {
//			ViewToolManager vManager = new ViewToolManager(getServletContext(), false, false);
//			vManager.configure(toolboxConfigLocation);
//			vManager.setVelocityEngine(getVelocityEngine());
//			return vManager.createContext(request, response);
//		}
//		return context;
	}

	/**
	 * Overridden to check for the ViewContext interface which is part of the
	 * view package of Velocity Tools. This requires a special Velocity context,
	 * like ChainedContext as set up by {@link #createVelocityContext} in this class.
	 */
	@Override
	protected void initTool(Object tool, Context velocityContext) throws Exception {
		// Velocity Tools 1.3: a class-level "init(Object)" method.
		Method initMethod = ClassUtils.getMethodIfAvailable(tool.getClass(), "init", Object.class);
		if (initMethod != null) {
			ReflectionUtils.invokeMethod(initMethod, tool, velocityContext);
		}
	}

}
