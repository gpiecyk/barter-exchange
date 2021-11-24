export default class OfferQuestionController {
    constructor(UserService, LoginService, OfferService, $mdDialog) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.offerService = OfferService;
        this.$mdDialog = $mdDialog;
        this.sending = false;
        if (this.loginService.isLoggedIn()) {
            this.email = this.userService.user.email;
        }
    }

    cancel() {
        this.$mdDialog.hide();
    }

    sendQuestion() {
        this.sending = true;
        this.offerService.sendQuestion(this.email, this.offerService.selected, this.message)
            .then((response) => this.onSuccessSendQuestionHandler(response), (response) => this.onErrorSendQuestionHandler(response));
    }

    onSuccessSendQuestionHandler(response) {
        this.sending = false;
        this.showConfirmDialog();
    }

    showConfirmDialog() {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Success')
                .textContent("Your question has been sent")
                .ariaLabel('Alert Dialog')
                .ok('Got it!')
        );
    }

    onErrorSendQuestionHandler(response) {
        this.sending = false;
        this.showErrorDialog();
    }

    showErrorDialog() {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Error')
                .textContent("Your question has not been sent :(")
                .ariaLabel('Alert Dialog')
                .ok('Got it!')
        );
    }
}