/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Asset;
import org.restlet.resource.Get;

import java.util.ArrayList;

public interface AssetsPopularResource {

    @Get("json")
    ArrayList<Asset> represent() throws Exception;

}

