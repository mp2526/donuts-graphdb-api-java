/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.resource.CommentsCommentidResource;
import com.seedlabs.donuts.api.service.CommentService;
import com.seedlabs.donuts.api.utils.FieldUtils;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class CommentsCommentidServerResource extends AbstractServerResource implements CommentsCommentidResource {

    // Define allowed roles for the method "get".
    private static final String[] get4AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get4DeniedGroups = new String[] {};

    public Comment represent() throws Exception {
        checkGroups(get4AllowedGroups, get4DeniedGroups);
        Comment result = null;

        try {

	        String commentidPathVariable = Reference.decode(getAttribute("commentid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = CommentService.getComment(getApplication().getTitanGraph(), commentidPathVariable, fields);

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

            String commentidPathVariable = Reference.decode(getAttribute("commentid"));

            result = CommentService.postComment(getApplication().getTitanGraph(), commentidPathVariable, comment);

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

    public void update(Comment comment) throws Exception {
        checkGroups(put24AllowedGroups, put24DeniedGroups);

        if (comment == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            String commentidPathVariable = Reference.decode(getAttribute("commentid"));

            CommentService.putComment(getApplication().getTitanGraph(), commentidPathVariable, comment);

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

            String commentidPathVariable = Reference.decode(getAttribute("commentid"));

            CommentService.deleteComment(getApplication().getTitanGraph(), commentidPathVariable);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    }
}

