package org.apache.velocity.tools;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.velocity.tools.config.SkipSetters;

/**
 * Manages data needed to create instances of a tool. New instances
 * are returned for every call to create(obj).
 *
 * @author Nathan Bubna
 * @author <a href="mailto:henning@schmiedehausen.org">Henning P. Schmiedehausen</a>
 * @version $Id: ToolInfo.java 511959 2007-02-26 19:24:39Z nbubna $
 */
public class ToolInfo implements java.io.Serializable
{
    private static final long serialVersionUID = -8145087882015742757L;
    public static final String CONFIGURE_METHOD_NAME = "configure";

    private String key;
    private Class clazz;
    private Class factory;
    private transient Method create;
    private boolean restrictToIsExact;
    private String restrictTo;
    private Map<String,Object> properties;
    private Boolean skipSetters;
    private transient Method configure = null;

    /**
     * Creates a new instance using the minimum required info
     * necessary for a tool.
     * @param key tool key
     * @param clazz tool class
     */
    public ToolInfo(String key, Class clazz)
    {
        this(key, clazz, null);
    }

    /**
     * Creates a new instance using the minimum required info
     * necessary for a tool.
     * @param key tool key
     * @param clazz tool class
     */
    public ToolInfo(String key, Class clazz, Class factory)
    {
        setKey(key);
        setClass(clazz);
        setFactory(factory);
    }


    /***********************  Mutators *************************/

    /**
     * Set the tool key
     * @param key tool key
     */
    public void setKey(String key)
    {
        this.key = key;
        if (this.key == null)
        {
            throw new NullPointerException("Key cannot be null");
        }
    }

    /**
     * Set the tool class
     * @param clazz the java.lang.Class of the tool
     */
    public void setClass(Class clazz)
    {
        if (clazz == null)
        {
            throw new NullPointerException("Tool class must not be null");
        }
        this.clazz = clazz;

        //NOTE: we used to check here that we could get an instance of
        //      the tool class, but that's been moved to ToolConfiguration
        //      in order to fail as earlier as possible.  most people won't
        //      manually create ToolInfo.  if they do and we can't get an
        //      instance, they should be capable of figuring out what happened
    }

    /**
     * <p>Set the factory class used to create tool instances.</p>
     * <p>The factory is supposed to have one of those three methods:</p>
     * <ul>
     *     <li>create<i>ToolClassName</i>()</li>
     *     <li>new<i>ToolClassName</i>()</li>
     *     <li>get<i>ToolClassName</i>()</li>
     * </ul>
     * <p>where <i>ToolClassName</i> is the tool's class name.</p>
     * <p>If this method takes one <code>java.util.Map</code> argument, it will be given the tool's configuration map.</p>
     *
     * @param factory factory class
     */
    public void setFactory(Class factory)
    {
        this.factory = factory;
    }

    /**
     * @param path the full or partial request path restriction of the tool
     */
    public void restrictTo(String path)
    {
        if (path != null && !path.startsWith("/"))
        {
            path = "/" + path;
        }

        if (path == null || path.equals("*"))
        {
            // match all paths
            restrictToIsExact = false;
            this.restrictTo = null;
        }
        else if(path.endsWith("*"))
        {
            // match some paths
            restrictToIsExact = false;
            this.restrictTo = path.substring(0, path.length() - 1);
        }
        else
        {
            // match one path
            restrictToIsExact = true;
            this.restrictTo = path;
        }
    }

    /**
     * Set whether or not to skip setters.
     * @param cfgOnly flag value
     */
    public void setSkipSetters(boolean cfgOnly)
    {
        this.skipSetters = cfgOnly;
    }

    /**
     * Adds a map of properties from a parent scope to the properties
     * for this tool.  Only new properties will be added; any that
     * are already set for this tool will be ignored.
     * @param parentProps parent properties map
     */
    public void addProperties(Map<String,Object> parentProps)
    {
        // only add those new properties for which we
        // do not already have a value. first prop set wins.
        Map<String,Object> properties = getProps();
        for (Map.Entry<String,Object> prop : parentProps.entrySet())
        {
            if (!properties.containsKey(prop.getKey()))
            {
                properties.put(prop.getKey(), prop.getValue());
            }
        }
    }

