/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.service;

import com.google.common.base.Strings;
import com.seedlabs.donuts.api.GraphSchemaConstants;
import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by mp2526 on 3/25/16.
 */
public class CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    static public Comment getComment(TitanGraph graph, String commentId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        Comment result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_COMMENT,
                    GraphSchemaConstants.PK_COMMENTID, commentId);

            if(gt.hasNext()) {
                result = Comment.loadComment(gt.next(), fields);
            }
        } catch(Exception ex) {
            throw ex;
        }

        return result;
    }

    static public Id postComment(TitanGraph graph, String commentId, Comment comment) throws Exception {
        TitanTransaction tx = graph.newTransaction();
        Id result = new Id();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_comment = g.V().hasLabel(GraphSchemaConstants.VL_COMMENT)
                    .has(GraphSchemaConstants.PK_COMMENTID, commentId).tryNext();

            if(v_comment.isPresent()) {
                result.setId(java.util.UUID.randomUUID().toString());
                result.setCreatedDate(Instant.now().getEpochSecond());

                Vertex v_reply = tx.addVertex(GraphSchemaConstants.VL_COMMENT);
                v_reply.property(GraphSchemaConstants.PK_COMMENTID, result.getId());
                v_reply.property(GraphSchemaConstants.PK_CREATEDDATE, result.getCreatedDate());
                v_reply.property(GraphSchemaConstants.PK_DETAILS, Strings.nullToEmpty(comment.getDetails()));

                //TODO: Throw error if no owner is provided. i.e. comment.getOwner() == null
                Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                        .has(GraphSchemaConstants.PK_USERID, comment.getOwner().getId()).tryNext();
                if (v_user.isPresent()) {
                    v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, v_reply);
                }

                v_comment.get().addEdge(GraphSchemaConstants.EL_HAS, v_reply);
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        }

        return result;
    }

    static public void putComment(TitanGraph graph, String commentId, Comment comment) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_comment = g.V().hasLabel(GraphSchemaConstants.VL_COMMENT)
                    .has(GraphSchemaConstants.PK_COMMENTID, commentId).tryNext();

            if(v_comment.isPresent()) {
                Vertex v = v_comment.get();
                if(comment.getDetails() != null)
                    v.property(GraphSchemaConstants.PK_DETAILS, comment.getDetails());
                //TODO: finish this
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        }
    }

    static public void deleteComment(TitanGraph graph, String commentId) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_COMMENT,
                    GraphSchemaConstants.PK_COMMENTID, commentId);

            //TODO: Deal with deleting reply comments
            if(gt.hasNext())
                gt.next().remove();

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            throw ex;
        }
    }
}
