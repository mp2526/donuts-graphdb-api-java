/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.models;

import java.util.List;

public class Group {

    public String id;
    public String ownerId;
    public String name;
    public String description;
    public String image;
    public String privacy;
    public String createdDate;

    public List<Category> tags;
}
