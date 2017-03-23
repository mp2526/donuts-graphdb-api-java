/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import org.restlet.resource.Get;

public interface ExportResource {

    @Get("json")
    String represent() throws Exception;

}

