/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.resource.UsersSelfFavoritesResource;
import com.seedlabs.donuts.api.representation.Favorite;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.logging.Level;

public class UsersSelfFavoritesServerResource extends AbstractServerResource implements UsersSelfFavoritesResource {

    // Define allowed roles for the method "get".
    private static final String[] get14AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get14DeniedGroups = new String[] {};

    public Favorite represent() throws Exception {
        checkGroups(get14AllowedGroups, get14DeniedGroups);
        Favorite result = null;

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";



        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }


}

