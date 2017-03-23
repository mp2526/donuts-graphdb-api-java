/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.representation;


public class Id {
    /** Default serial version ID. */
    private static final long serialVersionUID = 1L;

    private String id;
    private Long modifiedDate;
    private Long createdDate;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Long getModifiedDate() {
        return modifiedDate;
    }
    public void setModifiedDate(Long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }
}
