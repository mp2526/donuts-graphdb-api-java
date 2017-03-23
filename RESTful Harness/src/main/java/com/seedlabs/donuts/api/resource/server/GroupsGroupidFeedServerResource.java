/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Activity;
import com.seedlabs.donuts.api.representation.Post;
import com.seedlabs.donuts.api.resource.GroupsGroupidFeedResource;
import com.seedlabs.donuts.api.service.GroupService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

import static com.seedlabs.donuts.api.service.GroupService.postGroupFeed;

public class GroupsGroupidFeedServerResource extends AbstractServerResource implements GroupsGroupidFeedResource {

    // Define allowed roles for the method "get".
    private static final String[] get10AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get10DeniedGroups = new String[] {};

    public ArrayList<Activity> represent() throws Exception {
        checkGroups(get10AllowedGroups, get10DeniedGroups);
        ArrayList<Activity> result;

        try {
		
			String groupidPathVariable = Reference.decode(getAttribute("groupid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = GroupService.getGroupFeed(getApplication().getTitanGraph(), groupidPathVariable, fields);

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

            String groupidPathVariable = Reference.decode(getAttribute("groupid"));

            result = GroupService.postGroupFeed(getApplication().getTitanGraph(), groupidPathVariable, post);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }

        return result;
    }
}

