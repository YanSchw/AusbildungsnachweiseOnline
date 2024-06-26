package com.conleos.views.admin;

import com.conleos.common.PasswordHasher;
import com.conleos.common.Role;
import com.conleos.data.entity.User;
import com.conleos.data.service.UserService;
import com.conleos.data.validation.UserDataValidation;
import com.conleos.i18n.Lang;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateUserDialog extends Dialog {

    public CreateUserDialog() {
        setHeaderTitle(Lang.translate("view.createUser.headerTitle"));

        List<User> assignees = UserService.getInstance().getAllUsers();
        assignees.removeIf(user -> user.getRole() == Role.Trainee);

        setWidth("45%");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setWidthFull();
        add(dialogLayout);

        TextField username = new TextField(Lang.translate("view.createUser.label.username"));
        username.setWidthFull();
        PasswordField password = new PasswordField(Lang.translate("view.createUser.label.password"));
        password.setWidthFull();
        TextField firstName = new TextField(Lang.translate("view.createUser.label.firstname"));
        firstName.setWidthFull();
        TextField lastName = new TextField(Lang.translate("view.createUser.label.lastname"));
        lastName.setWidthFull();
        DatePicker birthday = new DatePicker(Lang.translate("view.createUser.label.birthday"));
        birthday.setWidthFull();
        TextField email = new TextField(Lang.translate("view.createUser.label.email"));
        email.setWidthFull();

        MultiSelectComboBox<User> assigneeSelect = new MultiSelectComboBox<>(Lang.translate("view.createUser.MultiSelectComboBox.label.assignedTo"));
        assigneeSelect.setPlaceholder(Lang.translate("view.createUser.MultiSelectComboBox.label.setPlace"));
        assigneeSelect.setItems(assignees);
        assigneeSelect.setItemLabelGenerator(User::getUsername);
        assigneeSelect.setEnabled(false);

        DatePicker startTimeSelector = new DatePicker(Lang.translate("view.createUser.timeSelector.label"));
        startTimeSelector.setValue(LocalDate.now());
        startTimeSelector.setEnabled(false);

        ComboBox<Role> roleSelect = new ComboBox<>(Lang.translate("view.createUser.comboBox.label.roleSelect"));
        roleSelect.setPlaceholder(Lang.translate("view.createUser.comboBox.label.setPlace"));
        roleSelect.setItems(Role.values());
        roleSelect.setItemLabelGenerator(Role::toString);
        roleSelect.addValueChangeListener(event -> {
            assigneeSelect.setEnabled(roleSelect.getValue().equals(Role.Trainee));
            startTimeSelector.setEnabled(roleSelect.getValue().equals(Role.Trainee));
        });

        dialogLayout.add(username, password, firstName, lastName, birthday, email, roleSelect, assigneeSelect, startTimeSelector);

        Button saveButton = new Button(Lang.translate("view.createUser.button.save"), e -> {
            User user = new User(username.getValue(), PasswordHasher.hash(password.getValue()), roleSelect.getValue(), firstName.getValue(), lastName.getValue(), birthday.getValue());
            user.setEmail(email.getValue());
            user.setAssignees(new ArrayList<>(assigneeSelect.getValue()));
            user.setStartDate(startTimeSelector.getValue());

            UserDataValidation.Result result = UserDataValidation.validateNewUserData(user);
            if (result.isValid) {
                UserService.getInstance().saveUser(user);
                close();
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show(result.errorMsg, 4000, Notification.Position.BOTTOM_START);
            }

        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button(Lang.translate("view.createUser.button.cancel"), e -> close());
        getFooter().add(cancelButton);
        getFooter().add(saveButton);
    }

}
