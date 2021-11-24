export default class SignUpController {
    constructor(SignUpService, UserService, LoginService) {
        this.signUpService = SignUpService;
        this.userService = UserService;
        this.loginService = LoginService;
        this.sending = false;
        this.checkLoggedUser();
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    register(user) {
        this.signUpService.sending = true;
        this.signUpService.registerUser(user);
    }
}