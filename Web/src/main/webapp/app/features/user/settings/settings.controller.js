export default class SettingsController {
    constructor(UserService, LoginService, $mdDialog, $state, OfferService) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.$mdDialog = $mdDialog;
        this.$state = $state;
        this.offerService = OfferService;
        this.userService.showWelcomeText = false;
        this.checkLoggedUser();
        if (this.loginService.isLoggedIn()) {
            this.getUser();
        }
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    getUser() {
        let email = localStorage.getItem("session");
        this.userService.getUserByEmail(email).then((response) => this.onSuccessGetUserHandler(response),
            (response) => this.onErrorGetUserHandler(response));
    }

    onSuccessGetUserHandler(response) {
        this.userService.user = response;
    }

    onErrorGetUserHandler(response) {
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

    showEditModeForUserInfo() {
        this.repackUserData();

        this.$mdDialog.show({
            controller: 'SettingsEditController',
            templateUrl: './features/user/settings/user.settings.edit.info.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }

    showEditModeForUserAddress() {
        this.repackUserData();

        this.$mdDialog.show({
            controller: 'SettingsEditController',
            templateUrl: './features/user/settings/user.settings.edit.address.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }

    repackUserData() {
        this.userService.editUser = JSON.parse(JSON.stringify(this.userService.user));
    }

    showEditModeForPassword() {
        this.$mdDialog.show({
            controller: 'SettingsEditController',
            templateUrl: './features/user/settings/user.settings.edit.password.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
        });
    }

    showDeleteUserConfirmationDialog() {
        let confirm = this.$mdDialog.confirm()
            .title('Delete Account')
            .textContent('Are you sure you want to delete your account?')
            .ariaLabel('Confirm Dialog')
            .ok('Yes')
            .cancel('No');

        this.$mdDialog.show(confirm).then(() => this.deleteUser());
    }

    deleteUser() {
        this.userService.deleteUser(this.userService.user.id).then((response) => this.onSuccessDeleteUserHandler(response),
            (response) => this.onErrorDeleteUserHandler(response));
    }

    onSuccessDeleteUserHandler(response) {
        localStorage.removeItem("session");
        this.loginService.setLoggedIn();
        this.$state.go('home');
        this.userService.user = null;
        this.offerService.selected = null;
    }

    onErrorDeleteUserHandler(response) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Failed')
                .textContent("Something went wrong :(")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }
}
