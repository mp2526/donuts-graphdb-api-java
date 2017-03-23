/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Activity;
import com.seedlabs.donuts.api.representation.Id;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.ArrayList;

public interface UsersUseridFeedResource {

    @Get("json")
    ArrayList<Activity> represent() throws Exception;

    @Post
    Id add(com.seedlabs.donuts.api.representation.Post post) throws Exception;

}

