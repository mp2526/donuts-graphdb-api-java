/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.service;

import com.thinkaurelius.titan.core.TitanGraph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by mp2526 on 3/7/16.
 */
public class ExportService {
    static public String getExport(TitanGraph graph, Integer type) throws Exception {
        if(type == null || type == 1) {
            try (final OutputStream os = new FileOutputStream("donuts-data.json")) {
                final GraphSONMapper mapper = graph.io(IoCore.graphson()).mapper().embedTypes(true).create();
                graph.io(IoCore.graphson()).writer().mapper(mapper).create().writeGraph(os, graph);
            }
        } else if (type == 2) {
            try (final OutputStream os = new FileOutputStream("donuts-data.xml")) {
                graph.io(IoCore.graphml()).writer().normalize(true).create().writeGraph(os, graph);
            }
        }

        return "Done!";
    }
}
