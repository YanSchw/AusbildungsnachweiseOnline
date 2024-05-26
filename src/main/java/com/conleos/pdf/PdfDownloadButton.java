package com.conleos.pdf;

import com.conleos.data.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class PdfDownloadButton {

    public static Component create(User trainee) {
        VerticalLayout container = new VerticalLayout();

        Anchor anchor = new Anchor(new StreamResource("DeineNachweise.pdf", () -> {
            // Here you can generate the file content dynamically
            return new ByteArrayInputStream(PdfGenerator.generateNachweisPdf(trainee).toByteArray());
        }), "Download");
        anchor.getElement().getStyle().set("display", "none");
        anchor.getElement().setAttribute("download", true);

        Button button = new Button("Download", VaadinIcon.DOWNLOAD.create());
        button.addClickListener(event -> {
            anchor.getElement().callJsFunction("click");
        });
        container.add(anchor, button);

        return container;
    }

}
