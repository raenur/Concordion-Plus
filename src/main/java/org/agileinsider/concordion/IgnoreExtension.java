package org.agileinsider.concordion;

import org.agileinsider.concordion.command.IgnoreCommand;
import org.agileinsider.concordion.event.IgnoreListener;
import org.agileinsider.concordion.render.IgnoreResultRenderer;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.util.IOUtil;

public class IgnoreExtension implements ConcordionExtension {
    public static final String IGNORE_COMMAND = "ignore";
    public static final String IGNORE_CSS = "/org/agileinsider/concordion/ignore.css";

    private final IgnoreCommand ignoreCommand;
    private final IgnoreListener resultRenderer;

    public IgnoreExtension() {
        this(new IgnoreCommand(), new IgnoreResultRenderer());
    }

    protected IgnoreExtension(IgnoreCommand ignoreCommand, IgnoreListener resultRenderer) {
        this.ignoreCommand = ignoreCommand;
        this.resultRenderer = resultRenderer;
    }

    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withCommand(ConcordionPlusExtension.CONCORDION_PLUS_NAMESPACE, IGNORE_COMMAND, ignoreCommand);
        ignoreCommand.addIgnoreListener(resultRenderer);
        String stylesheetContent = IOUtil.readResourceAsString(IGNORE_CSS);
        concordionExtender.withEmbeddedCSS(stylesheetContent);
    }
}
