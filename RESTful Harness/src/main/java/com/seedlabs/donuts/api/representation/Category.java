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
public class Category {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private Long createdDate;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
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




    public static Category loadCategory(Vertex v) {
        return loadCategory(v, null);
    }

    public static Category loadCategory(Vertex v, ArrayList<String> fields) {
        Category result = new Category();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(CategoryFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_CATEGORYID).value().toString());
                else if (field.equalsIgnoreCase(CategoryFields.NAME.toString()))
                    result.setName(v.property(GraphSchemaConstants.PK_NAME).value().toString());
                else if (field.equalsIgnoreCase(CategoryFields.DESCRIPTION.toString()))
                    result.setDescription(v.property(GraphSchemaConstants.PK_DESCRIPTION).value().toString());
                else if (field.equalsIgnoreCase(CategoryFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_CATEGORYID).value().toString());
        }

        return result;
    }

    public enum CategoryFields {
        ID("id"),
        NAME("name"),
        DESCRIPTION("description"),
        CREATEDDATE("createdDate");

        private final String text;

        /**
         * @param text
         */
        private CategoryFields(final String text) {
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
