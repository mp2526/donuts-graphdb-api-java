/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.User;
import org.restlet.resource.Get;

public interface UsersSelfResource {

    @Get("json")
    User represent() throws Exception;

}

