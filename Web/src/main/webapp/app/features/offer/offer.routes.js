export default function routes($stateProvider) {
    $stateProvider
        .state('offer/add', {
            url: '/offer/add',
            template: require('./offer.html'),
            controller: 'OfferController',
            controllerAs: 'offer'
        });
}