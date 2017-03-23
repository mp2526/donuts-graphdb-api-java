/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.ArrayList;

public interface PostsPostidCommentsResource {

    @Get("json")
    ArrayList<Comment> represent() throws Exception;

    @Post
    Id add(Comment comment) throws Exception;
}

