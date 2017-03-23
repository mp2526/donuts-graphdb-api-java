/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.service;

import com.seedlabs.donuts.api.GraphSchemaConstants;
import com.seedlabs.donuts.api.representation.Asset;
import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.representation.User;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.decr;

/**
 * Created by mp2526 on 3/15/16.
 */
public class AssetService {
    static public ArrayList<Asset> getAssets(TitanGraph graph, String[] ids, String userId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Asset> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> assets;

            if(ids != null && ids.length > 0) {
                //Get assets with requested ids
                assets = g.V().hasLabel(GraphSchemaConstants.VL_ASSET).has(GraphSchemaConstants.PK_ASSETID,
                        P.within((Arrays.asList(ids))));
            } else {
                //Get assets of supplied userId
                assets = g.V().has(GraphSchemaConstants.VL_USER, GraphSchemaConstants.PK_USERID, userId)
                        .hasLabel(GraphSchemaConstants.VL_ASSET);
            }

            if(assets.hasNext())
                while (assets.hasNext()) {
                    result.add(Asset.loadAsset(assets.next(), fields));
                }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }

    static public ArrayList<Asset> getActiveAssets(TitanGraph graph, String userId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Asset> result = new ArrayList<>();

        try {
            //TODO: determine criteria of an "active" asset
            //Google Drive Activity
            GraphTraversal<Vertex, Vertex> assets = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).out(GraphSchemaConstants.EL_SHARED)
                    .hasLabel(GraphSchemaConstants.VL_ASSET).limit(10);

            if(assets.hasNext())
                while (assets.hasNext()) {
                    result.add(Asset.loadAsset(assets.next(), fields));
                }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }

    static public ArrayList<Asset> getPopularAssets(TitanGraph graph, String userId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Asset> result = new ArrayList<>();

        try {
            //TODO: determine criteria of an "popular" asset
            //Combination of date added and like count
            GraphTraversal<Vertex, Vertex> assets = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).out(GraphSchemaConstants.EL_SHARED)
                    .hasLabel(GraphSchemaConstants.VL_ASSET).limit(10);

            if(assets.hasNext())
                while (assets.hasNext()) {
                    result.add(Asset.loadAsset(assets.next(), fields));
                }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }

    static public ArrayList<Asset> getRecentAssets(TitanGraph graph, String userId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Asset> result = new ArrayList<>();

        try {
            //TODO: add assets at user level and determine criteria of a "recent" asset
            //List of descending order of assets
            GraphTraversal<Vertex, Vertex> assets = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).out(GraphSchemaConstants.EL_MEMBER)
                    .out(GraphSchemaConstants.EL_CONTAINS).order().by(GraphSchemaConstants.PK_CREATEDDATE, decr)
                    .limit(10);

            if(assets.hasNext())
                while (assets.hasNext()) {
                    result.add(Asset.loadAsset(assets.next(), fields));
                }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }

    static public ArrayList<Asset> getSharedWithAssets(TitanGraph graph, String userId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Asset> result = new ArrayList<>();

        try {
            //TODO: determine criteria of an "shared with" asset
            GraphTraversal<Vertex, Vertex> assets = g.V().has(GraphSchemaConstants.VL_USER,
                    GraphSchemaConstants.PK_USERID, userId).out(GraphSchemaConstants.EL_SHARED)
                    .hasLabel(GraphSchemaConstants.VL_ASSET).limit(10);

            if(assets.hasNext())
                while (assets.hasNext()) {
                    result.add(Asset.loadAsset(assets.next(), fields));
                }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }

        return result;
    }

