export default class UserController {
    constructor(OfferService, UserService, LoginService) {
        this.offerService = OfferService;
        this.userService = UserService;
        this.loginService = LoginService;
        this.userService.showWelcomeText = true;
        this.checkLoggedUser();
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }
}
