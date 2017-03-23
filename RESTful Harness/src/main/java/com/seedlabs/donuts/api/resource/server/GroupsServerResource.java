/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Group;
import com.seedlabs.donuts.api.resource.GroupsResource;
import com.seedlabs.donuts.api.service.GroupService;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class GroupsServerResource extends AbstractServerResource implements GroupsResource {

    // Define allowed roles for the method "get".
    private static final String[] get6AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get6DeniedGroups = new String[] {};

    public ArrayList<Group> represent() throws Exception {
        checkGroups(get6AllowedGroups, get6DeniedGroups);
        ArrayList<Group> result;

        try {

            String[] ids = getQuery().getValuesArray("ids");
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

	        result = GroupService.getGroups(getApplication().getTitanGraph(), ids, fields);

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

    public Id add(Group group) throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);
        Id result;

        if (group == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            result = GroupService.postGroup(getApplication().getTitanGraph(), group);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }

        return result;
    }
}

