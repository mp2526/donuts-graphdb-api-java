/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.service;

import com.google.common.base.Strings;
import com.seedlabs.donuts.api.GraphSchemaConstants;
import com.seedlabs.donuts.api.representation.*;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

/**
 * Created by mp2526 on 3/7/16.
 */
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(AssetService.class);

    static public ArrayList<User> getUsers(TitanGraph graph, String[] ids, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<User> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> users;

            if(ids != null && ids.length > 0) {
                users = g.V().hasLabel(GraphSchemaConstants.VL_USER).has(GraphSchemaConstants.PK_USERID,
                        P.within((Arrays.asList(ids))));
            } else {
                users = g.V().hasLabel(GraphSchemaConstants.VL_USER);
            }

            while (users.hasNext()) {

                result.add(User.loadUser(users.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public User getUser(TitanGraph graph, String userId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        User result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId);

            if(gt.hasNext()) {
                result = User.loadUser(gt.next(), fields);
            }
        } catch(Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public ArrayList<Activity> getUserFeed(TitanGraph graph, String userId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Activity> result = new ArrayList<>();

        try {
            //TODO: figure out what constitutes a user feed
            GraphTraversal<Vertex, Vertex> feed = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).out(GraphSchemaConstants.EL_PERFORMED);

            while (feed.hasNext()) {
                result.add(Activity.loadActivity(feed.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public Id postUserFeed(TitanGraph graph, String userId, Post post) throws Exception {
        TitanTransaction tx = graph.newTransaction();
        Id result = new Id();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_user = g.V().hasLabel(GraphSchemaConstants.VL_USER)
                    .has(GraphSchemaConstants.PK_USERID, userId).tryNext();

            if(v_user.isPresent()) {
                result.setId(java.util.UUID.randomUUID().toString());
                result.setCreatedDate(Instant.now().getEpochSecond());

                Vertex v_post = tx.addVertex(GraphSchemaConstants.VL_POST);
                v_post.property(GraphSchemaConstants.PK_POSTID, result.getId());
                v_post.property(GraphSchemaConstants.PK_DETAILS, Strings.nullToEmpty(post.getDetails()));
                v_post.property(GraphSchemaConstants.PK_CREATEDDATE, post.getCreatedDate());

                v_user.get().addEdge(GraphSchemaConstants.EL_PARENTED, v_post);

                Optional<Vertex> v_owner = g.V().hasLabel(GraphSchemaConstants.VL_USER)
                        .has(GraphSchemaConstants.PK_USERID, post.getOwner().getId()).tryNext();

                if(v_owner.isPresent()) {
                    v_owner.get().addEdge(GraphSchemaConstants.EL_SHARED, v_post);
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

    static public ArrayList<User> getUserFollows(TitanGraph graph, String userId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<User> result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).outE(GraphSchemaConstants.EL_FOLLOWS).inV().dedup();

            result = new ArrayList<>();

            while (gt.hasNext()) {
                result.add(User.loadUser(gt.next(), fields));
            }


        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public ArrayList<User> getUserFollowedBy(TitanGraph graph, String userId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<User> result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).inE(GraphSchemaConstants.EL_FOLLOWS).outV().dedup();

            result = new ArrayList<>();

            while (gt.hasNext()) {
                result.add(User.loadUser(gt.next(), fields));
            }


        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public ArrayList<Skill> getUserSkills(TitanGraph graph, String userId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Skill> result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).out().dedup().hasLabel(GraphSchemaConstants.VL_SKILL);

            result = new ArrayList<>();

            while (gt.hasNext()) {
                result.add(Skill.loadSkill(gt.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }
}
