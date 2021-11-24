export default class ConfirmationController {
    constructor($resource, $state, $mdDialog, UserService, LoginService) {
        this.$resource = $resource;
        this.$state = $state;
        this.$mdDialog = $mdDialog;
        this.sending = false;
        this.status = 0;
        this.userService = UserService;
        this.loginService = LoginService;
        this.confirmRegistration();
        this.checkLoggedUser();
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    confirmRegistration() {
        let confirmAPI = this.$resource('http://localhost:8080/user/registration/confirm');
        confirmAPI.get({token: this.$state.params.token}).$promise.then((response) => this.onSuccess(response),
            (response) => this.onError(response));
    }

    onSuccess(response) {
        this.status = 200;
    }

    onError(response) {
        this.status = response.status;
    }

    resendConfirmation() {
        this.sending = true;
        let resendAPI = this.$resource('http://localhost:8080/user/registration/confirm/resend');
        resendAPI.get({token: this.$state.params.token}).$promise.then((response) => this.onSuccessResend(response),
            (response) => this.onErrorResend(response));
    }

    onSuccessResend(response) {
        this.sending = false;
        let confirm = this;
        confirm.$mdDialog.show(
            confirm.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Email sent')
                .textContent('Check your email and activate your account :)')
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    onErrorResend(response) {
        this.sending = false;
        let confirm = this;
        confirm.$mdDialog.show(
            confirm.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Failed')
                .textContent("Something went wrong :(")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }
}