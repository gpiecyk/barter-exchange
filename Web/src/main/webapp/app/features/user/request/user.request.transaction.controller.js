export default class TransactionRequestController {
    constructor(UserService, LoginService, TransactionService, $mdDialog, Lightbox) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.transactionService = TransactionService;
        this.Lightbox = Lightbox;
        this.$mdDialog = $mdDialog;
        this.sending = false;
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

    acceptTransaction() {
        this.sending = true;
        this.transactionService.acceptTransaction(this.transactionService.transaction.id).then((response) => this.onSuccessAcceptTransactionHandler(response),
            (response) => this.onErrorAcceptTransactionHandler(response));
    }

    onSuccessAcceptTransactionHandler(response) {
        this.sending = false;
        this.deleteTransactionFromArray();
        this.deleteTransactionsForOffer();
        this.showMessageDialog('Success', 'You successfully accepted the offer');
    }

    deleteTransactionFromArray() {
        let indexToDelete = -1;
        for (let i = 0; i < this.transactionService.transactions.length; i++) {
            if (this.transactionService.transactions[i].id === this.transactionService.transaction.id) {
                indexToDelete = i;
                break;
            }
        }

        if (indexToDelete !== -1) {
            this.transactionService.transactions.splice(indexToDelete, 1);
        }
    }

    deleteTransactionsForOffer() {
        for (let i = 0; i < this.transactionService.transactions.length; i++) {
            if (this.transactionService.transactions[i].offerId === this.transactionService.transaction.offerId) {
                this.transactionService.transactions.splice(i, 1);
            }
        }
    }

    onErrorAcceptTransactionHandler(response) {
        this.sending = false;
        this.showMessageDialog('Error', 'Transaction does not exists');
    }

    rejectTransaction() {
        this.sending = true;
        this.transactionService.rejectTransaction(this.transactionService.transaction.id).then((response) => this.onSuccessRejectTransactionHandler(response),
            (response) => this.onErrorRejectTransactionHandler(response));
    }

    onSuccessRejectTransactionHandler(response) {
        this.sending = false;
        this.deleteTransactionFromArray();
        this.showMessageDialog('Success', 'You successfully rejected the offer');
    }

    onErrorRejectTransactionHandler(response) {
        this.sending = false;
        this.showMessageDialog('Error', 'Transaction does not exists');
    }

    showMessageDialog(title, content) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title(title)
                .textContent(content)
                .ariaLabel('Message dialog')
                .ok('Got it!')
        );
    }
}
