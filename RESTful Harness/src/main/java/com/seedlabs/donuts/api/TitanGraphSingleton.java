/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.util.TitanCleanup;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

/**
 * Created by mp2526 on 3/9/16.
 */
public class TitanGraphSingleton {
    private static TitanGraphSingleton instance = new TitanGraphSingleton();
    private TitanGraph graph;
    private Configuration conf;

    public static TitanGraphSingleton getInstance() {
        return instance;
    }

    private TitanGraphSingleton() {
    }

    public void createGraph (String properties) throws Exception {
        this.conf = new PropertiesConfiguration(properties);
        //Load Graph
        this.graph = TitanFactory.open(this.conf);
    }

    public void createGraph (File file) throws Exception {
        this.conf = new PropertiesConfiguration(file);
        //Load Graph
        this.graph = TitanFactory.open(this.conf);
    }

    public TitanGraph getGraph () {
        return this.graph;
    }

    public void clearGraph () {
        this.graph.close();
        TitanCleanup.clear(this.graph);
        this.graph = TitanFactory.open(this.conf);
    }
}
