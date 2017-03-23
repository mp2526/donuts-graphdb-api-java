/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;


import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface CommentsCommentidResource {

    @Get("json")
    Comment represent() throws Exception;

    @Post
    Id add(Comment comment) throws Exception;

    @Put
    void update(Comment comment) throws Exception;

    @Delete
    void remove() throws Exception;
}

