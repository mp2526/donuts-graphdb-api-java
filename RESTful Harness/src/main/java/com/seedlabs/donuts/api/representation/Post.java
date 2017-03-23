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
public class Post {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String details;
    private User owner;
    private Object parent;
    private String parentType;
    private Long createdDate;

    private List<LinkPreview> linkPreviews;
    private List<Comment> comments;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

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

    public List<LinkPreview> getLinkPreviews() {return linkPreviews;}
    public void setLinkPreviews(List<LinkPreview> linkPreviews) {
        this.linkPreviews = linkPreviews;
    }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }




    public static Post loadPost(Vertex v) {
        return loadPost(v, null);
    }

    public static Post loadPost(Vertex v, ArrayList<String> fields) {
        Post result = new Post();

        if(fields != null && fields.size() != 0) {
            for (String field : fields) {
                if (field.equalsIgnoreCase(PostFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_POSTID).value().toString());
                else if (field.equalsIgnoreCase(PostFields.DETAILS.toString()))
                    result.setDetails(v.property(GraphSchemaConstants.PK_DETAILS).value().toString());
                else if (field.equalsIgnoreCase(PostFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
                else if (field.equalsIgnoreCase(PostFields.PARENTTYPE.toString()))
                    result.setParentType(v.property(GraphSchemaConstants.PK_PARENTTYPE).value().toString());
                else if(field.startsWith(PostFields.OWNER.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_SHARED);

                    ArrayList<String> f = null;

                    if(field.length() > PostFields.OWNER.toString().length())
                        f = FieldUtils.parseFields(field.substring(PostFields.OWNER.toString().length() + 1,
                                field.length() - 1));

                    if(iv.hasNext()) {
                        result.setOwner(User.loadUser(iv.next(), f));
                    }
                } else if(field.startsWith(PostFields.PARENT.toString())) {
                    ArrayList<String> f = null;

                    if(field.length() > PostFields.PARENT.toString().length())
                        f = FieldUtils.parseFields(field.substring(PostFields.PARENT.toString().length() + 1,
                                field.length() - 1));

                    if(v.property(GraphSchemaConstants.PK_PARENTTYPE).value().toString().equalsIgnoreCase(GraphSchemaConstants.USER)) {
                        Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_SHARED);

                        if (iv.hasNext()) {
                            result.setParent(User.loadUser(iv.next(), f));
                        }
                    } else if(v.property(GraphSchemaConstants.PK_PARENTTYPE).value().toString().equalsIgnoreCase(GraphSchemaConstants.GROUP)) {
                        Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_PARENTED);

                        if (iv.hasNext()) {
                            result.setParent(Group.loadGroup(iv.next(), f));
                        }
                    }
                }else if (field.startsWith(PostFields.LINKPREVIEWS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_LINK);

                    List<LinkPreview> linkPreviews = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > PostFields.LINKPREVIEWS.toString().length())
                        f = FieldUtils.parseFields(field.substring(PostFields.LINKPREVIEWS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        linkPreviews.add(LinkPreview.loadLinkPreview(iv.next(), f));
                    }

                    if(!linkPreviews.isEmpty())
                        result.setLinkPreviews(linkPreviews);
                } else if (field.startsWith(PostFields.COMMENTS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_HAS);

                    List<Comment> comments = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > PostFields.COMMENTS.toString().length())
                        f = FieldUtils.parseFields(field.substring(PostFields.COMMENTS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        comments.add(Comment.loadComment(iv.next(), f));
                    }

                    if(!comments.isEmpty())
                        result.setComments(comments);
                }
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_POSTID).value().toString());
        }

        return result;
    }

    public enum PostFields {
        ID("id"),
        DETAILS("details"),
        OWNER("owner"),
        PARENT("parent"),
        PARENTTYPE("parentType"),
        CREATEDDATE("createdDate"),
        LINKPREVIEWS("linkPreviews"),
        COMMENTS("comments");

        private final String text;

        /**
         * @param text
         */
        private PostFields(final String text) {
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
