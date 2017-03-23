/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api;

import org.restlet.Component;

/**
 * Created by mp2526 on 3/30/16.
 */
public class GraphApiComponent extends Component {
    public GraphApiComponent() throws Exception {
        setName("donutsService");
        setDescription("Donuts API RESTful Service");
        setOwner("Seedlabs LLC");
        setAuthor("Mike Perry");

        // Attach the application to the default virtual host
        getDefaultHost().attachDefault(new GraphApiApplication());
    }
}
