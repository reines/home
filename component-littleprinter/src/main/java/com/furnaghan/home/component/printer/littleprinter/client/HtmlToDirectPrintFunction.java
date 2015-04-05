package com.furnaghan.home.component.printer.littleprinter.client;

import com.google.common.base.Function;
import com.sun.jersey.api.representation.Form;

import javax.annotation.Nullable;

public class HtmlToDirectPrintFunction implements Function<String, Form> {

    public static final Function<String, Form> INSTANCE = new HtmlToDirectPrintFunction();

    @Nullable
    @Override
    public Form apply(@Nullable final String input) {
        return input == null ? null : buildForm(input);
    }

    private Form buildForm(String html) {
        final Form form = new Form();
        form.add("html", html);
        return form;
    }
}
