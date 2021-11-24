export default class LoginController {
    constructor(LoginService, $mdDialog, $state, UserService) {
        this.loginService = LoginService;
        this.$mdDialog = $mdDialog;
        this.$state = $state;
        this.userService = UserService;
        this.loginError = false;
        this.resetPasswordError = false;
        this.resetPasswordSuccess = false;
        this.forgotPassword = false;
        this.sending = false;
        this.showInvisibleLine = true;
    }
    
    register() {
        this.$mdDialog.hide();
        this.$state.go("signup", {location: true, notify: false, reload: false});
    }

    cancel() {
        this.$mdDialog.hide();
    }

    login() {
        this.loginService.login(this.user).then((response) => this.onSuccessLoginCallback(response))
            .catch((response) => this.onErrorLoginCallback(response));
    }

    onSuccessLoginCallback(response) {
        this.loginError = false;
        localStorage.setItem("session", this.user.email);
        this.loginService.setLoggedIn();
        if (this.$state.current.name === 'signup') {
            this.$state.go('home');
        }
        this.userService.getUser(this.user.email);
        this.$mdDialog.hide();
    }
    
    onErrorLoginCallback(response) {
        this.loginError = true;
    }
    
    resetPassword() {
        if (this.resetPasswordError === true || this.resetPasswordSuccess === true) {
            this.resetPasswordError = this.resetPasswordSuccess = false;
            this.showInvisibleLine = true;
        }
        this.sending = true;
        this.loginService.resetPassword(this.user.email).then((response) => this.onSuccessResetPasswordCallback(response))
            .catch((response) => this.onErrorResetPasswordCallback(response));
    }

    onSuccessResetPasswordCallback(response) {
        this.sending = false;
        this.showInvisibleLine = false;
        this.resetPasswordError = false;
        this.resetPasswordSuccess = true;
    }

    onErrorResetPasswordCallback(response) {
        this.sending = false;
        this.showInvisibleLine = false;
        this.resetPasswordSuccess = false;
        this.resetPasswordError = true;
    }
}