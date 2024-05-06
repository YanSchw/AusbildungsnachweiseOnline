package com.conleos.views.form;

import com.conleos.common.Role;
import com.conleos.core.Session;
import com.conleos.data.entity.Form;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

public class Day {
    LocalDate date;
    List<DayEntry> entries = new ArrayList<>();
    VerticalLayout container = new VerticalLayout();

    public Day(LocalDate date) {
        this.date = date;
    }

    public VerticalLayout createFormContentForDay(Form form, int i) {
        String dayLabel = getLocalDayName();
        container.setWidthFull();

        VerticalLayout day = new VerticalLayout();
        day.setClassName(Border.ALL);




        // Init the Container with Content from Database
        List<Form.FormEntry> initEntries = form.getEntriesByDate(date);
        if (!initEntries.isEmpty()) {
            for (Form.FormEntry entry : initEntries) {
                DayEntry dayEntryData = new DayEntry(this, container, entry, entries);
                entries.add(dayEntryData);
                container.add(dayEntryData);
            }
        } else {
            DayEntry dayEntry = new DayEntry(this, container, null, entries);
            entries.add(dayEntry);
            container.add(dayEntry);
        }
        Session session = Session.getSessionFromVaadinSession(VaadinSession.getCurrent());

        day.add(new Span(dayLabel), container);
        day.addClassName("day");
        return day;
    }

    public List<Form.FormEntry> getEntries(Form form) {
        List<Form.FormEntry> result = new ArrayList<>();
        for (DayEntry It : entries) {
            result.add(It.createFormEntry(form));
        }

        return result;
    }

    public String getLocalDayName() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, UI.getCurrent().getLocale());
    }

    public LocalDate getDate() {
        return date;
    }
}
