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
public class Skill {
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

    public String getName() {
        return name;
    }
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





    public static Skill loadSkill(Vertex v) {
        return loadSkill(v, null);
    }

    public static Skill loadSkill(Vertex v, ArrayList<String> fields) {
        Skill result = new Skill();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(SkillFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_SKILLID).value().toString());
                else if (field.equalsIgnoreCase(SkillFields.NAME.toString()))
                    result.setName(v.property(GraphSchemaConstants.PK_NAME).value().toString());
                else if (field.equalsIgnoreCase(SkillFields.DESCRIPTION.toString()))
                    result.setDescription(v.property(GraphSchemaConstants.PK_DESCRIPTION).value().toString());
                else if (field.equalsIgnoreCase(SkillFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_SKILLID).value().toString());
        }

        return result;
    }

    public enum SkillFields {
        ID("id"),
        NAME("name"),
        DESCRIPTION("description"),
        CREATEDDATE("createdDate");

        private final String text;

        /**
         * @param text
         */
        private SkillFields(final String text) {
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