    static public Asset getAsset(TitanGraph graph, String assetId, String userId, ArrayList<String> fields) throws Exception {

        GraphTraversalSource g = graph.traversal();
        Asset result = null;

        try {
            GraphTraversal<Vertex, Vertex> gt = g.V().has(GraphSchemaConstants.VL_ASSET,
                    GraphSchemaConstants.PK_ASSETID, assetId);

            GraphTraversal<Vertex, Vertex> gt_liked = g.V().has(GraphSchemaConstants.VL_ASSET,
                    GraphSchemaConstants.PK_ASSETID, assetId).in(GraphSchemaConstants.EL_LIKED)
                    .has(GraphSchemaConstants.VL_USER, GraphSchemaConstants.PK_USERID, userId);

            if(gt.hasNext()) {
                result = Asset.loadAsset(gt.next(), gt_liked.hasNext(), fields);
            }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch(Exception ex) {
            throw ex;
        }

        return result;
    }

    static public void putAsset(TitanGraph graph, String assetId, Asset asset) throws Exception {

        try {
            GraphTraversalSource g = graph.traversal();

            Optional<Vertex> v_asset = g.V().hasLabel(GraphSchemaConstants.VL_ASSET)
                    .has(GraphSchemaConstants.PK_ASSETID, assetId).tryNext();

            if(v_asset.isPresent()) {
                Vertex v = v_asset.get();
                if(asset.getMimeType() != null)
                    v.property(GraphSchemaConstants.PK_MIMETYPE, asset.getMimeType());
                if(asset.getName() != null)
                    v.property(GraphSchemaConstants.PK_NAME, asset.getName());
                if(asset.getDescription() != null)
                    v.property(GraphSchemaConstants.PK_DESCRIPTION, asset.getDescription());
                if(asset.getThumbnail() != null)
                    v.property(GraphSchemaConstants.PK_THUMBNAIL, asset.getThumbnail());
                if(asset.getFileName() != null)
                    v.property(GraphSchemaConstants.PK_FILENAME, asset.getFileName());
                if(asset.getUrl() != null)
                    v.property(GraphSchemaConstants.PK_URL, asset.getUrl());
                if(asset.getSize() != null)
                    v.property(GraphSchemaConstants.PK_SIZE, asset.getSize());
                if(asset.getLikeCount() != null)
                    v.property(GraphSchemaConstants.PK_LIKECOUNT, asset.getLikeCount());
                if(asset.getOpenCount() != null)
                    v.property(GraphSchemaConstants.PK_OPENCOUNT, asset.getOpenCount());
                if(asset.getStarCount() != null)
                    v.property(GraphSchemaConstants.PK_STARCOUNT, asset.getStarCount());

//                v.property(GraphSchemaConstants.PK_MODIFIEDDATE, Instant.now().getEpochSecond());
            } else {
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            graph.tx().commit();

        } catch(Exception ex) {
            graph.tx().rollback();
            throw ex;
        }
    }

    static public ArrayList<Comment> getAssetComments(TitanGraph graph, String assetId, ArrayList<String> fields)
            throws Exception
    {
        GraphTraversalSource g = graph.traversal();
        ArrayList<Comment> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> comments = g.V().has(GraphSchemaConstants.VL_ASSET,
                    GraphSchemaConstants.PK_ASSETID, assetId).out(GraphSchemaConstants.EL_HAS)
                    .hasLabel(GraphSchemaConstants.VL_COMMENT);

            if(comments.hasNext())
                while (comments.hasNext()) {
                    result.add(Comment.loadComment(comments.next(), fields));
                }
            else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
        } catch(Exception ex) {
            throw ex;
        }

        return result;
    }

    static public Id postAssetComment(TitanGraph graph, String userId, String assetId, Comment comment) throws Exception {
        Id result = new Id();

        try {
            GraphTraversalSource g = graph.traversal();

            Optional<Vertex> v_asset = g.V().hasLabel(GraphSchemaConstants.VL_ASSET)
                    .has(GraphSchemaConstants.PK_ASSETID, assetId).tryNext();

            if(v_asset.isPresent()) {
                result.setId(java.util.UUID.randomUUID().toString());
                result.setCreatedDate(Instant.now().getEpochSecond());

                Vertex v_comment = graph.addVertex(GraphSchemaConstants.VL_COMMENT);
                v_comment.property(GraphSchemaConstants.PK_COMMENTID, result.getId());
                if(comment.getDetails() != null)
                    v_comment.property(GraphSchemaConstants.PK_DETAILS, comment.getDetails());
                v_comment.property(GraphSchemaConstants.PK_CREATEDDATE, result.getCreatedDate());

                v_asset.get().addEdge(GraphSchemaConstants.EL_HAS, v_comment);

                GraphTraversal<Vertex, Vertex> v_user = g.V().has(GraphSchemaConstants.VL_USER,
                        GraphSchemaConstants.PK_USERID, userId);

                if(v_user.hasNext()) {
                    v_user.next().addEdge(GraphSchemaConstants.EL_SHARED, v_comment);
                }
            } else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

            graph.tx().commit();

        } catch(Exception ex) {
            graph.tx().rollback();
            throw ex;
        }

        return result;
    }

    static public void postAssetCategories(TitanGraph graph, String assetId, ArrayList<String> categories) throws Exception {
        try {
            GraphTraversalSource g = graph.traversal();

            Optional<Vertex> v_asset = g.V().hasLabel(GraphSchemaConstants.VL_ASSET)
                    .has(GraphSchemaConstants.PK_ASSETID, assetId).tryNext();

            if(v_asset.isPresent()) {
                Vertex vertex = v_asset.get();
                if(categories != null) {
                    for (String id : categories) {
                        Optional<Vertex> v_category = g.V().hasLabel(GraphSchemaConstants.VL_CATEGORY)
                                .has(GraphSchemaConstants.PK_CATEGORYID, id).tryNext();

                        if (v_category.isPresent()) {
                            vertex.addEdge(GraphSchemaConstants.EL_CATEGORIZED, v_category.get());
                        }
                        else
                            throw new Exception("Category does not exist.");
                    }
                }
            } else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

            graph.tx().commit();

        } catch(Exception ex) {
            graph.tx().rollback();
            throw ex;
        }
    }

    static public void deleteAssetCategory(TitanGraph graph, String assetId, String categoryId) throws Exception {
        try {
            GraphTraversalSource g = graph.traversal();

            Optional<Vertex> v_asset = g.V().hasLabel(GraphSchemaConstants.VL_ASSET)
                    .has(GraphSchemaConstants.PK_ASSETID, assetId).tryNext();

            if(v_asset.isPresent()) {
                Vertex vertex = v_asset.get();

                //TODO: Finish deleting edge
            } else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

            graph.tx().commit();

        } catch(Exception ex) {
            graph.tx().rollback();
            throw ex;
        }
    }

    static public ArrayList<User> getAssetLikes(TitanGraph graph, String assetId, ArrayList<String> fields)
            throws Exception {

        GraphTraversalSource g = graph.traversal();
        ArrayList<User> result = new ArrayList<>();

        try {
            GraphTraversal<Vertex, Vertex> users = g.V().has(GraphSchemaConstants.VL_ASSET,
                    GraphSchemaConstants.PK_ASSETID, assetId).in(GraphSchemaConstants.EL_LIKED)
                    .hasLabel(GraphSchemaConstants.VL_USER);

            if(users.hasNext()) {
                while (users.hasNext()) {
                    result.add(User.loadUser(users.next(), fields));
                }
            }
        } catch(Exception ex) {
            throw ex;
        }

        return result;
    }

    static public void postAssetLike (TitanGraph graph, String userId, String assetId) throws Exception {

        try {
            GraphTraversalSource g = graph.traversal();

            Optional<Vertex> v_user = g.V().hasLabel(GraphSchemaConstants.VL_USER)
                    .has(GraphSchemaConstants.PK_USERID, userId).tryNext();

            if(v_user.isPresent()) {
                GraphTraversal<Vertex, Vertex> gt_liked = g.V().has(GraphSchemaConstants.VL_ASSET,
                        GraphSchemaConstants.PK_ASSETID, assetId).in(GraphSchemaConstants.EL_LIKED)
                        .has(GraphSchemaConstants.VL_USER, GraphSchemaConstants.PK_USERID, userId);

                if(!gt_liked.hasNext()) {
                    Optional<Vertex> v_asset = g.V().hasLabel(GraphSchemaConstants.VL_ASSET)
                            .has(GraphSchemaConstants.PK_ASSETID, assetId).tryNext();

                    if (v_asset.isPresent()) {
                        Vertex v = v_asset.get();
                        v_user.get().addEdge(GraphSchemaConstants.EL_LIKED, v);
                        v.property(GraphSchemaConstants.PK_LIKECOUNT, (Integer) v.property(GraphSchemaConstants.PK_LIKECOUNT)
                                .value() + 1);
                    } else
                        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
                }
            } else
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);

            graph.tx().commit();

        } catch(Exception ex) {
            graph.tx().rollback();
            throw ex;
        }
    }

