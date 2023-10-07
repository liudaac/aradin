package org.apache.velocity.tools.config;

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

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.ConversionUtils;

/**
 * Provides support for reading a configuration file from a specified path.
 *
 * @author Nathan Bubna
 * @version $Id: XmlFactoryConfiguration.java 511959 2007-02-26 19:24:39Z nbubna $
 */
public abstract class FileFactoryConfiguration extends FactoryConfiguration
{
    protected FileFactoryConfiguration(Class clazz, String id)
    {
        super(clazz, id);
    }

    /**
     * <p>Reads an configuration from an {@link URL}.</p>
     * 
     * @param url the InputStream to read from
     * @throws IOException if a problem occurs
     */
    protected abstract void readImpl(URL url) throws IOException;

    /**
     * <p>Reads a configuration file from the specified file path
     * and sets up the configuration from that. If the file does not
     * exist, a {@link ResourceNotFoundException} will be thrown.</p>
     *
     * @param path the path to the file to be read from
     */
    public void read(String path)
    {
        read(path, true);
    }

    public void read(URL url)
    {
        read(url, true);
    }

    public void read(String path, boolean required)
    {
        read(path, required, null);
    }

    public void read(URL url, boolean required)
    {
        read(url, required, null);
    }

    public void read(String path, boolean required, Logger log)
    {
        if (path == null)
        {
            throw new NullPointerException("Path value cannot be null");
        }
        if (log != null && log.isTraceEnabled())
        {
            log.trace("Attempting to read configuration file at: {}", path);
        }

        URL url = findURL(path);
        if (url != null)
        {
            read(url, required, log);
        }
        else
        {
            String msg = "Could not find configuration file at: "+path;
            if (log != null)
            {
                log.debug(msg);
            }
            if (required)
            {
                throw new ResourceNotFoundException(msg);
            }
        }
    }

    protected URL findURL(String path)
    {
        return ConversionUtils.toURL(path, this);
    }

    protected void read(URL url, boolean required, Logger log)
    {
        try
        {
            readImpl(url);
            // only add the sources which can be read
            addSource("    .read("+url.toString()+")");
        }
        catch (IOException ioe)
        {
            String msg = "Could not open stream from: "+url;
            if (log != null)
            {
                log.debug(msg, ioe);
            }
            if (required)
            {
                throw new RuntimeException(msg, ioe);
            }
        }
    }
}
