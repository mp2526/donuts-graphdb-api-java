/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

/**
 * Created by mp2526 on 4/13/16.
 */
public class SeedJacksonConverter extends JacksonConverter {
    protected <T> JacksonRepresentation<T> create(MediaType mediaType, T source) {
        JacksonRepresentation<T> representation = new JacksonRepresentation<T>(mediaType, source);
        ObjectMapper objectMapper = representation.getObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return representation;
    }

    protected <T> JacksonRepresentation<T> create(Representation source, Class<T> objectClass) {
        JacksonRepresentation<T> representation = new JacksonRepresentation<T>(source, objectClass);
        ObjectMapper objectMapper = representation.getObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return representation;
    }
}
