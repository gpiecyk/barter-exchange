export default class OfferDetailsController {
    constructor(UserService, LoginService, $mdDialog, Lightbox, OfferService, $stateParams, $scope, $state) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.$mdDialog = $mdDialog;
        this.Lightbox = Lightbox;
        this.offerService = OfferService;
        this.$stateParams = $stateParams;
        this.$scope = $scope;
        this.$state = $state;
        this.loadingOffer = true;
        this.isUserObservingOfferFlag = false;
        this.offerService.userAlreadyMadeAnOffer = false;
        this.checkLoggedUser();
        this.getOfferById();
        this.$scope.$on("checkIfUserIsObservingOffer", () => this.isUserObservingOffer());
        this.$scope.$on("checkIfUserAlreadyMadeAnOffer", () => this.checkIfUserAlreadyMadeAnOffer());
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    getOfferById() {
        this.offerService.getOfferById(this.$stateParams.offerId).then((response) => this.onSuccessGetOfferHandler(response),
            (response) => this.onErrorGetOfferHandler(response));
    }

    onSuccessGetOfferHandler(response) {
        this.loadingOffer = false;
        this.offerService.selected = response;
        this.offerService.selected.endDate = new Date(this.offerService.selected.endDate);
        if (this.loginService.isLoggedIn()) {
            this.isUserObservingOffer();
            this.checkIfUserAlreadyMadeAnOffer();
        }
    }

    onErrorGetOfferHandler(response) {
        this.loadingOffer = false;
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Error')
                .textContent("Offer does not exist")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    openLightboxModal(index) {
        this.Lightbox.openModal(this.offerService.selected.urls, index);
    }

    isUsersOffer() {
        if (this.offerService.selected === null) {
            return;
        }
        return this.loginService.isLoggedIn() && this.offerService.selected.email === localStorage.getItem('session');
    }

    showDeleteConfirmationDialog() {
        let confirm = this.$mdDialog.confirm()
            .title('Delete Offer')
            .textContent('Are you sure you want to delete this offer?')
            .ariaLabel('Confirm Dialog')
            .ok('Yes')
            .cancel('No');

        this.$mdDialog.show(confirm).then(() => this.deleteOffer());
    }

    deleteOffer() {
        this.offerService.deleteOffer(this.offerService.selected.id).then((response) => this.onSuccessDeleteHandler(response),
            (response) => this.onErrorDeleteHandler(response));
    }

    onSuccessDeleteHandler(response) {
        let indexToDelete = -1;
        for (let i = 0; i < this.offerService.offers.length; i++) {
            if (this.offerService.offers[i].id === this.offerService.selected.id) {
                indexToDelete = i;
                break;
            }
        }

        if (indexToDelete !== -1) {
            this.offerService.offers.splice(indexToDelete, 1);
        }
        this.offerService.selected = null;
        if (this.offerService.offers.length === 0) {
            this.findOffersPaginated(this.activePage - 1, this.sizeOfOffers);
        }
        if (this.$state.current.name === 'search.offerDetails') {
            this.$state.go('search');
        } else {
            this.$state.go('home');
        }
    }

    onErrorDeleteHandler(response) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Failed')
                .textContent("Something went wrong :(")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    showEditModeForOffer() {
        let offer = JSON.parse(JSON.stringify(this.offerService.selected));
        this.offerService.editOffer = {};
        this.offerService.editOffer.id = offer.id;
        this.offerService.editOffer.title = offer.title;
        this.offerService.editOffer.description = offer.description;
        this.offerService.editOffer.category = offer.category;
        this.offerService.editOffer.endDate = new Date(this.offerService.selected.endDate);
        this.offerService.editOffer.email = offer.email;
        this.offerService.editOffer.urls = [];

        this.$mdDialog.show({
            controller: 'OfferController',
            templateUrl: './features/offer/offer.edit.html',
            parent: angular.element(document.body),
            clickOutsideToClose:false
        });
    }

    addToWatchList() {
        if (this.loginService.isLoggedIn()) {
            this.offerService.addToWatchList(this.offerService.selected.id, this.userService.user.id).then((response) => this.onSuccessAddToWatchListHandler(response),
                (response) => this.onErrorAddToWatchListHandler(response));
        } else {
            this.showLogin();
        }
    }

    onSuccessAddToWatchListHandler(response) {
        this.isUserObservingOfferFlag = true;
    }

    onErrorAddToWatchListHandler(response) {
        this.isUserObservingOfferFlag = response.status === 403;
    }

    showLogin() {
        this.$mdDialog.show({
            controller: 'LoginController',
            templateUrl: './features/login/login.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }

    isUserObservingOffer() {
        if (this.offerService.selected !== null) {
            this.offerService.isUserObservingOffer(this.offerService.selected.id, this.userService.user.id).then((response) => this.onSuccessCheckWatchListHandler(response),
                (response) => this.onErrorCheckWatchListHandler(response));
        }
    }

    onSuccessCheckWatchListHandler(response) {
        this.isUserObservingOfferFlag = true;
    }

    onErrorCheckWatchListHandler(response) {
        this.isUserObservingOfferFlag = false;
    }

    removeFromWatchList() {
        this.offerService.removeFromWatchList(this.offerService.selected.id, this.userService.user.id).then((response) => this.onSuccessRemoveFromWatchListHandler(response),
            (response) => this.onErrorRemoveFromWatchListHandler(response));
    }

    onSuccessRemoveFromWatchListHandler(response) {
        this.isUserObservingOfferFlag = false;
    }

    onErrorRemoveFromWatchListHandler(response) {
        this.isUserObservingOfferFlag = true;
    }

    showAskQuestionDialog() {
        this.$mdDialog.show({
            controller: 'OfferQuestionController',
            templateUrl: './features/offer/offer.question.html',
            parent: angular.element(document.body),
            clickOutsideToClose:false
        });
    }

    showMakeAnOfferDialog() {
        if (this.loginService.isLoggedIn()) {
            this.$mdDialog.show({
                controller: 'MakeAnOfferController',
                templateUrl: './features/offer/offer.make.html',
                parent: angular.element(document.body),
                clickOutsideToClose: false
            });
        } else {
            this.showLogin();
        }
    }

    checkIfUserAlreadyMadeAnOffer() {
        if (this.offerService.selected !== null) {
            this.offerService.checkIfUserAlreadyMadeAnOffer(this.userService.user.id, this.offerService.selected.id).then((response) => this.onSuccessCheckUserMadeAnOfferHandler(response),
                (response) => this.onErrorCheckUserMadeAnOfferHandler(response));
        }
    }

    onSuccessCheckUserMadeAnOfferHandler(response) {
        this.offerService.userAlreadyMadeAnOffer = response.statusValue === 200;
    }

    onErrorCheckUserMadeAnOfferHandler(response) {
        this.offerService.userAlreadyMadeAnOffer = false;
    }

    goToPendingTransactionsPage() {
        this.$state.go('user.pending');
    }
}