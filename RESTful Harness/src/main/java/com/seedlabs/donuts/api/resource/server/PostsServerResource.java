/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.resource.PostsResource;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Post;
import com.seedlabs.donuts.api.service.PostService;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class PostsServerResource extends AbstractServerResource implements PostsResource {

    // Define allowed roles for the method "get".
    private static final String[] get6AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get6DeniedGroups = new String[] {};

    public ArrayList<Post> represent() throws Exception {
        checkGroups(get6AllowedGroups, get6DeniedGroups);
        ArrayList<Post> result;

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

            String[] ids = getQuery().getValuesArray("ids");
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

	        result = PostService.getPosts(getApplication().getTitanGraph(), ids, userId, fields);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }


}

