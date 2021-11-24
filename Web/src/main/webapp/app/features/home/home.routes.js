export default function routes($stateProvider) {
    $stateProvider
        .state('home', {
            url: '/',
            template: require('./home.html'),
            controller: 'HomeController',
            controllerAs: 'home'
        })
        .state('search', {
            url: '/search?title',
            template: require('./home.html'),
            controller: 'HomeController',
            controllerAs: 'home'
        })
        .state('home.offerDetails', {
            url: 'offer/{offerId:int}',
            template: require('../offer/offer.details.html'),
            controller: 'OfferDetailsController',
            controllerAs: 'offerDetails'
        })
        .state('search.offerDetails', {
            url: '/offer/{offerId:int}',
            template: require('../offer/offer.details.html'),
            controller: 'OfferDetailsController',
            controllerAs: 'offerDetails'
        });
}