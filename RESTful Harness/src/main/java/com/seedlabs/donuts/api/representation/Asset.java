/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.seedlabs.donuts.api.GraphSchemaConstants;
import com.seedlabs.donuts.api.utils.FieldUtils;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Asset {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String mimeType;
    private String name;
    private String description;
    private String thumbnail;
    private String fileName;
    private String url;
    private Float size;
    private Boolean liked;
    private Integer likeCount;
    private Integer openCount;
    private Integer starCount;
    private Object parent;
    private String parentType;
    private User owner;
    private User modifier;
    private Long modifiedDate;
    private Long createdDate;

    private List<Category> categories;
    private List<Comment> comments;


    public String getId() {
        return id;
    }
    public void setId(java.lang.String id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }
    public void setName(java.lang.String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    public Float getSize() { return size; }
    public void setSize(Float size) { this.size = size; }

    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public Integer getOpenCount() { return openCount; }
    public void setOpenCount(Integer openCount) { this.openCount = openCount; }

    public Integer getStarCount() { return starCount; }
    public void setStarCount(Integer starCount) { this.starCount = starCount; }

    public Object getParent() {
        return parent;
    }
    public void setParent(Object parent) {
        this.parent = parent;
    }

    public String getParentType() {
        return parentType;
    }
    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getModifier() {
        return modifier;
    }
    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }


    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }




    public static Asset loadAsset(Vertex v) {
        return loadAsset(v, null, null);
    }

    public static Asset loadAsset(Vertex v, Boolean liked) {
        return loadAsset(v, liked, null);
    }

    public static Asset loadAsset(Vertex v, ArrayList<String> fields) { return loadAsset(v, null, fields); }

    public static Asset loadAsset(Vertex v, Boolean liked, ArrayList<String> fields) {
        Asset result = new Asset();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(AssetFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_ASSETID).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.MIMETYPE.toString()))
                    result.setMimeType(v.property(GraphSchemaConstants.PK_MIMETYPE).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.NAME.toString()))
                    result.setName(v.property(GraphSchemaConstants.PK_NAME).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.DESCRIPTION.toString()))
                    result.setDescription(v.property(GraphSchemaConstants.PK_DESCRIPTION).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.THUMBNAIL.toString()))
                    result.setThumbnail(v.property(GraphSchemaConstants.PK_THUMBNAIL).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.FILENAME.toString()))
                    result.setFileName(v.property(GraphSchemaConstants.PK_FILENAME).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.URL.toString()))
                    result.setUrl(v.property(GraphSchemaConstants.PK_URL).value().toString());
                else if (field.equalsIgnoreCase(AssetFields.SIZE.toString()))
                    result.setSize((Float) v.property(GraphSchemaConstants.PK_SIZE).value());
                else if (field.equalsIgnoreCase(AssetFields.LIKED.toString()))
                    result.setLiked(liked);
                else if (field.equalsIgnoreCase(AssetFields.LIKECOUNT.toString()))
                    result.setLikeCount((Integer) v.property(GraphSchemaConstants.PK_LIKECOUNT).value());
                else if (field.equalsIgnoreCase(AssetFields.OPENCOUNT.toString()))
                    result.setOpenCount((Integer) v.property(GraphSchemaConstants.PK_OPENCOUNT).value());
                else if (field.equalsIgnoreCase(AssetFields.STARCOUNT.toString()))
                    result.setStarCount((Integer) v.property(GraphSchemaConstants.PK_STARCOUNT).value());
                else if (field.equalsIgnoreCase(AssetFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
                else if (field.equalsIgnoreCase(AssetFields.MODIFIEDDATE.toString()))
                    result.setModifiedDate((Long) v.property(GraphSchemaConstants.PK_MODIFIEDDATE).value());
                else if (field.equalsIgnoreCase(AssetFields.PARENTTYPE.toString()))
                    result.setParentType(v.property(GraphSchemaConstants.PK_PARENTTYPE).value().toString());
                else if(field.startsWith(AssetFields.PARENT.toString())) {
                    ArrayList<String> f = null;

                    if(field.length() > AssetFields.PARENT.toString().length())
                        f = FieldUtils.parseFields(field.substring(AssetFields.PARENT.toString().length() + 1,
                                field.length() - 1));

                    if(v.property(GraphSchemaConstants.PK_PARENTTYPE).value().toString().equalsIgnoreCase(GraphSchemaConstants.USER)) {
                        Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_SHARED);

                        if (iv.hasNext()) {
                            result.setParent(User.loadUser(iv.next(), f));
                        }
                    } else if(v.property(GraphSchemaConstants.PK_PARENTTYPE).value().toString().equalsIgnoreCase(GraphSchemaConstants.GROUP)) {
                        Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_CONTAINS);

                        if (iv.hasNext()) {
                            result.setParent(Group.loadGroup(iv.next(), f));
                        }
                    }
                } else if(field.startsWith(AssetFields.OWNER.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_SHARED);

                    ArrayList<String> f = null;

                    if(field.length() > AssetFields.OWNER.toString().length())
                        f = FieldUtils.parseFields(field.substring(AssetFields.OWNER.toString().length() + 1,
                                field.length() - 1));

                    if(iv.hasNext()) {
                        result.setOwner(User.loadUser(iv.next(), f));
                    }
                } else if(field.startsWith(AssetFields.MODIFIER.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_MODIFIED);
                    ArrayList<String> f = null;

                    if(field.length() > AssetFields.MODIFIER.toString().length())
                        f = FieldUtils.parseFields(field.substring(AssetFields.MODIFIER.toString().length() + 1,
                                field.length() - 1));

                    if(iv.hasNext()) {
                        result.setModifier(User.loadUser(iv.next(), f));
                    }
                } else if (field.startsWith(AssetFields.CATEGORIES.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_CATEGORIZED);

                    List<Category> categories = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > AssetFields.CATEGORIES.toString().length())
                        f = FieldUtils.parseFields(field.substring(AssetFields.CATEGORIES.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        categories.add(Category.loadCategory(iv.next(), f));
                    }

                    if(!categories.isEmpty())
                        result.setCategories(categories);

                } else if (field.startsWith(AssetFields.COMMENTS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_HAS);

                    List<Comment> comments = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > AssetFields.COMMENTS.toString().length())
                        f = FieldUtils.parseFields(field.substring(AssetFields.COMMENTS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        comments.add(Comment.loadComment(iv.next(), f));
                    }

                    if(!comments.isEmpty())
                        result.setComments(comments);
                }
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_ASSETID).value().toString());
        }

        return result;
    }

    public enum AssetFields {
        ID("id"),
        NAME("name"),
        MIMETYPE("mimeType"),
        DESCRIPTION("description"),
        THUMBNAIL("thumbnail"),
        FILENAME("fileName"),
        URL("url"),
        SIZE("size"),
        LIKED("liked"),
        LIKECOUNT("likeCount"),
        OPENCOUNT("openCount"),
        STARCOUNT("starCount"),
        PARENT("parent"),
        PARENTTYPE("parentType"),
        OWNER("owner"),
        MODIFIER("modifier"),
        MODIFIEDDATE("modifiedDate"),
        CREATEDDATE("createdDate"),
        CATEGORIES("categories"),
        COMMENTS("comments");

        private final String text;

        /**
         * @param text
         */
        private AssetFields(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }
}
