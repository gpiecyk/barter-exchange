export default class RequestController {
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
            this.findRequestTransactions();
        }
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    findRequestTransactions() {
        let publisherId = this.userService.user.id;
        this.transactionService.findRequestTransactions(publisherId).then((response) => this.onSuccessFindRequestTransactionsHandler(response),
            (response) => this.onErrorFindRequestTransactionsHandler(response));
    }

    onSuccessFindRequestTransactionsHandler(response) {
        this.transactionService.transactions = response;
        this.loadingTransactions = false;
    }

    onErrorFindRequestTransactionsHandler(response) {
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
            controller: 'TransactionRequestController',
            controllerAs: 'trn',
            templateUrl: './features/user/request/user.request.transaction.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }
}
