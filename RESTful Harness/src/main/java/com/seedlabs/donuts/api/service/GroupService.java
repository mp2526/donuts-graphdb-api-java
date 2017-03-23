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
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.decr;

/**
 * Created by mp2526 on 3/14/16.
 */
public class GroupService {
    private static final Logger LOG = LoggerFactory.getLogger(GroupService.class);

    static public ArrayList<Group> getGroups(TitanGraph graph, String[] ids, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Group> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> groups;

            if(ids != null && ids.length > 0) {
                groups = g.V().hasLabel(GraphSchemaConstants.VL_GROUP).has(GraphSchemaConstants.PK_GROUPID,
                        P.within((Arrays.asList(ids))));
            } else {
                groups = g.V().hasLabel(GraphSchemaConstants.VL_GROUP);
            }

            while (groups.hasNext()) {
                result.add(Group.loadGroup(groups.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public Id postGroup(TitanGraph graph, Group group) throws Exception {
        TitanTransaction tx = graph.newTransaction();
        Id result = new Id();

        try {
            GraphTraversalSource g = tx.traversal();

            result.setId(java.util.UUID.randomUUID().toString());
            result.setCreatedDate(Instant.now().getEpochSecond());

            Vertex v_group = tx.addVertex(GraphSchemaConstants.VL_GROUP);
            v_group.property(GraphSchemaConstants.PK_GROUPID, result.getId());
            if(group.getName() != null)
                v_group.property(GraphSchemaConstants.PK_NAME, group.getName());
            if(group.getDescription() != null)
                v_group.property(GraphSchemaConstants.PK_DESCRIPTION, group.getDescription());
            if(group.getImage() != null)
                v_group.property(GraphSchemaConstants.PK_IMAGE, group.getImage());
            if(group.getPrivacy() != null)
                v_group.property(GraphSchemaConstants.PK_PRIVACY, group.getPrivacy());
            v_group.property(GraphSchemaConstants.PK_CREATEDDATE, result.getCreatedDate());

            Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                    .has(GraphSchemaConstants.PK_USERID, group.getOwner().getId()).tryNext();
            if (v_user.isPresent()) {
                v_user.get().addEdge(GraphSchemaConstants.EL_OWNS, v_group);
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public Group getGroup(TitanGraph graph, String groupId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        Group result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_GROUP,
                    GraphSchemaConstants.PK_GROUPID, groupId);

            if(gt.hasNext()) {
                result = Group.loadGroup(gt.next(), fields);
            }
        } catch(Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public void putGroup(TitanGraph graph, String groupId, Group group) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_group = g.V().hasLabel(GraphSchemaConstants.VL_GROUP)
                    .has(GraphSchemaConstants.PK_GROUPID, groupId).tryNext();

            if(v_group.isPresent()) {
                Vertex v = v_group.get();
                if(group.getName() != null)
                    v.property(GraphSchemaConstants.PK_NAME, group.getName());
                if(group.getDescription() != null)
                    v.property(GraphSchemaConstants.PK_DESCRIPTION, group.getDescription());
                if(group.getImage() != null)
                    v.property(GraphSchemaConstants.PK_IMAGE, group.getImage());
                if(group.getPrivacy() != null)
                    v.property(GraphSchemaConstants.PK_PRIVACY, group.getPrivacy());

                //Update owner by deleting old owner edge and adding new edge
                if(group.getOwner() != null) {
                    Iterator<Edge> ie = v.edges(Direction.IN, GraphSchemaConstants.EL_OWNS);

                    if (ie.hasNext())
                        ie.next().remove();

                    GraphTraversal<Vertex, Vertex> v_user = g.V().has(GraphSchemaConstants.VL_USER,
                            GraphSchemaConstants.PK_USERID, group.getOwner().getId());

                    if(v_user.hasNext())
                        v_user.next().addEdge(GraphSchemaConstants.EL_OWNS, v);
                }
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }
    }

    static public ArrayList<Activity> getGroupFeed(TitanGraph graph, String groupId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Activity> result = new ArrayList<>();

        try {
            //TODO: fix where activities come from
            GraphTraversal<Vertex, Vertex> feed = g.V().has(GraphSchemaConstants.VL_GROUP,
                    GraphSchemaConstants.PK_GROUPID, groupId).out(GraphSchemaConstants.EL_PERFORMED);

            while (feed.hasNext()) {
                result.add(Activity.loadActivity(feed.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public Id postGroupFeed(TitanGraph graph, String groupId, Post post) throws Exception {
        TitanTransaction tx = graph.newTransaction();
        Id result = new Id();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_group = g.V().hasLabel(GraphSchemaConstants.VL_GROUP)
                    .has(GraphSchemaConstants.PK_GROUPID, groupId).tryNext();

            if(v_group.isPresent()) {
                result.setId(java.util.UUID.randomUUID().toString());
                result.setCreatedDate(Instant.now().getEpochSecond());

                Vertex v_post = tx.addVertex(GraphSchemaConstants.VL_POST);
                v_post.property(GraphSchemaConstants.PK_POSTID, result.getId());
                if(post.getDetails() != null)
                    v_post.property(GraphSchemaConstants.PK_DETAILS, post.getDetails());
                v_post.property(GraphSchemaConstants.PK_CREATEDDATE, result.getCreatedDate());

                v_group.get().addEdge(GraphSchemaConstants.EL_PARENTED, v_post);

                Optional<Vertex> v_user = g.V().hasLabel(GraphSchemaConstants.VL_USER)
                        .has(GraphSchemaConstants.PK_USERID, post.getOwner().getId()).tryNext();

                if(v_user.isPresent()) {
                    v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, v_post);
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

    static public ArrayList<User> getGroupsMembers(TitanGraph graph, String groupId, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<User> result = new ArrayList<>();

        try {
            Optional<Vertex> v_group = g.V().hasLabel(GraphSchemaConstants.VL_GROUP)
                    .has(GraphSchemaConstants.PK_GROUPID, groupId).tryNext();

            if(v_group.isPresent()) {
                Iterator<Vertex> v_members = v_group.get().vertices(Direction.OUT, GraphSchemaConstants.EL_MEMBER);

                while (v_members.hasNext()) {
                    result.add(User.loadUser(v_members.next(), fields));
                }
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public void postGroupsMembers(TitanGraph graph, String groupId, List<User> users) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            Optional<Vertex> v_group = g.V().hasLabel(GraphSchemaConstants.VL_GROUP)
                    .has(GraphSchemaConstants.PK_GROUPID, groupId).tryNext();

            if(v_group.isPresent()) {
                for (User user: users) {
                    Optional<Vertex> v_user = g.V().hasLabel(GraphSchemaConstants.VL_USER)
                            .has(GraphSchemaConstants.PK_USERID, user.getId()).tryNext();

                    if(v_user.isPresent())
                        v_user.get().addEdge(GraphSchemaConstants.EL_MEMBER, v_group.get());
                }
            }

            tx.commit();

        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }
    }

    static public void deleteGroupsMember(TitanGraph graph, String groupId, String memberId) throws Exception {
        TitanTransaction tx = graph.newTransaction();

        try {
            GraphTraversalSource g = tx.traversal();

            //TODO: Fix this, this does nothing at the moment
            GraphTraversal<Vertex, Vertex> group = g.V().has(GraphSchemaConstants.VL_GROUP,
                    GraphSchemaConstants.PK_GROUPID, groupId);

            GraphTraversal<Vertex, Vertex> membership = group.bothE().bothV().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, memberId);


            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            LOG.error("Error: ", ex);
            throw ex;
        }
    }

    static public ArrayList<Group> getNewGroups(TitanGraph graph, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Group> result = new ArrayList<>();

        try {
            //TODO: determine criteria of an "new" group
            GraphTraversal<Vertex, Vertex> groups = g.V().hasLabel(GraphSchemaConstants.VL_GROUP).order()
                    .by(GraphSchemaConstants.PK_CREATEDDATE, decr).limit(10);

            while (groups.hasNext()) {
                result.add(Group.loadGroup(groups.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }

    static public ArrayList<Group> getPopularGroups(TitanGraph graph, ArrayList<String> fields) throws Exception {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Group> result = new ArrayList<>();

        try {
            //TODO: determine criteria of an "popular" group
            GraphTraversal<Vertex, Vertex> groups = g.V().hasLabel(GraphSchemaConstants.VL_GROUP).limit(10);

            while (groups.hasNext()) {
                result.add(Group.loadGroup(groups.next(), fields));
            }
        } catch (Exception ex) {
            LOG.error("Error: ", ex);
            throw ex;
        }

        return result;
    }
}
