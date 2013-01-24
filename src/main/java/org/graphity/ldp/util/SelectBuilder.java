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
package org.graphity.ldp.util;

import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.topbraid.spin.arq.ARQ2SPIN;
import org.topbraid.spin.model.Query;
import org.topbraid.spin.model.SPINFactory;
import org.topbraid.spin.model.Select;
import org.topbraid.spin.model.Variable;
import org.topbraid.spin.vocabulary.SP;

/**
 * SPARQL SELECT query builder based on SPIN RDF syntax
 * 
 * @author Martynas Jusevičius <martynas@graphity.org>
 * @see QueryBuilder
 * @see <a href="http://www.w3.org/Submission/2011/SUBM-spin-sparql-20110222/">SPIN - SPARQL Syntax</a>
 * @see <a href="http://topbraid.org/spin/api/1.2.0/spin/apidocs/org/topbraid/spin/model/Select.html">SPIN Select</a>
 */
public class SelectBuilder extends QueryBuilder implements Select
{
    private static final Logger log = LoggerFactory.getLogger(SelectBuilder.class);

    private Select select = null;

    /**
     * Constructs builder from SPIN query
     * 
     * @param query SPIN SELECT resource
     */
    protected SelectBuilder(Select select)
    {
	super(select);
	this.select = select;
    }

    public static SelectBuilder fromSelect(Select select)
    {
	return new SelectBuilder(select);
    }

    public static SelectBuilder fromQuery(org.topbraid.spin.model.Query query)
    {
	return fromSelect((Select)query);
    }

    public static SelectBuilder fromResource(Resource resource)
    {
	if (resource == null) throw new IllegalArgumentException("Select Resource cannot be null");
	
	Query query = SPINFactory.asQuery(resource);
	if (query == null || !(query instanceof Select))
	    throw new IllegalArgumentException("SelectBuilder Resource must be a SPIN SELECT Query");

	return fromSelect((Select)query);
    }

    public static SelectBuilder fromQuery(com.hp.hpl.jena.query.Query query, String uri, Model model)
    {
	if (query == null) throw new IllegalArgumentException("Query cannot be null");
	
	ARQ2SPIN arq2spin = new ARQ2SPIN(model);
	return fromQuery(arq2spin.createQuery(query, uri));
    }

    public static SelectBuilder fromQuery(com.hp.hpl.jena.query.Query query, Model model)
    {
	return fromQuery(query, null, model);
    }

    public static SelectBuilder fromQueryString(String queryString, Model model)
    {
	return fromQuery(QueryFactory.create(queryString), model);
    }

    /**
     * SPIN SELECT resource
     * 
     * @return the query resource of this builder
     */
    @Override
    protected Select getQuery()
    {
	return select;
    }


    public SelectBuilder limit(Long limit)
    {
	if (limit == null) throw new IllegalArgumentException("LIMIT cannot be null");

	if (log.isTraceEnabled()) log.trace("Setting LIMIT param: {}", limit);
	
	removeAll(SP.limit).
	    addLiteral(SP.limit, limit);
	
	return this;
    }

    public SelectBuilder offset(Long offset)
    {
	if (offset == null) throw new IllegalArgumentException("OFFSET cannot be null");
	
	if (log.isTraceEnabled()) log.trace("Setting OFFSET param: {}", offset);
	
	removeAll(SP.offset)
	    .addLiteral(SP.offset, offset);
	
	return this;
    }

    public SelectBuilder orderBy(String varName)
    {	
	return orderBy(varName, false);
    }

    public SelectBuilder orderBy(String varName, Boolean desc)
    {
	if (varName != null)
	    return orderBy(SPINFactory.createVariable(getModel(), varName), desc);
	else    
	    return orderBy((Variable)null, desc);
    }

    public SelectBuilder orderBy(Resource var)
    {
	if (var == null) throw new IllegalArgumentException("ORDER BY resource cannot be null");

	return orderBy(SPINFactory.asVariable(var), false);
    }

    public SelectBuilder orderBy(Resource var, Boolean desc)
    {
	if (var == null) throw new IllegalArgumentException("ORDER BY resource cannot be null");

	return orderBy(SPINFactory.asVariable(var), desc);
    }

    public SelectBuilder orderBy(Variable var)
    {
	return orderBy(var, false);
    }
    
    public SelectBuilder orderBy(Variable var, Boolean desc)
    {
	if (var == null) throw new IllegalArgumentException("ORDER BY variable cannot be null");
	if (desc == null) throw new IllegalArgumentException("DESC cannot be null");
	
	if (log.isTraceEnabled()) log.trace("Setting ORDER BY variable: {}", var);
	removeAll(SP.orderBy);

	Resource bnode = getModel().createResource().addProperty(SP.expression, var);
	addProperty(SP.orderBy, getModel().createList(new RDFNode[]{bnode}));

	if (desc)
	    bnode.addProperty(RDF.type, SP.Desc);
	else
	    bnode.addProperty(RDF.type, SP.Asc);
	
	return this;
    }

    @Override
    public List<Resource> getResultVariables()
    {
	return getQuery().getResultVariables();
    }

    @Override
    public boolean isDistinct()
    {
	return getQuery().isDistinct();
    }

    @Override
    public boolean isReduced()
    {
	return getQuery().isReduced();
    }

    @Override
    public Long getLimit()
    {
	return getQuery().getLimit();
    }

    @Override
    public Long getOffset()
    {
	return getQuery().getOffset();
    }

}