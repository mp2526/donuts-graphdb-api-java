/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import org.restlet.resource.Get;

import java.util.Map;

public interface RootResource {

    @Get("json")
    public Map represent();

}

