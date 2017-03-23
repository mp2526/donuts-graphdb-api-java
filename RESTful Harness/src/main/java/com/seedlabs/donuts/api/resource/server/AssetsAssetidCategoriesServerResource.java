/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.resource.AssetsAssetidCategoriesResource;
import com.seedlabs.donuts.api.service.AssetService;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class AssetsAssetidCategoriesServerResource extends AbstractServerResource implements AssetsAssetidCategoriesResource {

    // Define allowed roles for the method "post".
    private static final String[] post24AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "post".
    private static final String[] post24DeniedGroups = new String[] {};

    public void add(ArrayList<String> categories) throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);

        if (categories == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {
            String assetidPathVariable = Reference.decode(getAttribute("assetid"));

            AssetService.postAssetCategories(getApplication().getTitanGraph(), assetidPathVariable, categories);

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
            String assetidPathVariable = Reference.decode(getAttribute("assetid"));
            String categoryidPathVariable = Reference.decode(getAttribute("categoryid"));

            AssetService.deleteAssetCategory(getApplication().getTitanGraph(), assetidPathVariable, categoryidPathVariable);

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
