/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.resource.UsersSelfSkillsResource;
import com.seedlabs.donuts.api.service.UserService;
import com.seedlabs.donuts.api.representation.Skill;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class UsersSelfSkillsServerResource extends AbstractServerResource implements UsersSelfSkillsResource {

    // Define allowed roles for the method "get".
    private static final String[] get20AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get20DeniedGroups = new String[] {};

    public ArrayList<Skill> represent() throws Exception {
        checkGroups(get20AllowedGroups, get20DeniedGroups);
        ArrayList<Skill> result;

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = UserService.getUserSkills(getApplication().getTitanGraph(), userId, fields);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }


}

