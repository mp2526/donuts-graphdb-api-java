/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Post;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.Delete;

public interface PostsPostidResource {

    @Get("json")
    Post represent() throws Exception;

    @Put
    void update(Post post) throws Exception;

    @Delete
    void remove() throws Exception;
}

