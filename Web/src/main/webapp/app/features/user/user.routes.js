export default function routes($stateProvider) {
    $stateProvider
        .state('user', {
            url: '/user',
            template: require('./user.html'),
            controller: 'UserController',
            controllerAs: 'user'
        })
        .state('user.myoffers', {
            url: '/myoffers',
            template: require('./myoffers/user.myoffers.html'),
            controller: 'MyOffersController',
            controllerAs: 'of',
            onExit: function(UserService) {
                UserService.showWelcomeText = true;
            }
        })
        .state('user.watchList', {
            url: '/watch/list',
            template: require('./watch-list/user.watch-list.html'),
            controller: 'WatchListController',
            controllerAs: 'watch',
            onExit: function(UserService) {
                UserService.showWelcomeText = true;
            }
        })
        .state('user.settings', {
            url: '/settings',
            template: require('./settings/user.settings.html'),
            controller: 'SettingsController',
            controllerAs: 'settings',
            onExit: function(UserService) {
                UserService.showWelcomeText = true;
            }
        })
        .state('user.pending', {
            url: '/pending',
            template: require('./pending/user.pending.html'),
            controller: 'PendingController',
            controllerAs: 'pending',
            onExit: function(UserService) {
                UserService.showWelcomeText = true;
            }
        })
        .state('user.request', {
            url: '/request',
            template: require('./request/user.request.html'),
            controller: 'RequestController',
            controllerAs: 'request',
            onExit: function(UserService) {
                UserService.showWelcomeText = true;
            }
        })
        .state('changePassword', {
            url: '/change/password?token',
            template: require('./change.password.html'),
            controller: 'ChangePasswordController',
            controllerAs: 'password'
        });
}
