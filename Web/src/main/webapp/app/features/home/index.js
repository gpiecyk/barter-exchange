import angular from 'angular';
import uirouter from 'angular-ui-router';
import routing from './home.routes';
import homeController from './home.controller';
import pagination from  './directives/pagination';
import offersList from  './directives/offers_list';

export default angular.module('app.home', [uirouter]).config(routing)
    .controller("HomeController", homeController)
    .directive("pagination", pagination)
    .directive("offersList", offersList)
    .name;