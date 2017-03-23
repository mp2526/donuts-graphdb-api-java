/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import com.seedlabs.donuts.api.models.*;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.thinkaurelius.titan.util.stats.MetricManager;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.seedlabs.donuts.api.GraphSchemaConstants.*;

/**
 * Created by mp2526 on 2/26/16.
 */
public class DonutsGraphFactory {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int BATCH_SIZE = 10;
    private static final Logger LOG = LoggerFactory.getLogger(DonutsGraphFactory.class);



    public static final MetricRegistry REGISTRY = MetricManager.INSTANCE.getRegistry();
    public static final ConsoleReporter REPORTER = ConsoleReporter.forRegistry(REGISTRY).build();
    private static final String TIMER_LINE = "DonutGraphFactory.line";
    private static final String TIMER_CREATE = "DonutGraphFactory.create_";
    private static final String COUNTER_GET = "DonutGraphFactory.get_";

    private static final AtomicInteger COMPLETED_TASK_COUNT = new AtomicInteger(0);
    private static final int POOL_SIZE = 10;

    public static void load(final TitanGraph graph, boolean loadDataIfSchemaExisted) throws Exception {
        LOG.info("DonutGraphFactory.load - started");

        boolean schemaExisted = loadSchema(graph);

        LOG.info("DonutGraphFactory.load - start data loading");

        if(!schemaExisted || (loadDataIfSchemaExisted && schemaExisted))
            loadData(graph);

        LOG.info("DonutGraphFactory.load - finish data loading");

        LOG.info("DonutGraphFactory.load complete");
    }

