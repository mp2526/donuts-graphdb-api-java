/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.resource.PostsPostidResource;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Post;
import com.seedlabs.donuts.api.service.PostService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class PostsPostidServerResource extends AbstractServerResource implements PostsPostidResource {

    // Define allowed roles for the method "get".
    private static final String[] get4AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get4DeniedGroups = new String[] {};

    public Post represent() throws Exception {
        checkGroups(get4AllowedGroups, get4DeniedGroups);
        Post result;

        try {

	        String postidPathVariable = Reference.decode(getAttribute("postid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = PostService.getPost(getApplication().getTitanGraph(), postidPathVariable, fields);

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

    public void update(Post post) throws Exception {
        checkGroups(put24AllowedGroups, put24DeniedGroups);

        if (post == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            String postidPathVariable = Reference.decode(getAttribute("postid"));

            PostService.putPost(getApplication().getTitanGraph(), postidPathVariable, post);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }

    // Define allowed roles for the method "delete".
    private static final String[] delete24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "delete".
    private static final String[] delete24DeniedGroups = new String[] {};

    public void remove() throws Exception {
        checkGroups(delete24AllowedGroups, delete24DeniedGroups);

        try {

            String postidPathVariable = Reference.decode(getAttribute("postid"));

            PostService.deletePost(getApplication().getTitanGraph(), postidPathVariable);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }
}

