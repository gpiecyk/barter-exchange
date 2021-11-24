export default class ChangePasswordController {
    constructor($resource, $state, $mdDialog, $http) {
        this.$resource = $resource;
        this.$state = $state;
        this.$mdDialog = $mdDialog;
        this.$http = $http;
        this.sending = false;
        this.status = 0;
        this.match = true;
        this.changePassword();
    }

    changePassword() {
        let changePasswordAPI = this.$resource('http://localhost:8080/user/changePassword');
        changePasswordAPI.get({token: this.$state.params.token}).$promise.then((response) => this.onSuccess(response),
            (response) => this.onError(response));
    }

    onSuccess(response) {
        this.status = 200;
    }

    onError(response) {
        this.status = response.status;
    }

    savePassword() {
        if (this.password !== this.confirmPassword) {
            this.match = false;
        } else {
            this.match = true;
            this.sending = true;
            this.$http.post("http://localhost:8080/user/savePassword", "password=" + this.password, {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then((response) => this.onSuccessSavePassword(response),
                (response) => this.onErrorSavePassword(response));
        }
    }

    onSuccessSavePassword(response) {
        this.sending = false;
        let changePassword = this;
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
        this.sending = false;
        let changePassword = this;
        changePassword.$mdDialog.show(
            changePassword.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Error')
                .textContent("You are not authorized.")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
                .targetEvent(response)
        );
    }
}