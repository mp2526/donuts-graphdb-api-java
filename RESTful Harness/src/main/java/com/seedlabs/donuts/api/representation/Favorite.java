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
public class Favorite {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String url;
    private Long createdDate;

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }



    public static Favorite loadFavorite(Vertex v) {
        return loadFavorite(v, null);
    }

    public static Favorite loadFavorite(Vertex v, ArrayList<String> fields) {
        Favorite result = new Favorite();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(FavoriteFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_FAVORITEID).value().toString());
                else if (field.equalsIgnoreCase(FavoriteFields.NAME.toString()))
                    result.setName(v.property(GraphSchemaConstants.PK_NAME).value().toString());
                else if (field.equalsIgnoreCase(FavoriteFields.URL.toString()))
                    result.setUrl(v.property(GraphSchemaConstants.PK_URL).value().toString());
                else if (field.equalsIgnoreCase(FavoriteFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_FAVORITEID).value().toString());
        }

        return result;
    }

    public enum FavoriteFields {
        ID("id"),
        NAME("name"),
        URL("url"),
        CREATEDDATE("createdDate");

        private final String text;

        /**
         * @param text
         */
        private FavoriteFields(final String text) {
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
