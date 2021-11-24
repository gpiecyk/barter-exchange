export default class TransactionService {
    constructor($resource, $q) {
        this.$resource = $resource;
        this.$q = $q;
        this.transactions = [];
        this.transaction = null;
    }

    findPendingTransactions(offeringId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/transaction/find/pending');
        offersAPI.query({offeringId: offeringId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    findRequestTransactions(publisherId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/transaction/find/request');
        offersAPI.query({publisherId: publisherId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    acceptTransaction(transactionId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/transaction/acceptTransaction');
        offersAPI.get({transactionId: transactionId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    rejectTransaction(transactionId) {
        let deferredObject = this.$q.defer();
        let offersAPI = this.$resource('http://localhost:8080/transaction/rejectTransaction');
        offersAPI.get({transactionId: transactionId}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }
}
