export default class OfferController {
    constructor(UserService, LoginService, $mdDialog, OfferService) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.$mdDialog = $mdDialog;
        this.offerService = OfferService;
        this.sending = false;
        this.offer = null;
        this.checkLoggedUser();
        this.minDate = new Date();

        this.categories = [
            'Electronics & Computers',
            'Movies, Music & Games',
            'Toys, Kids & Baby',
            'Clothing, Shoes & Jewelry',
            'Handmade',
            'Automotive & Industrial',
            'Beauty & Health',
            'Home, Garden & Tools',
            'Sports & Outdoors'
        ];
    }

    checkLoggedUser() {
        if (this.loginService.isLoggedIn() && this.userService.isUserNameSet === false) {
            this.userService.getUser(localStorage.getItem("session"));
        }
    }

    addOffer() {
        if (this.validateOffer()) {
            return;
        }
        this.setupUserEmail();
        this.processOffer();
    }


    validateOffer() {
        if (typeof this.offer.endDate == 'undefined') {
            this.showErrorDialog('End Date cannot be empty', 'You have to provide end date');
            return true;
        }

        if (typeof this.offer.category == 'undefined') {
            this.showErrorDialog('Category cannot be empty', 'You have to provide category');
            return true;
        }

        if (this.photos.length === 0) {
            this.showErrorDialog('Photos cannot be empty', 'You have to provide at least one photo');
            return true;
        }
        return false;
    }

    showErrorDialog(title, content) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title(title)
                .textContent(content)
                .ariaLabel('Error dialog')
                .ok('Got it!')
        );
    }

    setupUserEmail() {
        this.offer.user = {};
        this.offer.user.email = localStorage.getItem("session");
    }

    processOffer() {
        this.offerService.sending = true;
        this.offer.pictures = [];
        if (this.photos.length > 0) {
            let pictures = this.repackPicturesIntoArray();
            this.processPicturesAndCreateOffer(pictures);
        }
    }

    repackPicturesIntoArray() {
        let pictures = [];
        angular.forEach(this.photos, function (obj) {
            pictures.push(obj.lfFile);
        });
        return pictures;
    }

    processPicturesAndCreateOffer(pictures) {
        let that = this;
        let j = 0, length = pictures.length;
        for (let i = 0; i < length; i++) {
            let fileReader = new FileReader();
            fileReader.onload = function (e) {
                let picture = {};
                picture.picture = e.target.result;
                that.offer.pictures.push(picture);
                j++;
                if (j === length) {
                    that.offerService.createOffer(that.offer);
                }
            };
            fileReader.readAsDataURL(pictures[i]);
        }
    }

    cancelEditDialog() {
        this.$mdDialog.hide();
    }

    updateOffer() {
        this.sending = true;
        this.offerService.updateOffer().then((response) => this.onSuccessUpdateHandler(response),
            (response) => this.onErrorUpdateHandler(response));
    }

    onSuccessUpdateHandler(response) {
        this.sending = false;
        this.cancelEditDialog();
        for (let i = 0; i < this.offerService.offers.length; i++) {
            if (this.offerService.offers[i].id === response.id) {
                this.offerService.offers[i] = response;
                this.offerService.selected = response;
                this.offerService.selected.endDate = new Date(response.endDate);
                this.offerService.editOffer = null;
                break;
            }
            if (i === this.offerService.offers.length-1) {
                this.offerService.selected = response;
                this.offerService.selected.endDate = new Date(response.endDate);
                this.offerService.editOffer = null;
            }
        }
    }

    onErrorUpdateHandler(response) {
        this.sending = false;
        if (response.status === 401) {
            this.showErrorDialog('Unauthorized', 'You are not authorized. Please log in.');
        } else {
            this.showErrorDialog('Error', 'Internal Server Error');
        }
    }
}