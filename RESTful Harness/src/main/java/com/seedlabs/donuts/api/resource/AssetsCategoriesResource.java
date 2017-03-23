/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Category;
import com.seedlabs.donuts.api.representation.Id;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.ArrayList;

public interface AssetsCategoriesResource {

    @Get("json")
    ArrayList<Category> represent() throws Exception;

    @Post
    Id add(Category category) throws Exception;

}

