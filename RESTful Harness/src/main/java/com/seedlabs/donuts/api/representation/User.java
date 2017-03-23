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
public class User {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String jobTitle;
    private Long createdDate;
    private String division;
    private String description;
    private String location;
    private String image;

    private List<Contact> contacts;
    private List<Skill> skills;
    private List<Group> groups;
    private List<Favorite> favorites;
    private List<User> following;
    private List<User> followedBy;


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

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getDivision() {
        return division;
    }
    public void setDivision(String division) {
        this.division = division;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Favorite> getFavorites() { return favorites; }
    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<User> getFollowing() { return following; }
    public void setFollowing(List<User> following) {
        this.following = following;
    }

    public List<User> getFollowedBy() { return followedBy; }
    public void setFollowedBy(List<User> followedBy) {
        this.followedBy = followedBy;
    }




    public static User loadUser(Vertex v) {
        return loadUser(v, null);
    }

    public static User loadUser(Vertex v, ArrayList<String> fields) {
        User result = new User();

        if(fields != null && fields.size() != 0) {
            for (String field: fields) {
                if (field.equalsIgnoreCase(UserFields.ID.toString()))
                    result.setId(v.property(GraphSchemaConstants.PK_USERID).value().toString());
                else if (field.equalsIgnoreCase(UserFields.NAME.toString()))
                    result.setName(v.property(GraphSchemaConstants.PK_NAME).value().toString());
                else if (field.equalsIgnoreCase(UserFields.FIRSTNAME.toString()))
                    result.setFirstName(v.property(GraphSchemaConstants.PK_FIRSTNAME).value().toString());
                else if (field.equalsIgnoreCase(UserFields.LASTNAME.toString()))
                    result.setLastName(v.property(GraphSchemaConstants.PK_LASTNAME).value().toString());
                else if (field.equalsIgnoreCase(UserFields.JOBTITLE.toString()))
                    result.setJobTitle(v.property(GraphSchemaConstants.PK_JOBTITLE).value().toString());
                else if (field.equalsIgnoreCase(UserFields.DIVISION.toString()))
                    result.setDivision(v.property(GraphSchemaConstants.PK_DIVISION).value().toString());
                else if (field.equalsIgnoreCase(UserFields.DESCRIPTION.toString()))
                    result.setDescription(v.property(GraphSchemaConstants.PK_DESCRIPTION).value().toString());
                else if (field.equalsIgnoreCase(UserFields.LOCATION.toString()))
                    result.setLocation(v.property(GraphSchemaConstants.PK_LOCATION).value().toString());
                else if (field.equalsIgnoreCase(UserFields.IMAGE.toString()))
                    result.setImage(v.property(GraphSchemaConstants.PK_IMAGE).value().toString());
                else if (field.equalsIgnoreCase(UserFields.CREATEDDATE.toString()))
                    result.setCreatedDate((Long) v.property(GraphSchemaConstants.PK_CREATEDDATE).value());
                else if (field.startsWith(UserFields.GROUPS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_MEMBER);

                    List<Group> groups = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > UserFields.GROUPS.toString().length())
                        f = FieldUtils.parseFields(field.substring(UserFields.GROUPS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        groups.add(Group.loadGroup(iv.next(), f));
                    }

                    if(!groups.isEmpty())
                        result.setGroups(groups);

                } else if (field.startsWith(UserFields.SKILLS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_OBTAINED);

                    List<Skill> skills = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > UserFields.SKILLS.toString().length())
                        f = FieldUtils.parseFields(field.substring(UserFields.SKILLS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        skills.add(Skill.loadSkill(iv.next(), f));
                    }

                    if(!skills.isEmpty())
                        result.setSkills(skills);

                } else if (field.startsWith(UserFields.CONTACTS.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_HAS);

                    List<Contact> contacts = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > UserFields.CONTACTS.toString().length())
                        f = FieldUtils.parseFields(field.substring(UserFields.CONTACTS.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        contacts.add(Contact.loadContact(iv.next(), f));
                    }

                    if(!contacts.isEmpty())
                        result.setContacts(contacts);

                } else if (field.startsWith(UserFields.FAVORITES.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_FAVORITED);

                    List<Favorite> favorites = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > UserFields.FAVORITES.toString().length())
                        f = FieldUtils.parseFields(field.substring(UserFields.FAVORITES.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        favorites.add(Favorite.loadFavorite(iv.next(), f));
                    }

                    if(!favorites.isEmpty())
                        result.setFavorites(favorites);

                } else if (field.startsWith(UserFields.FOLLOWING.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.OUT, GraphSchemaConstants.EL_FOLLOWS);

                    List<User> following = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > UserFields.FOLLOWING.toString().length())
                        f = FieldUtils.parseFields(field.substring(UserFields.FOLLOWING.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        following.add(User.loadUser(iv.next(), f));
                    }

                    if(!following.isEmpty())
                        result.setFollowing(following);

                } else if (field.startsWith(UserFields.FOLLOWEDBY.toString())) {
                    Iterator<Vertex> iv = v.vertices(Direction.IN, GraphSchemaConstants.EL_FOLLOWS);

                    List<User> followedBy = new ArrayList<>();
                    ArrayList<String> f = null;

                    if(field.length() > UserFields.FOLLOWEDBY.toString().length())
                        f = FieldUtils.parseFields(field.substring(UserFields.FOLLOWEDBY.toString().length() + 1,
                                field.length() - 1));

                    while (iv.hasNext()) {
                        followedBy.add(User.loadUser(iv.next(),
                                FieldUtils.parseFields(field.substring(UserFields.FOLLOWEDBY.toString().length() + 1,
                                        field.length() - 1))));
                    }

                    if(!followedBy.isEmpty())
                        result.setFollowedBy(followedBy);

                }
            }
        } else {
            result.setId(v.property(GraphSchemaConstants.PK_USERID).value().toString());
        }

        return result;
    }

    public enum UserFields {
        ID("id"),
        NAME("name"),
        FIRSTNAME("firstName"),
        LASTNAME("lastName"),
        JOBTITLE("jobTitle"),
        CREATEDDATE("createdDate"),
        DIVISION("division"),
        DESCRIPTION("description"),
        LOCATION("location"),
        IMAGE("image"),
        CONTACTS("contacts"),
        FAVORITES("favorites"),
        GROUPS("groups"),
        SKILLS("skills"),
        FOLLOWING("following"),
        FOLLOWEDBY("followedBy");

        private final String text;

        /**
         * @param text
         */
        private UserFields(final String text) {
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
