export default class TransactionController {
    constructor(UserService, LoginService, TransactionService, $mdDialog, Lightbox) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.transactionService = TransactionService;
        this.Lightbox = Lightbox;
        this.$mdDialog = $mdDialog;
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

    openLightboxModal(index) {
        this.Lightbox.openModal(this.transactionService.transaction.urls, index);
    }
}
