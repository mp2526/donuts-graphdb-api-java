/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.resource.server;

import com.seedlabs.donuts.api.resource.ExportResource;
import com.seedlabs.donuts.api.service.ExportService;
import org.restlet.data.Status;
import org.restlet.engine.util.StringUtils;
import org.restlet.resource.ResourceException;

import java.util.logging.Level;

public class ExportServerResource extends AbstractServerResource implements ExportResource {

    // Define allowed roles for the method "get".
    private static final String[] get28AllowedGroups = new String[] {"anyone"};
    // Define denied roles for the method "get".
    private static final String[] get28DeniedGroups = new String[] {};

    public String represent() throws Exception {
        checkGroups(get28AllowedGroups, get28DeniedGroups);
        String result;

        try {

            Integer type = null;
            String t = getQuery().getValues("type");

            if(!StringUtils.isNullOrEmpty(t))
                type = Integer.valueOf(getQuery().getValues("type"));

            result = ExportService.getExport(getApplication().getTitanGraph(), type);

        } catch (Exception ex) {
            // In a real code, customize handling for each type of exception
            getLogger().log(Level.WARNING, "Error when executing the method", ex);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    ex.getMessage(), ex);
        }
    
        return result;
    }

}