    /**
     * Puts a new property for this tool.
     * @param name property name
     * @param value property value
     * @return previous property value
     */
    public Object putProperty(String name, Object value)
    {
        return getProps().put(name, value);
    }

    /**
     * Get tools property (synchronized version)
     * @return tools property
     */
    protected synchronized Map<String,Object> getProps()
    {
        if (properties == null)
        {
            properties = new HashMap<String,Object>();
        }
        return properties;
    }


    /***********************  Accessors *************************/

    /**
     * Get tool key
     * @return tool key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Get tool class name
     * @return tool class name
     */
    public String getClassname()
    {
        return clazz.getName();
    }

    /**
     * Get tool class
     * @return tool class
     */
    public Class getToolClass()
    {
        return clazz;
    }

    /**
     * Get factory class
     * @return factory class or null if not provided
     */
    public Class getFactory()
    {
        return factory;
    }

    /**
     * Get tool properties
     * @return tools properties
     */
    public Map<String,Object> getProperties()
    {
        return getProps();
    }

    /**
     * Get whether this tool has a <code>configure()</code> method
     * @return <code>true</code> if the tool has a <code>configure()</code> method, <code>false</code> otherwise
     */
    public boolean hasConfigure()
    {
        return (getConfigure() != null);
    }

    /**
     * Get whether setters are to be skipped
     * @return whether to skip setters
     */
    public boolean isSkipSetters()
    {
        if (skipSetters == null)
        {
            skipSetters = (clazz.getAnnotation(SkipSetters.class) != null);
        }
        return skipSetters;
    }

    /**
     * @param path the path of a template requesting this tool
     * @return <code>true</code> if the specified
     *         request path matches the restrictions of this tool.
     *         If there is no request path restriction for this tool,
     *         it will always return <code>true</code>.
     */
    public boolean hasPermission(String path)
    {
        if (this.restrictTo == null)
        {
            return true;
        }
        else if (restrictToIsExact)
        {
            return this.restrictTo.equals(path);
        }
        else if (path != null)
        {
            return path.startsWith(this.restrictTo);
        }
        return false;
    }


    /***********************  create() *************************/

    /**
     * Returns a new instance of the tool. If the tool
     * has an configure(Map) method, the new instance
     * will be initialized using the given properties combined with
     * whatever "constant" properties have been put into this
     * ToolInfo.
     * @param dynamicProperties map of dynamic properties
     * @return newly created and configured object
     */
    public Object create(Map<String,Object> dynamicProperties)
    {
        /* Get the tool instance */
        Object tool = newInstance();

        /* put configured props into the combo last, since
           dynamic properties will almost always be conventions
           and we need to let configuration win out */
        Map<String,Object> props;
        if (properties == null)
        {
            props = dynamicProperties;
        }
        else
        {
            props = combine(dynamicProperties, properties);
        }

        // perform the actual configuration of the new tool
        configure(tool, props);
        return tool;
    }


    /***********************  protected methods *************************/

    /**
     * Actually performs configuration of the newly instantiated tool
     * using the combined final set of configuration properties. First,
     * if the class lacks the {@link SkipSetters} annotation, then any
     * specific setters matching the configuration keys are called, then
     * the general configure(Map) method (if any) is called.
     * @param tool newly created tool to be configured
     * @param configuration properties
     */
    protected void configure(Object tool, Map<String,Object> configuration)
    {
        if (!isSkipSetters() && configuration != null)
        {
            try
            {
                // look for specific setters
                for (Map.Entry<String,Object> conf : configuration.entrySet())
                {
                    setProperty(tool, conf.getKey(), conf.getValue());
                }
            }
            catch (RuntimeException re)
            {
                throw re;
            }
            catch (Exception e)
            {
                // convert to a runtime exception, and re-throw
                throw new RuntimeException(e);
            }
        }

        if (hasConfigure())
        {
            invoke(getConfigure(), tool, configuration);
        }
    }

