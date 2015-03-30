package com.furnaghan.home.component.torrent.deluge.client.filter;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class JsonClientFilter extends ClientFilter {

    private static final MediaType REPLACE_TYPE = new MediaType("application", "x-json");

    @Override
    public ClientResponse handle(final ClientRequest clientRequest) throws ClientHandlerException {
        final ClientResponse clientResponse = getNext().handle(clientRequest);

        if (REPLACE_TYPE.equals(clientResponse.getType())) {
            clientResponse.getHeaders().remove(HttpHeaders.CONTENT_TYPE);
            clientResponse.getHeaders().putSingle(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }

        return clientResponse;
    }
}
