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
public class Group {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String image;
    private String privacy;
    private Long createdDate;
    private User owner;

    private List<User> members;
    private List<Asset> assets;
    private List<Category> categories;

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public Long getCreatedDate() { return createdDate; }
    public void setCreatedDate(Long createdDate) { this.createdDate = createdDate; }

    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getMembers() { return members; }
    public void setMembers(List<User> members) {
        this.members = members;
    }
    
    public List<Asset> getAssets() { return assets; }
    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }





    public static Group loadGroup(Vertex v) {
        return loadGroup(v, null);
    }

    public static Group loadGroup(Vertex v, ArrayList<String> fields) {
        Group result = new Group();

        if(fields != null && fields.size() != 0) {
            for (String field : fields) {
                if (field.equalsIgnoreCase(GroupFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_GROUPID).value().toString());
                else if (field.equalsIgnoreCase(GroupFields.NAME.toString()))
                    result.setName(v.property(GraphSchemaConstants.PK_NAME).value().toString());
                else if (field.equalsIgnoreCase(GroupFields.DETAILS.toString()))
                    result.setDescription(v.property(GraphSchemaConstants.PK_DESCRIPTION).value().toString());
                else if (field.equalsIgnoreCase(GroupFields.IMAGE.toString()))
                    result.setImage(v.property(GraphSchemaConstants.PK_IMAGE).value().toString());
                else if (field.equalsIgnoreCase(GroupFields.PRIVACY.toString()))
                    result.setPrivacy(v.property(GraphSchemaConstants.PK_PRIVACY).value().toString());
                else if (field.equalsIgnoreCase(GroupFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
                else if (field.startsWith(GroupFields.OWNER.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_OWNS);

                    ArrayList<String> f = null;

                    if(field.length() > GroupFields.OWNER.toString().length())
                        f = FieldUtils.parseFields(field.substring(GroupFields.OWNER.toString().length() + 1,
                                field.length() - 1));

                    if (iv.hasNext())
                        result.setOwner(User.loadUser(iv.next(), f));
                } else if (field.startsWith(GroupFields.MEMBERS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_MEMBER);

                    List<User> members = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > GroupFields.MEMBERS.toString().length())
                        f = FieldUtils.parseFields(field.substring(GroupFields.MEMBERS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        members.add(User.loadUser(iv.next(), f));
                    }

                    if(!members.isEmpty())
                        result.setMembers(members);
                }
                else if (field.startsWith(GroupFields.ASSETS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_CONTAINS);

                    List<Asset> assets = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > GroupFields.ASSETS.toString().length())
                        f = FieldUtils.parseFields(field.substring(GroupFields.ASSETS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
//                        assets.add(Asset.loadAsset(iv.next(), f));
                    }

                    if(!assets.isEmpty())
                        result.setAssets(assets);
                }
                else if (field.startsWith(GroupFields.CATEGORIES.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_CATEGORIZED);

                    List<Category> categories = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > GroupFields.CATEGORIES.toString().length())
                        f = FieldUtils.parseFields(field.substring(GroupFields.CATEGORIES.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        categories.add(Category.loadCategory(iv.next(), f));
                    }

                    if(!categories.isEmpty())
                        result.setCategories(categories);
                }
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_GROUPID).value().toString());
        }

        return result;
    }

    public enum GroupFields {
        ID("id"),
        OWNER("owner"),
        NAME("name"),
        DETAILS("details"),
        IMAGE("image"),
        PRIVACY("privacy"),
        CREATEDDATE("createdDate"),
        MEMBERS("members"),
        ASSETS("assets"),
        CATEGORIES("categories");

        private final String text;

        /**
         * @param text
         */
        private GroupFields(final String text) {
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
