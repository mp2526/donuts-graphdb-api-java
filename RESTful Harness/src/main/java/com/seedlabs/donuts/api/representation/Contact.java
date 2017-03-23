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
public class Contact {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private Integer type;
    private String value;
    private Long createdDate;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }



    public static Contact loadContact(Vertex v) {
        return loadContact(v, null);
    }

    public static Contact loadContact(Vertex v, ArrayList<String> fields) {
        Contact result = new Contact();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(ContactFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_CONTACTID).value().toString());
                else if (field.equalsIgnoreCase(ContactFields.TYPE.toString()))
                    result.setType((Integer) v.property(GraphSchemaConstants.PK_TYPE).value());
                else if (field.equalsIgnoreCase(ContactFields.VALUE.toString()))
                    result.setValue(v.property(GraphSchemaConstants.PK_VALUE).value().toString());
                else if (field.equalsIgnoreCase(ContactFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_CONTACTID).value().toString());
        }

        return result;
    }

    public enum ContactFields {
        ID("id"),
        TYPE("type"),
        VALUE("value"),
        CREATEDDATE("createdDate");

        private final String text;

        /**
         * @param text
         */
        private ContactFields(final String text) {
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
