/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.resource.GroupsNewResource;
import com.seedlabs.donuts.api.service.GroupService;
import com.seedlabs.donuts.api.representation.Group;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class GroupsNewServerResource extends AbstractServerResource implements GroupsNewResource {

    // Define allowed roles for the method "get".
    private static final String[] get8AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get8DeniedGroups = new String[] {};

    public ArrayList<Group> represent() throws Exception {
        checkGroups(get8AllowedGroups, get8DeniedGroups);
        ArrayList<Group> result;

        try {

            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = GroupService.getNewGroups(getApplication().getTitanGraph(), fields);

         } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }


}

