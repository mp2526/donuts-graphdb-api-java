/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Favorite;
import org.restlet.resource.Get;

public interface UsersSelfFavoritesResource {

    @Get("json")
    Favorite represent() throws Exception;

}

