export default class AppController {
    constructor($mdDialog, LoginService, UserService, OfferService, $state) {
        this.$mdDialog = $mdDialog;
        this.loginService = LoginService;
        this.userService = UserService;
        this.offerService = OfferService;
        this.$state = $state;
    }

    showLogin() {
        this.$mdDialog.show({
            controller: 'LoginController',
            templateUrl: './features/login/login.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }

    logout() {
        this.loginService.logout().then((response) => this.onSuccessLogoutCallback(response))
            .catch((response) => this.onErrorLogoutCallback(response));
    }

    onSuccessLogoutCallback(response) {
        localStorage.removeItem("session");
        this.loginService.setLoggedIn();
        this.$state.go('home');
        this.userService.user = null;
        this.offerService.selected = null;
        this.offerService.userAlreadyMadeAnOffer = false;
    }

    onErrorLogoutCallback(response) {
        console.log("Something went wrong with logging out :(");
        this.$state.go('error');
    }

    resetSelectedOffer() {
        this.offerService.selected = null;
    }

    querySearch(query) {
        return query ? this.titles : [];
    }

    searchTextChange(text) {
        this.findSearchOfferTitles(text);
    }

    findSearchOfferTitles(text) {
        this.offerService.findSearchOfferTitles(text).then((response) => this.onSuccessGetOfferTitlesHandler(response),
            (response) => this.onErrorGetOfferTitlesHandler(response));
    }

    onSuccessGetOfferTitlesHandler(response) {
        this.titles = response.map(function (state) {
            return {
                value: state.toLowerCase(),
                display: state
            }
        });
    }

    onErrorGetOfferTitlesHandler(response) {
        console.log("Error getting titles");
    }

    selectedItemChange(item) {
        if (item != null) {
            this.$state.go('search', {title: item.display});
        }
    }
}