export default function routes($stateProvider) {
    $stateProvider
        .state('error', {
            url: '/error',
            template: require('./error.html'),
            controller: 'ErrorController',
            controllerAs: 'error'
        });
}