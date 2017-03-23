/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.resource.AssetsAssetidCommentsResource;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Comment;
import com.seedlabs.donuts.api.service.AssetService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class AssetsAssetidCommentsServerResource extends AbstractServerResource implements AssetsAssetidCommentsResource {

    // Define allowed roles for the method "get".
    private static final String[] get5AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get5DeniedGroups = new String[] {};

    public ArrayList<Comment> represent() throws Exception {
        checkGroups(get5AllowedGroups, get5DeniedGroups);
        ArrayList<Comment> result;

        try {

            String assetidPathVariable = Reference.decode(getAttribute("assetid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = AssetService.getAssetComments(getApplication().getTitanGraph(), assetidPathVariable, fields);

         } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);

            if(ResourceException.class.isInstance(ex))
                throw ex;
            else
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex.getMessage(), ex);
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

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

            String assetidPathVariable = Reference.decode(getAttribute("assetid"));

            result = AssetService.postAssetComment(getApplication().getTitanGraph(), userId, assetidPathVariable, comment);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);

            if(ResourceException.class.isInstance(ex))
                throw ex;
            else
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex.getMessage(), ex);
        }

        return result;
    }
}
