/**
 *  Copyright 2012 Martynas Jusevičius <martynas@graphity.org>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.graphity.server.model;

import com.hp.hpl.jena.rdf.model.Resource;
import com.sun.jersey.api.core.ResourceConfig;
import javax.ws.rs.core.Request;

/**
 * A factory class for creating Linked Data resources.
 * 
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class LinkedDataResourceFactory
{
    /**
     * Creates new Linked Data resource from explicit URI resource.
     * 
     * @param resource RDF resource
     * @return a new resource
     */
    /*
    public static LinkedDataResource createResource(Resource resource)
    {
	return new LinkedDataResourceBase(resource, null);
    }
    */

    /**
     * Creates new Linked Data resource from explicit URI resource. Provides cache control.
     * 
     * @param resource RDF resource
     * @param request request object
     * @param resourceConfig resource config
     * @return a new resource
     */
    public static LinkedDataResource createResource(Resource resource, Request request, ResourceConfig resourceConfig)
    {
	return new LinkedDataResourceBase(resource, request, resourceConfig);
    }

}
