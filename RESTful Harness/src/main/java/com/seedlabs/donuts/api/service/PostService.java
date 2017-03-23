/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.service;

import com.seedlabs.donuts.api.GraphSchemaConstants;
import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.representation.Post;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by mp2526 on 3/7/16.
 */
public class PostService {
    private static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    static public ArrayList<Post> getPosts(TitanGraph graph, String[] ids, String userId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Post> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> posts;

            if(ids != null && ids.length > 0) {
                //Get posts with requested ids
                posts = g.V().hasLabel(GraphSchemaConstants.VL_POST).has(GraphSchemaConstants.PK_POSTID,
                        P.within((Arrays.asList(ids))));
            } else {
                //Get posts of supplied userId
                posts = g.V().has(GraphSchemaConstants.VL_USER, GraphSchemaConstants.PK_USERID, userId)
                        .hasLabel(GraphSchemaConstants.VL_POST);
            }

            while (posts.hasNext()) {
                result.add(Post.loadPost(posts.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public Post getPost(TitanGraph graph, String postId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        Post result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_POST,
                    GraphSchemaConstants.PK_POSTID, postId);

            if(gt.hasNext()) {
                result = Post.loadPost(gt.next(), fields);
            }
        } catch(Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public void putPost(TitanGraph graph, String postId, Post post) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_post = g.V().hasLabel(GraphSchemaConstants.VL_POST)
                    .has(GraphSchemaConstants.PK_POSTID, postId).tryNext();

            if(v_post.isPresent()) {
                Vertex v = v_post.get();
                if(post.getDetails() != null)
                    v.property(GraphSchemaConstants.PK_DETAILS, post.getDetails());
                //TODO: finish this
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }
    }

    static public void deletePost(TitanGraph graph, String postId) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_POST,
                    GraphSchemaConstants.PK_POSTID, postId);

            if(gt.hasNext())
                gt.next().remove();

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }
    }

    static public ArrayList<Comment> getPostComments(TitanGraph graph, String postId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Comment> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_POST,
                    GraphSchemaConstants.PK_POSTID, postId).outV().hasLabel(GraphSchemaConstants.VL_COMMENT);

            while (gt.hasNext()) {
                result.add(Comment.loadComment(gt.next(), fields));
            }
        } catch(Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public Id postComment(TitanGraph graph, String postId, Comment comment) throws Exception {
        TitanTransaction tx = graph.newTransaction();
        Id result = new Id();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_post = g.V().hasLabel(GraphSchemaConstants.VL_POST)
                    .has(GraphSchemaConstants.PK_POSTID, postId).tryNext();

            if(v_post.isPresent()) {
                result.setId(java.util.UUID.randomUUID().toString());
                result.setCreatedDate(Instant.now().getEpochSecond());

                Vertex v_comment = tx.addVertex(GraphSchemaConstants.VL_COMMENT);
                v_comment.property(GraphSchemaConstants.PK_COMMENTID, result.getId());
                if(comment.getDetails() != null)
                    v_comment.property(GraphSchemaConstants.PK_DETAILS, comment.getDetails());
                v_comment.property(GraphSchemaConstants.PK_CREATEDDATE, result.getCreatedDate());

                v_post.get().addEdge(GraphSchemaConstants.EL_HAS, v_comment);

                GraphTraversal<Vertex, Vertex> v_user = g.V().has(GraphSchemaConstants.VL_USER,
                        GraphSchemaConstants.PK_USERID, comment.getOwner().getId());

                if(v_user.hasNext()) {
                    v_user.next().addEdge(GraphSchemaConstants.EL_SHARED, v_comment);
                }
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }
}
