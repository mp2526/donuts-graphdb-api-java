/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.resource.UsersResource;
import com.seedlabs.donuts.api.service.UserService;
import com.seedlabs.donuts.api.representation.User;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class UsersServerResource extends AbstractServerResource implements UsersResource {

    // Define allowed roles for the method "get".
    private static final String[] get11AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get11DeniedGroups = new String[] {};

    public ArrayList<User> represent() throws Exception {
        checkGroups(get11AllowedGroups, get11DeniedGroups);
        ArrayList<User> result;

        try {

            String[] ids = getQuery().getValuesArray("ids");
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = UserService.getUsers(getApplication().getTitanGraph(), ids, fields);
	    
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

