/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.User;
import com.seedlabs.donuts.api.resource.UsersUseridFollowsResource;
import com.seedlabs.donuts.api.service.UserService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class UsersUseridFollowsServerResource extends AbstractServerResource implements UsersUseridFollowsResource {

    // Define allowed roles for the method "get".
    private static final String[] get26AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get26DeniedGroups = new String[] {};

    public ArrayList<User> represent() throws Exception {
        checkGroups(get26AllowedGroups, get26DeniedGroups);
        ArrayList<User> result;

        try {

	        String useridPathVariable = Reference.decode(getAttribute("userid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = UserService.getUserFollows(getApplication().getTitanGraph(), useridPathVariable, fields);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }


}

