/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.models;

import java.util.List;

public class Post {

    public String id;
    public String details;
    public String ownerId;
    public String parentId;
    public int parentType;
    public String createdDate;

    public List<LinkPreview> linkPreviews;
    public List<Comment> comments;
}
