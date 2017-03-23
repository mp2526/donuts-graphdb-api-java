/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Group;
import com.seedlabs.donuts.api.resource.GroupsGroupidResource;
import com.seedlabs.donuts.api.service.GroupService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class GroupsGroupidServerResource extends AbstractServerResource implements GroupsGroupidResource {

    // Define allowed roles for the method "get".
    private static final String[] get9AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get9DeniedGroups = new String[] {};

    public Group represent() throws Exception {
        checkGroups(get9AllowedGroups, get9DeniedGroups);
        Group result;

        try {

	        String groupidPathVariable = Reference.decode(getAttribute("groupid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = GroupService.getGroup(getApplication().getTitanGraph(), groupidPathVariable, fields);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }

    // Define allowed roles for the method "put".
    private static final String[] put24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "put".
    private static final String[] put24DeniedGroups = new String[] {};

    public void update(Group group) throws Exception {
        checkGroups(put24AllowedGroups, put24DeniedGroups);

        if (group == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            String groupidPathVariable = Reference.decode(getAttribute("groupid"));

            GroupService.putGroup(getApplication().getTitanGraph(), groupidPathVariable, group);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }
}

