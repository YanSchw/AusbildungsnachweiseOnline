package com.conleos.views.dashboard;

import com.conleos.data.entity.FormStatus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.List;

public class DashboardFormFilter {

    private final Button button;
    private Dialog dialog;
    private final Button applyFiltersButton;
    private boolean hideSignedForms = true;

    public DashboardFormFilter() {
        button = new Button("Filter", VaadinIcon.FILTER.create());
        button.addClickListener(event -> dialog.open());
        dialog = new Dialog("Set Filters");
        dialog.setWidth("45%");

        Checkbox hideSignedFormsCheckBox = new Checkbox("Hide Signed Forms");
        hideSignedFormsCheckBox.setValue(hideSignedForms);
        dialog.add(hideSignedFormsCheckBox);

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> {
            dialog.close();
            hideSignedFormsCheckBox.setValue(hideSignedForms);
        });
        applyFiltersButton = new Button("Apply");
        applyFiltersButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyFiltersButton.addClickListener(event -> {
            dialog.close();
            hideSignedForms = hideSignedFormsCheckBox.getValue();
        });
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(applyFiltersButton);
    }

    public void filterAndSortFormCards(List<FormCard> cards) {
        if (hideSignedForms) {
            cards.removeIf(formCard -> formCard.getForm() != null && formCard.getForm().getStatus().equals(FormStatus.Signed));
        }
    }

    public Button getButton() {
        return button;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Button getApplyFiltersButton() {
        return applyFiltersButton;
    }


}
