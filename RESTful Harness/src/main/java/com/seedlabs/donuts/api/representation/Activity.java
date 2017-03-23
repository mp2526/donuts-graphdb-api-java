/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Activity {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private java.lang.String id;

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }


    private java.lang.String ownerId;

    public java.lang.String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(java.lang.String ownerId) {
        this.ownerId = ownerId;
    }


    private java.lang.String action;

    public java.lang.String getAction() {
        return action;
    }

    public void setAction(java.lang.String action) {
        this.action = action;
    }


    private java.lang.Integer targetType;

    public java.lang.Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(java.lang.Integer targetType) {
        this.targetType = targetType;
    }


    private java.lang.Integer sourceType;

    public java.lang.Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(java.lang.Integer sourceType) {
        this.sourceType = sourceType;
    }


    private java.lang.String sourceId;

    public java.lang.String getSourceId() {
        return sourceId;
    }

    public void setSourceId(java.lang.String sourceId) {
        this.sourceId = sourceId;
    }


    private java.lang.String sourceParentId;

    public java.lang.String getSourceParentId() {
        return sourceParentId;
    }

    public void setSourceParentId(java.lang.String sourceParentId) {
        this.sourceParentId = sourceParentId;
    }


    private java.lang.Long createdDate;

    public java.lang.Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.lang.Long createdDate) {
        this.createdDate = createdDate;
    }

    public static Activity loadActivity(Vertex v) {
        return loadActivity(v, null);
    }

    public static Activity loadActivity(Vertex v, ArrayList<String> fields) {
        Activity result = new Activity();
//        result.setId(v.property(GraphSchemaConstants.PK_ACTIVITYID).value().toString());
//        result.setOwnerId(v.property(GraphSchemaConstants.PK_OWNERID).value().toString());
//        result.setAction(v.property(GraphSchemaConstants.PK_ACTION).value().toString());
//        result.setTargetType(Integer.getInteger(v.property(GraphSchemaConstants.PK_TARGETTYPE).value().toString()));
//        result.setSourceType(Integer.getInteger(v.property(GraphSchemaConstants.PK_SOURCETYPE).value().toString()));
//        result.setSourceId(v.property(GraphSchemaConstants.PK_SOURCEID).value().toString());
//        result.setSourceParentId(v.property(GraphSchemaConstants.PK_SOURCEPARENTID).value().toString());
//        result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());

        return result;
    }

    public enum ActivityFields {
        ID("id"),
        NAME("name"),
        CREATEDDATE("createdDate");

        private final String text;

        /**
         * @param text
         */
        private ActivityFields(final String text) {
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
