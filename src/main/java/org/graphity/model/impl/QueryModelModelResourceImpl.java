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

package org.graphity.model.impl;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import org.graphity.model.query.QueryModelModelResource;
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

    public QueryModelModelResourceImpl(Model queryModel, Query query)
    {
	if (queryModel == null) throw new IllegalArgumentException("Query Model must be not null");
	if (query == null || !(query.isConstructType() || query.isDescribeType())) throw new IllegalArgumentException("Query must be not null and CONSTRUCT or DESCRIBE");
	this.queryModel = queryModel;
	this.query = query;
	if (log.isDebugEnabled()) log.debug("Query Model: {} Query: {}", queryModel, query);
    }

    public QueryModelModelResourceImpl(Model queryModel, String uri)
    {
	this(queryModel, QueryFactory.create("DESCRIBE <" + uri + ">"));
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

}