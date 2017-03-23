/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.resource.PostsPostidCommentsResource;
import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.service.PostService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class PostsPostidCommentsServerResource extends AbstractServerResource implements PostsPostidCommentsResource {

    // Define allowed roles for the method "get".
    private static final String[] get4AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get4DeniedGroups = new String[] {};

    public ArrayList<Comment> represent() throws Exception {
        checkGroups(get4AllowedGroups, get4DeniedGroups);
        ArrayList<Comment> result;

        try {

	        String postidPathVariable = Reference.decode(getAttribute("postid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = PostService.getPostComments(getApplication().getTitanGraph(), postidPathVariable, fields);

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

    public Id add(Comment comment) throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);
        Id result;

        if (comment == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            String postidPathVariable = Reference.decode(getAttribute("postid"));

            result = PostService.postComment(getApplication().getTitanGraph(), postidPathVariable, comment);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }

        return result;
    }
}

