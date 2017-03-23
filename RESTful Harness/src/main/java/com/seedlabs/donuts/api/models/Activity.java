/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.models;

public class Activity {

    public String id;
    public String ownerId;
    public String action;
    public Integer targetType;
    public Integer sourceType;
    public String sourceId;
    public String sourceParentId;
    public Long createdDate;
}
