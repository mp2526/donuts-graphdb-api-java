/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.PropertiesFileReader;
import com.seedlabs.donuts.api.resource.RootResource;

import java.util.Collections;
import java.util.Map;

public class RootServerResource extends AbstractServerResource implements RootResource {

    // Define allowed roles for the method "get".
    private static final String[] get1AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get1DeniedGroups = new String[] {};

    public Map represent() {
        String gitSha1 = PropertiesFileReader.getGitSha1();
        return Collections.singletonMap("git", gitSha1);
    }
}
