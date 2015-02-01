package com.furnaghan.home.component.printer.littleprinter.client;

import com.sun.jersey.api.client.Client;

import java.net.URI;

public class PrinterClient {

    private final Client client;
    private final URI root;
    private final String id;

    public PrinterClient(final Client client, final URI root, final String id) {
        this.client = client;
        this.root = root;
        this.id = id;
    }

    public void print(final String html) {
        client.resource(root)
                .path(String.format("/playground/direct_print/%s", id))
                .post(HtmlToDirectPrintFunction.INSTANCE.apply(html));
    }
}
