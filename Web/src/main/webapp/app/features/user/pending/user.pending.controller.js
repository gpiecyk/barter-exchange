export default class PendingController {
    constructor(OfferService, UserService, LoginService, TransactionService, $mdDialog) {
        this.offerService = OfferService;
        this.userService = UserService;
        this.loginService = LoginService;
        this.transactionService = TransactionService;
        this.$mdDialog = $mdDialog;
        this.userService.showWelcomeText = false;
        this.checkLoggedUser();
        if (this.loginService.isLoggedIn()) {
            this.loadingTransactions = true;
            this.findPendingTransactions();
        }
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    findPendingTransactions() {
        let offeringId = this.userService.user.id;
        this.transactionService.findPendingTransactions(offeringId).then((response) => this.onSuccessFindPendingTransactionsHandler(response),
            (response) => this.onErrorFindPendingTransactionsHandler(response));
    }

    onSuccessFindPendingTransactionsHandler(response) {
        this.transactions = response;
        this.loadingTransactions = false;
    }

    onErrorFindPendingTransactionsHandler(response) {
        this.loadingTransactions = false;
        if (response.status !== 400) {
            this.$mdDialog.show(
                this.$mdDialog.alert()
                    .clickOutsideToClose(true)
                    .title('Failed')
                    .textContent("You are not authorized. Please log in.")
                    .ariaLabel('Alert Dialog')
                    .ok('Got it!')
            );
        }
    }

    showDetails(transaction) {
        this.transactionService.transaction = JSON.parse(JSON.stringify(transaction));

        this.$mdDialog.show({
            controller: 'TransactionController',
            controllerAs: 'trn',
            templateUrl: './features/user/pending/user.pending.transaction.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }
}
