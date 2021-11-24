export default class MyOffersController {
    constructor(OfferService, UserService, LoginService, $mdDialog) {
        this.offerService = OfferService;
        this.userService = UserService;
        this.loginService = LoginService;
        this.$mdDialog = $mdDialog;
        this.userService.showWelcomeText = false;
        this.checkLoggedUser();
        if (this.loginService.isLoggedIn()) {
            this.loadingOffers = true;
            this.findOffersByEmail();
        }
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    findOffersByEmail() {
        let email = localStorage.getItem("session");
        this.offerService.getOffersByEmail(email).then((response) => this.onSuccessGetOfferHandler(response),
            (response) => this.onErrorGetOfferHandler(response));
    }

    onSuccessGetOfferHandler(response) {
        this.offers = response;
        this.loadingOffers = false;
    }

    onErrorGetOfferHandler(response) {
        this.loadingOffers = false;
        if (response.status !== 400) {
            this.$mdDialog.show(
                this.$mdDialog.alert()
                    .clickOutsideToClose(true)
                    .title('Failed')
                    .textContent("You are not authorized. Please log in.")
                    .ariaLabel('Alert Dialog Demo')
                    .ok('Got it!')
            );
        }
    }
}
