/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.User;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.ArrayList;
import java.util.List;

public interface GroupsMembersResource {

    @Get("json")
    ArrayList<User> represent() throws Exception;

    @Post
    void add(List<User> users) throws Exception;

    @Delete
    void remove() throws Exception;
}

