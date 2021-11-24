export default class OfferService {
    constructor($resource, $mdDialog, $state, $q, $http) {
        this.$resource = $resource;
        this.$mdDialog = $mdDialog;
        this.$state = $state;
        this.$q = $q;
        this.$http = $http;
        this.sending = false;
        this.editOffer = null;
        this.offers = [];
        this.selected = null;
        this.userAlreadyMadeAnOffer = false;
    }

    createOffer(offer) {
        var offerApi = this.$resource('http://localhost:8080/offer/create');
        offerApi.save(offer, response => this.onCreateOfferSuccessHandler(response), response => this.onCreateOfferErrorHandler(response));
    }

    onCreateOfferSuccessHandler(response) {
        this.sending = false;
        this.showDialog('Success', 'Offer was created successfully.');
    }

    onCreateOfferErrorHandler(response) {
        this.sending = false;
        if (response.status === 401) {
            this.showDialog('Unauthorized', 'You are not authorized. Please log in.');
        } else if (response.status === 406) {
            this.showConfirmationDialog();
        } else {
            this.showDialog('Error', 'Internal Server Error');
        }
    }

    showDialog(title, content) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title(title)
                .textContent(content)
                .ariaLabel('Dialog')
                .ok('Got it!')
        );
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
            that.resendConfirmation();
        }, function() {
            console.log("Cancel");
        });
    }

    resendConfirmation() {
        var resendAPI = this.$resource('http://localhost:8080/user/registration/confirm/resend/email');
        resendAPI.get({email: localStorage.getItem("session")}).$promise.then((response) => this.onSuccessResend(response),
            (response) => this.onErrorResend(response));
    }

    onSuccessResend(response) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Email sent')
                .textContent('Check your email and activate your account :)')
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    onErrorResend(response) {
        this.$mdDialog.show(
            this.$mdDialog.alert()
                .clickOutsideToClose(true)
                .title('Failed')
                .textContent("You're fucked! :(")
                .ariaLabel('Alert Dialog Demo')
                .ok('Got it!')
        );
    }

    getOffersPaginated(page, size) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/find/paginated');
        offersAPI.query({page: page, size: size}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    getSearchOffersPaginated(page, size, titlePattern) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/find/search/paginated');
        offersAPI.query({page: page, size: size, titlePattern: titlePattern}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    getOffersByEmail(email) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/find/email');
        offersAPI.query({email: email}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    findOffersObservedByUser(userId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/find/observed');
        offersAPI.query({userId: userId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    getOfferById(id) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/' + id);
        offersAPI.get().$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    deleteOffer(id) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/delete');
        offersAPI.get({id: id}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    updateOffer() {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/update');
        offersAPI.save(this.editOffer).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    getOffersCount() {
        return this.$http.get("http://localhost:8080/offer/count").then((response) => {return response.data});
    }

    getSearchOffersCount(titlePattern) {
        return this.$http.get("http://localhost:8080/offer/search/count", {
            params: {titlePattern: titlePattern}
        }).then((response) => {return response.data});
    }

    isUserObservingOffer(offerId, userId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/isUserObservingOffer');
        offersAPI.get({offerId: offerId, userId: userId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    addToWatchList(offerId, userId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/addToWatchList?offerId=' + offerId +'&userId=' + userId);
        offersAPI.save().$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    removeFromWatchList(offerId, userId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/delete/watchList');
        offersAPI.get({offerId: offerId, userId: userId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    findSearchOfferTitles(titlePattern) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/find/search/titles');
        offersAPI.query({titlePattern: titlePattern}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    sendQuestion(senderEmail, offer, message) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/offer/sendQuestion');
        offersAPI.get({senderEmail: senderEmail, receiverEmail: offer.email, offerTitle: offer.title,
            offerId: offer.id, message: message})
            .$promise.then((response) => deferredObject.resolve(response), (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    makeAnOffer(transaction) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/transaction/makeAnOffer');
        offersAPI.save(transaction).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    checkIfUserAlreadyMadeAnOffer(offeringUserId, offerId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/transaction/userAlreadyMadeAnOffer');
        offersAPI.get({offeringUserId: offeringUserId, offerId: offerId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }
}
