/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Group;
import org.restlet.resource.Get;

import java.util.ArrayList;

public interface GroupsNewResource {

    @Get("json")
    ArrayList<Group> represent() throws Exception;

}

