/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Asset;
import com.seedlabs.donuts.api.resource.AssetsAssetidResource;
import com.seedlabs.donuts.api.service.AssetService;
import com.seedlabs.donuts.api.utils.FieldUtils;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class AssetsAssetidServerResource extends AbstractServerResource implements AssetsAssetidResource {

    // Define allowed roles for the method "get".
    private static final String[] get4AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get4DeniedGroups = new String[] {};

    public Asset represent() throws Exception {
        checkGroups(get4AllowedGroups, get4DeniedGroups);
        Asset result;

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";

	        String assetidPathVariable = Reference.decode(getAttribute("assetid"));
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = AssetService.getAsset(getApplication().getTitanGraph(), assetidPathVariable, userId, fields);

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

    // Define allowed roles for the method "put".
    private static final String[] put24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "put".
    private static final String[] put24DeniedGroups = new String[] {};

    public void update(Asset asset) throws Exception {
        checkGroups(put24AllowedGroups, put24DeniedGroups);

        if (asset == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {

            String assetidPathVariable = Reference.decode(getAttribute("assetid"));

            AssetService.putAsset(getApplication().getTitanGraph(), assetidPathVariable, asset);

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

