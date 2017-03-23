/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Group;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface GroupsGroupidResource {

    @Get("json")
    Group represent() throws Exception;

    @Put
    void update(Group group) throws Exception;
}

