/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.service;

import com.google.common.base.Strings;
import com.seedlabs.donuts.api.GraphSchemaConstants;
import com.seedlabs.donuts.api.representation.Asset;
import com.seedlabs.donuts.api.representation.Category;
import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.decr;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;

/**
 * Created by mp2526 on 3/15/16.
 */
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    static public ArrayList<Category> getCategories(TitanGraph graph, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Category> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> categories  = g.V().hasLabel(GraphSchemaConstants.VL_CATEGORY);

            while (categories.hasNext()) {
                result.add(Category.loadCategory(categories.next(), fields));
            }
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }

    static public Id postCategory(TitanGraph graph, Category category) throws Exception {
        TitanTransaction tx = graph.newTransaction();
        Id result = new Id();

        try {
            result.setId(java.util.UUID.randomUUID().toString());
            result.setCreatedDate(Instant.now().getEpochSecond());

            Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_CATEGORY);
            vertex.property(GraphSchemaConstants.PK_CATEGORYID, result.getId());
            if(category.getName() != null)
                vertex.property(GraphSchemaConstants.PK_NAME, category.getName());
            vertex.property(GraphSchemaConstants.PK_CREATEDDATE, result.getCreatedDate());

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        }

        return result;
    }
}