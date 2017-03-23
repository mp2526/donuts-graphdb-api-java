/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.User;
import com.seedlabs.donuts.api.resource.GroupsMembersResource;
import com.seedlabs.donuts.api.service.GroupService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GroupsMembersServerResource extends AbstractServerResource implements GroupsMembersResource {

    // Define allowed roles for the method "get".
    private static final String[] get6AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get6DeniedGroups = new String[] {};

    public ArrayList<User> represent() throws Exception {
        checkGroups(get6AllowedGroups, get6DeniedGroups);
        ArrayList<User> result;

        try {

            String groupidPathVariable = Reference.decode(getAttribute("groupid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

	        result = GroupService.getGroupsMembers(getApplication().getTitanGraph(), groupidPathVariable, fields);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }

    // Define allowed roles for the method "put".
    private static final String[] post24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "put".
    private static final String[] post24DeniedGroups = new String[] {};

    public void add(List<User> users) throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);

        if (users == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            String groupidPathVariable = Reference.decode(getAttribute("groupid"));

            GroupService.postGroupsMembers(getApplication().getTitanGraph(), groupidPathVariable, users);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }

    // Define allowed roles for the method "put".
    private static final String[] delete24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "put".
    private static final String[] delete24DeniedGroups = new String[] {};

    public void remove() throws Exception {
        checkGroups(delete24AllowedGroups, delete24DeniedGroups);

        try {

            String groupidPathVariable = Reference.decode(getAttribute("groupid"));
            String memberidPathVariable = Reference.decode(getAttribute("memberid"));

            GroupService.deleteGroupsMember(getApplication().getTitanGraph(), groupidPathVariable, memberidPathVariable);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }
}

