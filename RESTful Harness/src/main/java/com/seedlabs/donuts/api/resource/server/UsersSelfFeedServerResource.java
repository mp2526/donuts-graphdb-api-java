/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Activity;
import com.seedlabs.donuts.api.representation.Post;
import com.seedlabs.donuts.api.resource.UsersSelfFeedResource;
import com.seedlabs.donuts.api.service.UserService;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class UsersSelfFeedServerResource extends AbstractServerResource implements UsersSelfFeedResource {

    // Define allowed roles for the method "get".
    private static final String[] get15AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get15DeniedGroups = new String[] {};

    public ArrayList<Activity> represent() throws Exception {
        checkGroups(get15AllowedGroups, get15DeniedGroups);
        ArrayList<Activity> result;

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = UserService.getUserFeed(getApplication().getTitanGraph(), userId, fields);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }

    // Define allowed roles for the method "post".
    private static final String[] post16AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "post".
    private static final String[] post16DeniedGroups = new String[] {};

    public Id add(Post post) throws Exception {
        checkGroups(post16AllowedGroups, post16DeniedGroups);
        Id result;

    	if (post==null) {
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    	}

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

	        result = UserService.postUserFeed(getApplication().getTitanGraph(), userId, post);
	    
	        // Initialize here your bean
        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }
}

