/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource;

import com.seedlabs.donuts.api.representation.Skill;
import org.restlet.resource.Get;

import java.util.ArrayList;

public interface UsersUseridSkillsResource {

    @Get("json")
    ArrayList<Skill> represent() throws Exception;

}

