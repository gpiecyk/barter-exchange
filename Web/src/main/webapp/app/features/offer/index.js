import angular from 'angular';
import uirouter from 'angular-ui-router';
import routing from './offer.routes';
import offerController from './offer.controller';
import offerDetailsController from './offer.details.controller';
import offerQuestionController from './offer.question.controller';
import makeAnOfferController from './offer.make.controller';
import offerService from './offer.service';

export default angular.module('app.offer', [uirouter]).config(routing)
    .controller("OfferController", offerController)
    .controller("OfferDetailsController", offerDetailsController)
    .controller("OfferQuestionController", offerQuestionController)
    .controller("MakeAnOfferController", makeAnOfferController)
    .service("OfferService", offerService).name;