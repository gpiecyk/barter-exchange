export default class HomeController {
    constructor(UserService, LoginService, $resource, $mdDialog, Lightbox, OfferService, $state, $stateParams) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.$resource = $resource;
        this.$mdDialog = $mdDialog;
        this.Lightbox = Lightbox;
        this.offerService = OfferService;
        this.$state = $state;
        this.$stateParams = $stateParams;
        this.checkLoggedUser();
        this.loading = true;
        this.offerService.offers = [];
        this.pages = [];
        this.offerService.selected = null;
        this.sizeOfOffers = 10;
        this.showPaginationButtons = false;
        this.loadingSideOffers = false;
        this.setActivePage(1);
        this.findOffers();
    }

    findOffers() {
        if (this.$stateParams.title == null) {
            this.findOffersPaginated(1, this.sizeOfOffers);
        } else {
            this.findSearchOffersPaginated(1, this.sizeOfOffers, this.$stateParams.title);
        }
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    setActivePage(page) {
        this.activePage = page;
        localStorage.setItem("activePage", this.activePage);
    }

    findOffersPaginated(page, size) {
        if (page === 1 || (page > 0 && page <= this.pages.length)) {
            this.loadingSideOffers = true;
            this.setActivePage(page);
            this.offerService.getOffersPaginated(page, size).then((response) => this.onSuccessGetOfferHandler(response, page),
                (response) => this.onErrorGetOfferHandler(response));
        }
    }

    onSuccessGetOfferHandler(response, page) {
        this.getCount();
        this.offerService.offers = response;
        this.loading = false;
        this.loadingSideOffers = false;
        if (page !== 1) {
            this.offerService.selected = null;
            this.removePartOfUrlAfterPagination();
        }
    }

    removePartOfUrlAfterPagination() {
        if (this.$stateParams.title == null) {
            this.$state.go('home');
        } else {
            this.$state.go('search', {title: this.$stateParams.title});
        }
    }

    getCount() {
        if (this.$stateParams.title == null) {
            this.getOffersCount();
        } else {
            this.getSearchOfferCount();
        }
    }

    onErrorGetOfferHandler(response) {
        this.loading = false;
        this.loadingSideOffers = false;
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Failed')
                .textContent("Something went wrong :(")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    getOffersCount() {
        this.offerService.getOffersCount().then((response) => this.onSuccessGetOfferCountHandler(response),
            (response) => this.onErrorGetOfferCountHandler(response));
    }

    onSuccessGetOfferCountHandler(response) {
        this.pages = [];
        let offersCount = response / this.sizeOfOffers;
        for (let i = 0; i < offersCount; i++) {
            this.pages.push(i + 1);
        }
        if (response > this.sizeOfOffers) {
            this.showPaginationButtons = true;
        }
    }

    onErrorGetOfferCountHandler(response) {
        this.loading = false;
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Failed')
                .textContent("Something went wrong :(")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    getSearchOfferCount() {
        this.offerService.getSearchOffersCount(this.$stateParams.title).then((response) => this.onSuccessGetOfferCountHandler(response),
            (response) => this.onErrorGetOfferCountHandler(response));
    }

    filterPagination(value, index, array) {
        let activePage = parseInt(localStorage.getItem("activePage"));
        if (activePage > 3 && activePage <= array.length - 2) {
            return ((value >= activePage - 2) && (value <= activePage + 2));
        } else if (activePage > array.length - 2) {
            return value > array.length - 5;
        } else {
            return value <= 5;
        }
    }

    findSearchOffersPaginated(page, size, titlePattern) {
        if (page === 1 || (page > 0 && page <= this.pages.length)) {
            this.loadingSideOffers = true;
            this.setActivePage(page);
            this.offerService.getSearchOffersPaginated(page, size, titlePattern).then((response) => this.onSuccessGetOfferHandler(response, page),
                (response) => this.onErrorGetOfferHandler(response));
        }
    }
}