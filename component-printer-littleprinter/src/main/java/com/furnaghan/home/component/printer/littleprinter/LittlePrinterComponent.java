package com.furnaghan.home.component.printer.littleprinter;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.printer.PrinterType;
import com.furnaghan.home.component.printer.littleprinter.client.PrinterClient;
import com.furnaghan.home.component.util.JerseyClientFactory;
import com.google.common.html.HtmlEscapers;

public class LittlePrinterComponent extends Component<PrinterType.Listener> implements PrinterType {
    private final PrinterClient client;

    public LittlePrinterComponent(final LittlePrinterConfiguration configuration) {
        configuration.setGzipEnabledForRequests(false);

        client = new PrinterClient(
                JerseyClientFactory.build("littleprinter-" + configuration.getId(), configuration),
                configuration.getRoot(),
                configuration.getId()
        );
    }

    @Override
    public void print(final String message) {
        final String html = String.format("<p>%s</p>", HtmlEscapers.htmlEscaper().escape(message));
        client.print(html);
    }
}
