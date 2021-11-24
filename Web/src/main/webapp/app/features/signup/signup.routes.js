export default function routes($stateProvider) {
    $stateProvider
        .state('signup', {
            url: '/signup',
            template: require('./signup.html'),
            controller: 'SignUpController',
            controllerAs: 'signUp'
        })
        .state('registrationConfirm', {
            url: '/registration/confirm?token',
            template: require('./confirmation.html'),
            controller: 'ConfirmationController',
            controllerAs: 'confirm'
        });
}