export default class MakeAnOfferController {
    constructor(UserService, LoginService, OfferService, $mdDialog) {
        this.userService = UserService;
        this.loginService = LoginService;
        this.offerService = OfferService;
        this.$mdDialog = $mdDialog;
        this.sending = false;
        this.photosRequired = false;
        this.transaction = {};
        this.items = [];
    }

    cancel() {
        this.$mdDialog.hide();
    }

    sendRequest() {
        this.setupTransaction();
        this.processTransaction();
    }

    setupTransaction() {
        this.transaction.offeringUser = {};
        this.transaction.publisher = {};
        this.transaction.offeringUser.email = localStorage.getItem('session');
        this.transaction.publisher.email = this.offerService.selected.email;
        this.transaction.offerIdFk = this.offerService.selected.id;
        this.transaction.itemsForExchange = this.items.join(';');
    }

    processTransaction() {
        this.transaction.pictures = [];
        if (this.photos.length > 0) {
            this.sending = true;
            let pictures = this.repackPicturesIntoArray();
            this.processPicturesAndSendRequest(pictures);
        } else {
            this.photosRequired = true;
        }
    }

    repackPicturesIntoArray() {
        let pictures = [];
        angular.forEach(this.photos, function (obj) {
            pictures.push(obj.lfFile);
        });
        return pictures;
    }

    processPicturesAndSendRequest(pictures) {
        let that = this;
        let j = 0, length = pictures.length;
        for (let i = 0; i < length; i++) {
            let fileReader = new FileReader();
            fileReader.onload = function (e) {
                let picture = {};
                picture.picture = e.target.result;
                that.transaction.pictures.push(picture);
                j++;
                if (j === length) {
                    that.offerService.makeAnOffer(that.transaction).then((response) => that.onSuccessSendRequestHandler(response),
                            (response) => that.onErrorSendRequestHandler(response));
                }
            };
            fileReader.readAsDataURL(pictures[i]);
        }
    }

    onSuccessSendRequestHandler(response) {
        this.sending = false;
        this.offerService.userAlreadyMadeAnOffer = true;
        this.showSuccessDialog();
    }

    showSuccessDialog() {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Success')
                .textContent("Your offer request has been sent")
                .ariaLabel('Alert Dialog')
                .ok('Got it!')
        );
    }

    onErrorSendRequestHandler(response) {
        this.sending = false;
        if (response.status === 406) {
            this.showConfirmationDialog();
        } else {
            this.showErrorDialog('Error', 'Your offer request has not been sent :(');
        }
    }

    showConfirmationDialog() {
        let that = this;
        let confirm = this.$mdDialog.confirm()
            .title('Inactive')
            .textContent('Your account is not active. Please activate your account.')
            .ariaLabel('Confirm Dialog')
            .ok('Send confirmation email')
            .cancel('Cancel');

        this.$mdDialog.show(confirm).then(function() {
            that.offerService.resendConfirmation();
        }, function() {
            console.log("Cancel");
        });
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
}