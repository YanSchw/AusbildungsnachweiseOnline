package com.conleos.data.validation;

import com.conleos.data.entity.User;
import com.conleos.data.service.UserService;

import java.time.LocalDate;

public class UserDataValidation {

    public static class Result {
        public boolean isValid;
        public String errorMsg;

        public Result() {
            isValid = true;
            errorMsg = null;
        }

        public Result(String error) {
            isValid = false;
            errorMsg = error;
        }
    }

    public static Result validateNewUserData(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return new Result("Username should not be empty!");
        }
        if (UserService.getInstance().getUserByUsername(user.getUsername()) != null) {
            return new Result("Username '" + user.getUsername() + "' already exists!");
        }

        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return new Result("FirstName should not be empty!");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return new Result("LastName should not be empty!");
        }

        if (user.getRole() == null) {
            return new Result("A Role should be selected!");
        }

        if (user.getBirthday() == null) {
            return new Result("Birthday should not be empty!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            return new Result("Birthday is invalid!");
        }

        return new Result();
    }

}