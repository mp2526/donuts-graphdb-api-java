/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.models;

import java.util.List;

public class User {

    public String id;
    public String name;
    public String firstName;
    public String lastName;
    public String jobTitle;
    public String createdDate;
    public String division;
    public String description;
    public String location;
    public String image;

    public List<Contact> contacts;
    public List<Skill> skills;
    public List<Group> groups;
    public List<Favorite> favorites;
    public List<User> following;
    public List<User> followedBy;
    public List<Post> posts;
    public List<Asset> assets;
}
