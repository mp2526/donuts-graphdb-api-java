/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Asset;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface AssetsAssetidResource {

    @Get("json")
    Asset represent() throws Exception;

    @Put
    void update(Asset asset) throws Exception;
}

