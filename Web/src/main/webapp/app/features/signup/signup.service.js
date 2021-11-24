export default class SignUpService {
    constructor($resource, $state, LoginService, UserService, $mdDialog) {
        this.$resource = $resource;
        this.$state = $state;
        this.loginService = LoginService;
        this.userService = UserService;
        this.$mdDialog = $mdDialog;
        this.user = null;
        this.sending = false;
    }

    registerUser(user) {
        this.user = user;
        let userApi = this.$resource('http://localhost:8080/user/register');
        let signup = this;
        userApi.save(user, function() {
            signup.sending = false;
            signup.login(user);
        }, function() {
            signup.sending = false;
            signup.$mdDialog.show(
                signup.$mdDialog.alert()
                    .clickOutsideToClose(true)
                    .title('User already exists')
                    .textContent('You have to provide another email address :(')
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Got it!')
            );
        });
    }

    login(user) {
        this.loginService.login(user).then((response) => this.onSuccessLoginCallback(response))
            .catch((response) => this.onErrorLoginCallback(response));
    };

    onSuccessLoginCallback(response) {
        localStorage.setItem("session", this.user.email);
        this.loginService.setLoggedIn();
        this.$state.go('home');
        this.userService.getUser(this.user.email);
        this.showConfirmDialog();
    }

    showConfirmDialog() {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Confirmation')
                .textContent("Check your email and confirm your account")
                .ariaLabel('Alert Dialog')
                .ok('Got it!')
        );
    }

    onErrorLoginCallback(response) {
        this.$mdDialog.show(
            signup.$mdDialog.alert()
                .parent(angular.element(document.querySelector('#signupContainer')))
                .clickOutsideToClose(true)
                .title('Login error has occured')
                .textContent('Something is wrong :(')
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
                .targetEvent(ev)
        );
    }
}