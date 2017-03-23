/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Group;
import com.seedlabs.donuts.api.representation.Id;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.ArrayList;

public interface GroupsResource {

    @Get("json")
    ArrayList<Group> represent() throws Exception;

    @Post
    Id add(Group group) throws Exception;
}

