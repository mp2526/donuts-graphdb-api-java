/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.models;

import java.util.List;

public class Comment {

    public String id;
    public String ownerId;
    public String parentId;
    public int parentType;
    public String details;
    public String createdDate;

    public List<Comment> comments;
}