    static public void deleteAssetLike(TitanGraph graph, String userId, String assetId) throws Exception {

        try {
            GraphTraversalSource g = graph.traversal();

            GraphTraversal<Vertex, Map<String, Object>> gt_liked = g.V().has(GraphSchemaConstants.VL_ASSET,
                    GraphSchemaConstants.PK_ASSETID, assetId).as(GraphSchemaConstants.ASSET)
                    .inE(GraphSchemaConstants.EL_LIKED).as(GraphSchemaConstants.LIKES)
                    .outV().has(GraphSchemaConstants.VL_USER, GraphSchemaConstants.PK_USERID, userId)
                    .select(GraphSchemaConstants.ASSET, GraphSchemaConstants.LIKES);

            if(gt_liked.hasNext()) {
                Map<String, Object> map = gt_liked.next();
                ((Edge)map.get(GraphSchemaConstants.LIKES)).remove();
                Vertex v = (Vertex)map.get(GraphSchemaConstants.ASSET);

                v.property(GraphSchemaConstants.PK_LIKECOUNT, (Integer)v.property(GraphSchemaConstants.PK_LIKECOUNT)
                        .value() - 1);
            } else {
                throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
            }

            graph.tx().commit();

        } catch(Exception ex) {
            graph.tx().rollback();
            throw ex;
        }
    }
}