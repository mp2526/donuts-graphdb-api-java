/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.resource.AssetsSharedWithResource;
import com.seedlabs.donuts.api.utils.FieldUtils;
import com.seedlabs.donuts.api.representation.Asset;
import com.seedlabs.donuts.api.service.AssetService;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class AssetsSharedWithServerResource extends AbstractServerResource implements AssetsSharedWithResource {

    // Define allowed roles for the method "get".
    private static final String[] get3AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get3DeniedGroups = new String[] {};

    public ArrayList<Asset> represent() throws Exception {
        checkGroups(get3AllowedGroups, get3DeniedGroups);
        ArrayList<Asset> result;

        try {

            //TODO: Get the logged in user from the security token
            String userId = "59d1280ee9df";
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = AssetService.getSharedWithAssets(getApplication().getTitanGraph(), userId, fields);

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

