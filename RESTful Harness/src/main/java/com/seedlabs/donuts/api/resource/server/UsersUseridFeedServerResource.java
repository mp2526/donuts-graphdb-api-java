/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Activity;
import com.seedlabs.donuts.api.representation.Post;
import com.seedlabs.donuts.api.resource.UsersUseridFeedResource;
import com.seedlabs.donuts.api.service.UserService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class UsersUseridFeedServerResource extends AbstractServerResource implements UsersUseridFeedResource {

    // Define allowed roles for the method "get".
    private static final String[] get23AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get23DeniedGroups = new String[] {};

    public ArrayList<Activity> represent() throws Exception {
        checkGroups(get23AllowedGroups, get23DeniedGroups);
        ArrayList<Activity> result;

        try {

	        String useridPathVariable = Reference.decode(getAttribute("userid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

	        result = UserService.getUserFeed(getApplication().getTitanGraph(), useridPathVariable, fields);

         } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }

    // Define allowed roles for the method "post".
    private static final String[] post24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "post".
    private static final String[] post24DeniedGroups = new String[] {};

    public Id add(Post post) throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);
        Id result;

    	if (post==null) {
    		throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    	}

        try {
	        String useridPathVariable = Reference.decode(getAttribute("userid"));

            result = UserService.postUserFeed(getApplication().getTitanGraph(), useridPathVariable, post);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }

        return result;
    }


}