    /**
     * Try to find a <code>configure()</code> method.
     * @return <code>configure()</code> method if found, <code>null</code>otherwise.
     */
    protected Method getConfigure()
    {
        if (this.configure == null)
        {
            // search for a configure(Map params) method in the class
            try
            {
                this.configure = ClassUtils.findMethod(clazz, CONFIGURE_METHOD_NAME,
                                              new Class[]{ Map.class });
            }
            catch (SecurityException se)
            {
                // fail early, rather than wait until
                String msg = "Unable to gain access to '" +
                             CONFIGURE_METHOD_NAME + "(Map)'" +
                             " method for '" + clazz.getName() +
                             "' under the current security manager."+
                             "  This tool cannot be properly configured for use.";
                throw new IllegalStateException(msg, se);
            }
        }
        return this.configure;
    }

    /* TODO? if we have performance issues with copyProperties,
             look at possibly finding and caching these common setters
                setContext(VelocityContext)
                setVelocityEngine(VelocityEngine)
                setLog(Log)
                setLocale(Locale)
             these four are tricky since we may not want servlet deps here
                setRequest(ServletRequest)
                setSession(HttpSession)
                setResponse(ServletResponse)
                setServletContext(ServletContext)    */

    /**
     * Creates a new instance for this tool.
     * @return newly created tool
     * @throws IllegalStateException if creation failed
     */
    protected Object newInstance()
    {
        try
        {
            Class factory = getFactory();
            if (factory == null)
            {
                return clazz.newInstance();
            }
            else
            {
                Method factoryMethod = ClassUtils.findFactoryMethod(factory, clazz);
                return factoryMethod.invoke(null, new Object[] {});
            }
        }
        /* we shouldn't get either of these exceptions here because
         * we already got an instance of this class during setClass().
         * but to be safe, let's catch them and re-throw as RuntimeExceptions */
        catch (IllegalAccessException iae)
        {
            String message = "Unable to instantiate instance of \"" +
                  getClassname() + "\"";
            throw new IllegalStateException(message, iae);
        }
        catch (InstantiationException | InvocationTargetException e)
        {
            String message = "Exception while instantiating instance of \"" +
                  getClassname() + "\"";
            throw new IllegalStateException(message, e);
        }

    }

    /**
     * Invoke a single argument method on a tool
     * @param method the method to invoke
     * @param tool the tool on which to invoke the method
     * @param param the method argument
     * @throws IllegalStateException if invocation failed
     */
    protected void invoke(Method method, Object tool, Object param)
    {
        try
        {
            // call the setup method on the instance
            method.invoke(tool, new Object[]{ param });
        }
        catch (IllegalAccessException iae)
        {
            String msg = "Unable to invoke " + method + " on " + tool;
            // restricting access to this method by this class ist verboten
            throw new IllegalStateException(msg, iae);
        }
        catch (InvocationTargetException ite)
        {
            String msg = "Exception when invoking " + method + " on " + tool;
            // convert to a runtime exception, and re-throw
            throw new RuntimeException(msg, ite.getCause());
        }
    }

    /**
     * Set a property on a tool instance
     * @param tool tool instance
     * @param name property name
     * @param value property value
     * @throws Exception if setting the property throwed
     */
    protected void setProperty(Object tool, String name, Object value) throws Exception
    {
        if (PropertyUtils.isWriteable(tool, name))
        {
            //TODO? support property conversion here?
            //      heavy-handed way is BeanUtils.copyProperty(...)
            PropertyUtils.setProperty(tool, name, value);
        }
    }

    //TODO? move to Utils?
    /**
     * Combine several property maps
     * @param maps maps to combine
     * @return combined map
     */
    protected Map<String,Object> combine(Map<String,Object>... maps)
    {
        Map<String,Object> combined = new HashMap<String,Object>();
        for (Map<String,Object> map : maps)
        {
            if (map != null)
            {
                combined.putAll(map);
            }
        }
        return combined;
    }

}
