export default class SettingsEditController {
    constructor(UserService, LoginService, $mdDialog) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.$mdDialog = $mdDialog;
        this.sending = false;
        this.match = true;
        this.checkLoggedUser();
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    cancelEditDialog() {
        this.$mdDialog.hide();
    }

    updateUser() {
        this.sending = true;
        this.userService.updateUser(this.userService.editUser).then((response) => this.onSuccessUpdateHandler(response),
            (response) => this.onErrorUpdateHandler(response));
    }

    onSuccessUpdateHandler(response) {
        this.sending = false;
        this.cancelEditDialog();
        this.userService.user = response;
    }

    onErrorUpdateHandler(response) {
        this.sending = false;
        if (response.status === 401) {
            this.showErrorDialog('Unauthorized', 'You are not authorized. Please log in.');
        } else {
            this.showErrorDialog('Error', 'Internal Server Error');
        }
    }

    showErrorDialog(title, content) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title(title)
                .textContent(content)
                .ariaLabel('Error dialog')
                .ok('Got it!')
        );
    }

    savePassword() {
        if (this.password !== this.confirmPassword) {
            this.match = false;
        } else {
            this.match = true;
            let email = localStorage.getItem("session");
            this.userService.savePasswordFromSettings(email, this.password).then((response) => this.onSuccessSavePassword(response),
                (response) => this.onErrorSavePassword(response));
        }
    }

    onSuccessSavePassword(response) {
        var changePassword = this;
        changePassword.$mdDialog.show(
            changePassword.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Success')
                .textContent('Your password has been changed.')
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
                .targetEvent(response));
    }


    onErrorSavePassword(response) {
        this.showErrorDialog('Error', 'Internal Server Error');
    }
}
