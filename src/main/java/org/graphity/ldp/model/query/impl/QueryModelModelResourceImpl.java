/*
 * Copyright (C) 2012 Martynas Jusevičius <martynas@graphity.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graphity.ldp.model.query.impl;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import javax.ws.rs.core.*;
import org.graphity.ldp.model.query.QueryModelModelResource;
import org.graphity.util.ModelUtils;
import org.graphity.util.manager.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martynas Jusevičius <martynas@graphity.org>
 */
public class QueryModelModelResourceImpl implements QueryModelModelResource
{
    private static final Logger log = LoggerFactory.getLogger(QueryModelModelResourceImpl.class);

    private Query query = null;
    private Model queryModel, model = null;
    private Request req = null;
    private UriInfo uriInfo = null;
    private MediaType mediaType = org.graphity.MediaType.APPLICATION_RDF_XML_TYPE;

    public QueryModelModelResourceImpl(Model queryModel, Query query, 
	UriInfo uriInfo, Request req,
	MediaType mediaType)
    {
	if (queryModel == null) throw new IllegalArgumentException("Query Model must be not null");
	if (query == null) throw new IllegalArgumentException("Query must be not null");
	this.queryModel = queryModel;
	this.query = query;
	this.req = req;
	this.uriInfo = uriInfo;
	if (mediaType != null) this.mediaType = mediaType;
	
	if (log.isDebugEnabled()) log.debug("Query Model: {} Query: {}", queryModel, query);
    }

    public QueryModelModelResourceImpl(Model queryModel, String uri,
	UriInfo uriInfo, Request req,
	MediaType mediaType)
    {
	this(queryModel, QueryFactory.create("DESCRIBE <" + uri + ">"), uriInfo, req, mediaType);
    }

    @Override
    public Model getModel()
    {
	if (model == null)
	{
	    if (log.isDebugEnabled()) log.debug("Querying Model: {} with Query: {}", getQueryModel(), getQuery());
	    model = DataManager.get().loadModel(getQueryModel(), getQuery());
	    
	    if (log.isDebugEnabled()) log.debug("Number of Model stmts read: {}", model.size());
	}

	return model;
    }

    @Override
    public Query getQuery()
    {
	return query;
    }

    @Override
    public Model getQueryModel()
    {
	return queryModel;
    }

    @Override
    public UriInfo getUriInfo()
    {
	return uriInfo;
    }

    @Override
    public Request getRequest()
    {
	return req;
    }
    
    @Override
    public Response getResponse()
    {
	if (log.isDebugEnabled()) log.debug("Accept param: {}, writing SPARQL results (XML or JSON)", getMediaType());

	return Response.ok(getModel(), getMediaType()).build();
    }
    
    public MediaType getMediaType()
    {
	return mediaType;
    }

    @Override
    public EntityTag getEntityTag()
    {
	return new EntityTag(Long.toHexString(ModelUtils.hashModel(getModel())));
    }

}