    private static boolean loadSchema(final TitanGraph graph)  throws Exception {
        LOG.info("DonutGraphFactory.load - start schema definition");
        TitanManagement mgmt = graph.openManagement();

        if (mgmt.getPropertyKey(GraphSchemaConstants.PK_SCHEMAREADY) != null) {
            mgmt.rollback();
            return true;
        }

        //declare vertex label
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_ASSET).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_CATEGORY).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_COMMENT).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_CONTACT).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_FAVORITE).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_GROUP).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_LINKPREVIEW).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_POST).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_SKILL).make();
        mgmt.makeVertexLabel(GraphSchemaConstants.VL_USER).make();

        //declare properties
        final PropertyKey assetIdKey = mgmt.makePropertyKey(PK_ASSETID).dataType(String.class).make();
        final PropertyKey categoryIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_CATEGORYID).dataType(String.class).make();
        final PropertyKey commentIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_COMMENTID).dataType(String.class).make();
        final PropertyKey contactIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_CONTACTID).dataType(String.class).make();
        final PropertyKey favoriteIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_FAVORITEID).dataType(String.class).make();
        final PropertyKey groupIdKey = mgmt.makePropertyKey(PK_GROUPID).dataType(String.class).make();
        final PropertyKey linkPreviewIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_LINKPREVIEWID).dataType(String.class).make();
        final PropertyKey postIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_POSTID).dataType(String.class).make();
        final PropertyKey skillIdKey = mgmt.makePropertyKey(GraphSchemaConstants.PK_SKILLID).dataType(String.class).make();
        final PropertyKey userIdKey = mgmt.makePropertyKey(PK_USERID).dataType(String.class).make();

        mgmt.makePropertyKey(GraphSchemaConstants.PK_ACTION).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_CREATEDDATE).dataType(Long.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_DESCRIPTION).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_DETAILS).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_DIVISION).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_FILENAME).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_FIRSTNAME).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_IMAGE).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_JOBTITLE).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_LASTNAME).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_LIKECOUNT).dataType(Integer.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_LOCATION).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_MIMETYPE).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_MODIFIEDDATE).dataType(Long.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_NAME).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_OPENCOUNT).dataType(Integer.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_PARENTTYPE).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_PRIVACY).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_SIZE).dataType(Float.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_STARCOUNT).dataType(Integer.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_THUMBNAIL).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_TITLE).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_TYPE).dataType(Integer.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_URL).dataType(String.class).make();
        mgmt.makePropertyKey(GraphSchemaConstants.PK_VALUE).dataType(String.class).make();

        //build composite indexes
        mgmt.buildIndex("AssetIdCompositeIdx", Vertex.class).addKey(assetIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("CategoryIdCompositeIdx", Vertex.class).addKey(categoryIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("CommentIdCompositeIdx", Vertex.class).addKey(commentIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("ContactIdCompositeIdx", Vertex.class).addKey(contactIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("FavoriteIdCompositeIdx", Vertex.class).addKey(favoriteIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("GroupIdCompositeIdx", Vertex.class).addKey(groupIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("LinkPreviewIdCompositeIdx", Vertex.class).addKey(linkPreviewIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("PostIdCompositeIdx", Vertex.class).addKey(postIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("SkillIdCompositeIdx", Vertex.class).addKey(skillIdKey).unique().buildCompositeIndex();
        mgmt.buildIndex("UserIdCompositeIdx", Vertex.class).addKey(userIdKey).unique().buildCompositeIndex();

        //declare edge label
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_HAS).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_CATEGORIZED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_CONTAINS).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_FOLLOWS).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_FAVORITED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_LIKED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_LINK).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_MEMBER).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_MODIFIED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_OBTAINED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_OWNS).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_PARENTED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_PERFORMED).multiplicity(Multiplicity.MULTI).make();
        mgmt.makeEdgeLabel(GraphSchemaConstants.EL_SHARED).multiplicity(Multiplicity.MULTI).make();

        mgmt.makePropertyKey(GraphSchemaConstants.PK_SCHEMAREADY).dataType(String.class).make();

        mgmt.commit();

        LOG.info("DonutGraphFactory.load - finished schema definition");

        return false;
    }

    private static void loadData (final TitanGraph graph)  throws Exception {
        ClassLoader classLoader = DonutsGraphFactory.class.getClassLoader();

        try(Reader reader = new InputStreamReader(classLoader.getResourceAsStream("data/Categories-final.json"), "UTF-8")) {
            Category[] categories = (new GsonBuilder().create()).fromJson(reader, Category[].class);

            TitanTransaction tx = graph.newTransaction();

            int catCount = categories.length;
            LOG.info(String.format("DonutGraphFactory.load - start categories (%d)", catCount));
            for (Category category: categories) {
                createCategory(tx, category);
                LOG.info(String.format("DonutGraphFactory.load - Category created (%1$d/%2$d)", categories.length, catCount--));
            }
            LOG.info("DonutGraphFactory.load - finish categories");

            tx.commit();
        }

        try(Reader reader = new InputStreamReader(classLoader.getResourceAsStream("data/Skills-final.json"), "UTF-8")) {
            Skill[] skills = (new GsonBuilder().create()).fromJson(reader, Skill[].class);

            TitanTransaction tx = graph.newTransaction();

            int skillCount = skills.length;
            LOG.info(String.format("DonutGraphFactory.load - start skills (%d)", skillCount));
            for (Skill skill: skills) {
                createSkill(tx, skill);
                LOG.info(String.format("DonutGraphFactory.load - Skill created (%1$d/%2$d)", skills.length, skillCount--));
            }
            LOG.info("DonutGraphFactory.load - finish skills");

            tx.commit();
        }

        Group[] groups;
        try(Reader reader = new InputStreamReader(classLoader.getResourceAsStream("data/Groups-final.json"), "UTF-8")) {
            groups = (new GsonBuilder().create()).fromJson(reader, Group[].class);

            TitanTransaction tx = graph.newTransaction();

            int groupCount = groups.length;
            LOG.info(String.format("DonutGraphFactory.load - start groups (%d)", groupCount));
            for (Group group: groups) {
                createGroup(tx, group);
                LOG.info(String.format("DonutGraphFactory.load - Group created (%1$d/%2$d)", groups.length, groupCount--));
            }
            LOG.info("DonutGraphFactory.load - finish groups");

            tx.commit();
        }

        try(Reader reader = new InputStreamReader(classLoader.getResourceAsStream("data/Users-final.json"), "UTF-8")) {
            User[] users = (new GsonBuilder().create()).fromJson(reader, User[].class);

            TitanTransaction tx;

            int userCount = users.length;
            LOG.info(String.format("DonutGraphFactory.load - start users (%d)", userCount));
            for (User user: users) {
                tx = graph.newTransaction();
                createUser(tx, user);
                tx.commit();
                LOG.info(String.format("DonutGraphFactory.load - User created (%1$d/%2$d)", users.length, userCount--));
            }
            LOG.info("DonutGraphFactory.load - finish users");

            userCount = users.length;
            LOG.info(String.format("DonutGraphFactory.load - process users (%d)", userCount));
            for (User user: users) {
                tx = graph.newTransaction();

                Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                        .has(PK_USERID, user.id).tryNext();

                if(v_user.isPresent()) {
                    for (User follow : user.following) {
                        Optional<Vertex> v_follows = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                                .has(PK_USERID, follow.id).tryNext();

                        if (v_follows.isPresent())
                            v_user.get().addEdge(GraphSchemaConstants.EL_FOLLOWS, v_follows.get());
                    }
                }

                if(user.posts != null) {
                    int postCount = user.posts.size();
                    LOG.info(String.format("DonutGraphFactory.load - start posts (%d)", postCount));
                    for (Post post : user.posts) {
                        createPost(tx, post);
                        LOG.info(String.format("DonutGraphFactory.load - Post created (%1$d/%2$d)", user.posts.size(), postCount--));
                    }
                    LOG.info("DonutGraphFactory.load - finish posts");
                }

                if(user.assets != null) {
                    int assetCount = user.assets.size();
                    LOG.info(String.format("DonutGraphFactory.load - start assets (%d)", assetCount));
                    for (Asset asset : user.assets) {
                        createAsset(tx, asset);
                        LOG.info(String.format("DonutGraphFactory.load - Asset created (%1$d/%2$d)", user.assets.size(), assetCount--));
                    }
                    LOG.info("DonutGraphFactory.load - finish assets");
                }
                LOG.info(String.format("DonutGraphFactory.load - User processed (%1$d/%2$d)", users.length, userCount--));
                tx.commit();
            }
            LOG.info("DonutGraphFactory.load - finish users");
        }


        TitanTransaction tx = graph.newTransaction();

        int groupCount = groups.length;
        LOG.info(String.format("DonutGraphFactory.load - add owners to groups (%d)", groupCount));
        for (Group group: groups) {
            Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                    .has(PK_USERID, group.ownerId).tryNext();

            if(v_user.isPresent()) {
                Optional<Vertex> v_group = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_GROUP)
                        .has(PK_GROUPID, group.id).tryNext();
                if (v_group.isPresent()) {
                    v_user.get().addEdge(GraphSchemaConstants.EL_OWNS, v_group.get());
                }
            }
            LOG.info(String.format("DonutGraphFactory.load - Owner added to group (%1$d/%2$d)", groups.length, groupCount--));
        }
        LOG.info("DonutGraphFactory.load - finish adding owners to groups");

        tx.commit();
    }

    private static Vertex createAsset(TitanTransaction tx, Asset asset) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_ASSET);
        vertex.property(PK_ASSETID, asset.id);
        vertex.property(GraphSchemaConstants.PK_MIMETYPE, Strings.nullToEmpty(asset.mimeType));
        vertex.property(GraphSchemaConstants.PK_NAME, Strings.nullToEmpty(asset.name));
        vertex.property(GraphSchemaConstants.PK_DESCRIPTION, Strings.nullToEmpty(asset.description));
        vertex.property(GraphSchemaConstants.PK_THUMBNAIL, Strings.nullToEmpty(asset.thumbnail));
        vertex.property(GraphSchemaConstants.PK_FILENAME, Strings.nullToEmpty(asset.fileName));
        vertex.property(GraphSchemaConstants.PK_URL, Strings.nullToEmpty(asset.url));
        vertex.property(GraphSchemaConstants.PK_SIZE, asset.size);
        vertex.property(GraphSchemaConstants.PK_LIKECOUNT, asset.likeCount);
        vertex.property(GraphSchemaConstants.PK_OPENCOUNT, asset.openCount);
        vertex.property(GraphSchemaConstants.PK_STARCOUNT, asset.starCount);
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, asset.createdDate);
        vertex.property(GraphSchemaConstants.PK_MODIFIEDDATE, asset.modifiedDate);

        Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                .has(PK_USERID, asset.ownerId).tryNext();

        if (v_user.isPresent())
            v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, vertex);

        v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                .has(PK_USERID, asset.modifierId).tryNext();

        if (v_user.isPresent())
            v_user.get().addEdge(GraphSchemaConstants.EL_MODIFIED, vertex);

        if (asset.parentType == 1) {
            vertex.property(GraphSchemaConstants.PK_PARENTTYPE, GraphSchemaConstants.USER);

            v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                    .has(PK_USERID, asset.parentId).tryNext();

            if (v_user.isPresent())
                v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, vertex);
        } else if (asset.parentType == 2) {
            vertex.property(GraphSchemaConstants.PK_PARENTTYPE, GraphSchemaConstants.GROUP);

            Optional<Vertex> v_group = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_GROUP)
                    .has(PK_GROUPID, asset.parentId).tryNext();

            if (v_group.isPresent())
                v_group.get().addEdge(GraphSchemaConstants.EL_CONTAINS, vertex);
        }

        if(asset.categories != null) {
            for (Category category : asset.categories) {
                Optional<Vertex> v_category = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_CATEGORY)
                        .has(GraphSchemaConstants.PK_CATEGORYID, category.id).tryNext();

                if (v_category.isPresent()) {
                    vertex.addEdge(GraphSchemaConstants.EL_CATEGORIZED, v_category.get());
                }
            }
        }

        if(asset.comments != null) {
            for (Comment comment: asset.comments) {
                Vertex v_comment = createComment(tx, comment);

                vertex.addEdge(GraphSchemaConstants.EL_HAS, v_comment);
            }
        }

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.ASSET).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.ASSET).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createCategory(TitanTransaction tx, Category category) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_CATEGORY);
        vertex.property(GraphSchemaConstants.PK_CATEGORYID, category.id);
        vertex.property(GraphSchemaConstants.PK_NAME, Strings.nullToEmpty(category.name));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, category.createdDate);

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.CATEGORY).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.CATEGORY).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createComment(TitanTransaction tx, Comment comment) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_COMMENT);
        vertex.property(GraphSchemaConstants.PK_COMMENTID, comment.id);
        vertex.property(GraphSchemaConstants.PK_DETAILS, Strings.nullToEmpty(comment.details));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, comment.createdDate);

        Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                .has(PK_USERID, comment.ownerId).tryNext();

        if (v_user.isPresent())
            v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, vertex);

        if(comment.comments != null) {
            for (Comment c: comment.comments) {
                Vertex v_comment = createComment(tx, c);

                vertex.addEdge(GraphSchemaConstants.EL_HAS, v_comment);
            }
        }

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.COMMENT).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.COMMENT).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createContact(TitanTransaction tx, Contact contact) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_CONTACT);
        vertex.property(GraphSchemaConstants.PK_CONTACTID, contact.id);
        vertex.property(GraphSchemaConstants.PK_TYPE, contact.type);
        vertex.property(GraphSchemaConstants.PK_VALUE, Strings.nullToEmpty(contact.value));
        if(contact.createdDate != null)
            vertex.property(GraphSchemaConstants.PK_CREATEDDATE, contact.createdDate);

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.CONTACT).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.CONTACT).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createFavorite(TitanTransaction tx, Favorite favorite) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_FAVORITE);
        vertex.property(GraphSchemaConstants.PK_FAVORITEID, favorite.id);
        vertex.property(GraphSchemaConstants.PK_NAME, Strings.nullToEmpty(favorite.name));
        vertex.property(GraphSchemaConstants.PK_URL, Strings.nullToEmpty(favorite.url));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, favorite.createdDate);

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.FAVORITE).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.FAVORITE).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createGroup(TitanTransaction tx, Group group) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_GROUP);
        vertex.property(PK_GROUPID, group.id);
        vertex.property(GraphSchemaConstants.PK_NAME, Strings.nullToEmpty(group.name));
        vertex.property(GraphSchemaConstants.PK_DESCRIPTION, Strings.nullToEmpty(group.description));
        vertex.property(GraphSchemaConstants.PK_IMAGE, Strings.nullToEmpty(group.image));
        vertex.property(GraphSchemaConstants.PK_PRIVACY, Strings.nullToEmpty(group.privacy));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, group.createdDate);

        if(group.tags != null) {
            for (Category category : group.tags) {
                Optional<Vertex> v_category = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_CATEGORY)
                        .has(GraphSchemaConstants.PK_CATEGORYID, category.id).tryNext();

                if (v_category.isPresent()) {
                    vertex.addEdge(GraphSchemaConstants.EL_CATEGORIZED, v_category.get());
                }
            }
        }

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.GROUP).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.GROUP).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createLinkPreview(TitanTransaction tx, LinkPreview linkPreview) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_LINKPREVIEW);
        vertex.property(GraphSchemaConstants.PK_LINKPREVIEWID, linkPreview.id);
        vertex.property(GraphSchemaConstants.PK_TITLE, Strings.nullToEmpty(linkPreview.title));
        vertex.property(GraphSchemaConstants.PK_URL, Strings.nullToEmpty(linkPreview.url));
        vertex.property(GraphSchemaConstants.PK_IMAGE, Strings.nullToEmpty(linkPreview.imageUrl));
        vertex.property(GraphSchemaConstants.PK_DESCRIPTION, Strings.nullToEmpty(linkPreview.description));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, linkPreview.createdDate);

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.LINKPREVIEW).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.LINKPREVIEW).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createPost(TitanTransaction tx, Post post) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_POST);
        vertex.property(GraphSchemaConstants.PK_POSTID, post.id);
        vertex.property(GraphSchemaConstants.PK_DETAILS, Strings.nullToEmpty(post.details));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, post.createdDate);

        Optional<Vertex> v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                .has(PK_USERID, post.ownerId).tryNext();

        if (v_user.isPresent())
            v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, vertex);

        if (post.parentType == 1) {
            vertex.property(GraphSchemaConstants.PK_PARENTTYPE, GraphSchemaConstants.USER);

            v_user = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_USER)
                    .has(PK_USERID, post.parentId).tryNext();

            if (v_user.isPresent())
                v_user.get().addEdge(GraphSchemaConstants.EL_SHARED, vertex);
        } else if (post.parentType == 2) {
            vertex.property(GraphSchemaConstants.PK_PARENTTYPE, GraphSchemaConstants.GROUP);

            Optional<Vertex> v_group = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_GROUP)
                    .has(PK_GROUPID, post.parentId).tryNext();

            if (v_group.isPresent())
                v_group.get().addEdge(GraphSchemaConstants.EL_PARENTED, vertex);
        }

        if(post.linkPreviews != null) {
            for (LinkPreview linkPreview : post.linkPreviews) {
                Vertex v_linkPreview = createLinkPreview(tx, linkPreview);

                vertex.addEdge(GraphSchemaConstants.EL_LINK, v_linkPreview);
            }
        }

        if(post.comments != null) {
            for (Comment comment: post.comments) {
                Vertex v_comment = createComment(tx, comment);

                vertex.addEdge(GraphSchemaConstants.EL_HAS, v_comment);
            }
        }

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.POST).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.POST).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createSkill(TitanTransaction tx, Skill skill) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_SKILL);
        vertex.property(GraphSchemaConstants.PK_SKILLID, skill.id);
        vertex.property(GraphSchemaConstants.PK_NAME, Strings.nullToEmpty(skill.name));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, skill.createdDate);

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.SKILL).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.SKILL).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }

    private static Vertex createUser(TitanTransaction tx, User user) {
        long start = System.currentTimeMillis();

        Vertex vertex = tx.addVertex(GraphSchemaConstants.VL_USER);
        vertex.property(PK_USERID, user.id);
        vertex.property(GraphSchemaConstants.PK_NAME, Strings.nullToEmpty(user.name));
        vertex.property(GraphSchemaConstants.PK_FIRSTNAME, Strings.nullToEmpty(user.firstName));
        vertex.property(GraphSchemaConstants.PK_LASTNAME, Strings.nullToEmpty(user.lastName));
        vertex.property(GraphSchemaConstants.PK_JOBTITLE, Strings.nullToEmpty(user.jobTitle));
        vertex.property(GraphSchemaConstants.PK_CREATEDDATE, user.createdDate);
        vertex.property(GraphSchemaConstants.PK_DIVISION, Strings.nullToEmpty(user.division));
        vertex.property(GraphSchemaConstants.PK_DESCRIPTION, Strings.nullToEmpty(user.description));
        vertex.property(GraphSchemaConstants.PK_LOCATION, Strings.nullToEmpty(user.location));
        vertex.property(GraphSchemaConstants.PK_IMAGE, Strings.nullToEmpty(user.image));

        if(user.contacts != null) {
            for (Contact contact: user.contacts) {
                Vertex v_contact = createContact(tx, contact);

                vertex.addEdge(GraphSchemaConstants.EL_HAS, v_contact);
            }
        }

        if(user.skills != null) {
            for (Skill skill : user.skills) {
                Optional<Vertex> v_skill = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_SKILL)
                        .has(GraphSchemaConstants.PK_SKILLID, skill.id).tryNext();

                if (v_skill.isPresent())
                    vertex.addEdge(GraphSchemaConstants.EL_OBTAINED, v_skill.get());
            }
        }

        if(user.groups != null) {
            for (Group group : user.groups) {
                Optional<Vertex> v_group = tx.traversal().V().hasLabel(GraphSchemaConstants.VL_GROUP)
                        .has(PK_GROUPID, group.id).tryNext();

                if (v_group.isPresent())
                    vertex.addEdge(GraphSchemaConstants.EL_MEMBER, v_group.get());
            }
        }

        if(user.favorites != null) {
            for (Favorite favorite: user.favorites) {
                Vertex v_favorite = createFavorite(tx, favorite);

                vertex.addEdge(GraphSchemaConstants.EL_FAVORITED, v_favorite);
            }
        }

        REGISTRY.counter(COUNTER_GET + GraphSchemaConstants.USER).inc();
        long end = System.currentTimeMillis();
        long time = end - start;
        REGISTRY.timer(TIMER_CREATE + GraphSchemaConstants.USER).update(time, TimeUnit.MILLISECONDS);
        return vertex;
    }
}
