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
public class Comment {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String details;
    private Long createdDate;
    private User owner;
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

    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }




    public static Comment loadComment(Vertex v) {
        return loadComment(v, null);
    }

    public static Comment loadComment(Vertex v, ArrayList<String> fields) {
        Comment result = new Comment();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(CommentFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_COMMENTID).value().toString());
                else if (field.equalsIgnoreCase(CommentFields.DETAILS.toString()))
                    result.setDetails(v.property(GraphSchemaConstants.PK_DETAILS).value().toString());
                else if (field.equalsIgnoreCase(CommentFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
                else if (field.startsWith(CommentFields.OWNER.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_SHARED);

                    ArrayList<String> f = null;

                    if(field.length() > CommentFields.OWNER.toString().length())
                        f = FieldUtils.parseFields(field.substring(CommentFields.OWNER.toString().length() + 1,
                                field.length() - 1));

                    if (iv.hasNext())
                        result.setOwner(User.loadUser(iv.next(), f));
                } else if (field.startsWith(CommentFields.COMMENTS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_HAS);

                    List<Comment> comments = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > CommentFields.COMMENTS.toString().length())
                        f = FieldUtils.parseFields(field.substring(CommentFields.COMMENTS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        comments.add(loadComment(iv.next(), f));
                    }

                    if(!comments.isEmpty())
                        result.setComments(comments);
                }
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_COMMENTID).value().toString());
        }

        return result;
    }

    public enum CommentFields {
        ID("id"),
        DETAILS("details"),
        CREATEDDATE("createdDate"),
        OWNER("owner"),
        COMMENTS("comments");

        private final String text;

        /**
         * @param text
         */
        private CommentFields(final String text) {
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
