/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.representation.Category;
import com.seedlabs.donuts.api.representation.Id;
import com.seedlabs.donuts.api.resource.AssetsCategoriesResource;
import com.seedlabs.donuts.api.service.CategoryService;
import com.seedlabs.donuts.api.utils.FieldUtils;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.util.ArrayList;
import java.util.logging.Level;

public class AssetsCategoriesServerResource extends AbstractServerResource implements AssetsCategoriesResource {

    // Define allowed roles for the method "get".
    private static final String[] get1AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get1DeniedGroups = new String[] {};

    public ArrayList<Category> represent() throws Exception {
        checkGroups(get1AllowedGroups, get1DeniedGroups);
        ArrayList<Category> result;

        try {
            ArrayList<String> fields = FieldUtils.parseFields(getQuery().getValues("fields"));

            result = CategoryService.getCategories(getApplication().getTitanGraph(), fields);

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

    public Id add(Category category) throws Exception {
        checkGroups(post24AllowedGroups, post24DeniedGroups);
        Id result;

        if (category == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        try {
            result = CategoryService.postCategory(getApplication().getTitanGraph(), category);

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
