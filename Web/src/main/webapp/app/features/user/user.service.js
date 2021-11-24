export default class UserService {
    constructor($resource, $state, $q, $rootScope, $firebaseObject, firebaseRootRef, $mdToast, LoginService) {
        this.$resource = $resource;
        this.$state = $state;
        this.$q = $q;
        this.$rootScope = $rootScope;
        this.$firebaseObject = $firebaseObject;
        this.firebaseRootRef = firebaseRootRef;
        this.$mdToast = $mdToast;
        this.loginService = LoginService;
        this.isUserNameSet = false;
        this.editUser = null;
    }

    getUser(email) {
        let userAPI = this.$resource('http://localhost:8080/user/email/:email/');
        userAPI.get({email: email}).$promise.then((response) => this.onSuccess(response),
            (response) => this.onError(response));
    }

    onSuccess(response) {
        this.user = response;
        this.isUserNameSet = true;
        if (this.$state.current.name === 'home.offerDetails' || this.$state.current.name === 'search.offerDetails') {
            this.$rootScope.$broadcast("checkIfUserIsObservingOffer", {});
            this.$rootScope.$broadcast("checkIfUserAlreadyMadeAnOffer", {});
        }
        this.checkNotifications(this.user.id);
    }

    onError(response) {
        if (response.status === 400) {
            this.$state.go('error');
            this.user = {};
            this.user.firstName = 'User';
        }
    }

    checkNotifications(userId) {
        let ref = this.firebaseRootRef.child('notifications').child(userId);
        this.$rootScope.notification = this.$firebaseObject(ref);
        this.$rootScope.notification.$loaded().then(data => this.onNotificationLoaded(data));
    }

    onNotificationLoaded(data) {
        if (typeof data.show != 'undefined') {
            this.$rootScope.$watch('notification', (data) => this.onChangeNotification(data), true);
        }
    }

    onChangeNotification(data) {
        if (this.loginService.isLoggedIn() && data.show === 'true') {
            this.showNotification();
            data.show = 'false';
            data.$save();
        }
    }

    showNotification() {
        this.$mdToast.show({
            hideDelay   : 10000,
            position    : 'top right',
            controller  : 'NotificationController',
            controllerAs: 'notif',
            templateUrl : './features/user/user.notification.html'
        });
    };

    getUserByEmail(email) {
        let deferredObject = this.$q.defer();
        let userAPI = this.$resource('http://localhost:8080/user/email/:email/');
        userAPI.get({email: email}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    updateUser(user) {
        let deferredObject = this.$q.defer();
        let userAPI = this.$resource('http://localhost:8080/user/update');
        userAPI.save(user).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    savePasswordFromSettings(email, password) {
        let deferredObject = this.$q.defer();
        let userAPI = this.$resource('http://localhost:8080/user/savePasswordFromSettings?password=' + password +'&email=' + email);
        userAPI.save().$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }

    deleteUser(id) {
        let deferredObject = this.$q.defer();
        let userAPI = this.$resource('http://localhost:8080/user/delete');
        userAPI.get({userId: id}).$promise.then((response) => deferredObject.resolve(response),
            (response) => deferredObject.reject(response));
        return deferredObject.promise;
    }
}
