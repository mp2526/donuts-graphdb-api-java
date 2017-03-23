/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.User;
import org.restlet.resource.Get;

import java.util.ArrayList;

public interface UsersSelfFollowedbyResource {

    @Get("json")
    ArrayList<User> represent() throws Exception;

}

