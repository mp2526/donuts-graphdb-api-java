/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import org.restlet.resource.Delete;
import org.restlet.resource.Post;

import java.util.ArrayList;

public interface AssetsAssetidCategoriesResource {

    @Post
    void add(ArrayList<String> categories) throws Exception;

    @Delete
    void remove() throws Exception;
}

