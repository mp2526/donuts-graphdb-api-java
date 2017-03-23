/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.seedlabs.donuts.api.GraphSchemaConstants;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LinkPreview {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String url;
    private String imageUrl;
    private String description;
    private Long createdDate;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }




    public static LinkPreview loadLinkPreview(Vertex v) {
        return loadLinkPreview(v, null);
    }

    public static LinkPreview loadLinkPreview(Vertex v, ArrayList<String> fields) {
        LinkPreview result = new LinkPreview();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(LinkPreviewFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_LINKPREVIEWID).value().toString());
                else if (field.equalsIgnoreCase(LinkPreviewFields.TITLE.toString()))
                    result.setTitle(v.property(GraphSchemaConstants.PK_TITLE).value().toString());
                else if (field.equalsIgnoreCase(LinkPreviewFields.URL.toString()))
                    result.setUrl(v.property(GraphSchemaConstants.PK_URL).value().toString());
                else if (field.equalsIgnoreCase(LinkPreviewFields.IMAGEURL.toString()))
                    result.setImageUrl(v.property(GraphSchemaConstants.PK_IMAGE).value().toString());
                else if (field.equalsIgnoreCase(LinkPreviewFields.DESCRIPTION.toString()))
                    result.setDescription(v.property(GraphSchemaConstants.PK_DESCRIPTION).value().toString());
                else if (field.equalsIgnoreCase(LinkPreviewFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_LINKPREVIEWID).value().toString());
        }

        return result;
    }

    public enum LinkPreviewFields {
        ID("id"),
        TITLE("title"),
        URL("url"),
        IMAGEURL("imageUrl"),
        DESCRIPTION("description"),
        CREATEDDATE("createdDate");

        private final String text;

        /**
         * @param text
         */
        private LinkPreviewFields(final String text) {
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
