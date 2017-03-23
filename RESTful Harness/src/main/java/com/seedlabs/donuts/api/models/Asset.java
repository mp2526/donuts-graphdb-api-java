/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.models;

import java.util.List;

public class Asset {

    public String id;
    public String mimeType;
    public String name;
    public String description;
    public String thumbnail;
    public String fileName;
    public String url;
    public String size;
    public boolean liked;
    public int likeCount;
    public int openCount;
    public int starCount;
    public String parentId;
    public int parentType;
    public String ownerId;
    public String modifierId;
    public String modifiedDate;
    public String createdDate;

    public List<Category> categories;
    public List<Comment> comments;
}
