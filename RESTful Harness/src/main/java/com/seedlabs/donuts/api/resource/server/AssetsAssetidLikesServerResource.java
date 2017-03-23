/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.User;
import com.seedlabs.donuts.api.resource.AssetsAssetidLikesResource;
import com.seedlabs.donuts.api.service.AssetService;
import com.seedlabs.donuts.api.utils.FieldUtils;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class AssetsAssetidLikesServerResource extends AbstractServerResource implements AssetsAssetidLikesResource {

    // Define allowed roles for the method "get".
    private static final String[] get4AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get4DeniedGroups = new String[] {};

    public ArrayList<User> represent() throws Exception {
        checkGroups(get4AllowedGroups, get4DeniedGroups);
        ArrayList<User> result;

        try {

            String assetidPathVariable = Reference.decode(getAttribute("assetid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = AssetService.getAssetLikes(getApplication().getTitanGraph(), assetidPathVariable, fields);

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

    public void add() throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);

        try {
            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

            String assetidPathVariable = Reference.decode(getAttribute("assetid"));

            AssetService.postAssetLike(getApplication().getTitanGraph(), userId, assetidPathVariable);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);

            if(ResourceException.class.isInstance(ex))
                throw ex;
            else
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex.getMessage(), ex);
        }
    }

    // Define allowed roles for the method "delete".
    private static final String[] delete24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "delete".
    private static final String[] delete24DeniedGroups = new String[] {};

    public void remove() throws Exception {
        checkGroups(delete24AllowedGroups, delete24DeniedGroups);

        try {
            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

            String assetidPathVariable = Reference.decode(getAttribute("assetid"));

            AssetService.deleteAssetLike(getApplication().getTitanGraph(), userId, assetidPathVariable);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);

            if(ResourceException.class.isInstance(ex))
                throw ex;
            else
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL, ex.getMessage(), ex);
        }
    }
}
