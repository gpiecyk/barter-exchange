import angular from 'angular';
import uirouter from 'angular-ui-router';
import routing from './error.routes';
import errorController from './error.controller';

export default angular.module('app.error', [uirouter]).config(routing)
    .controller("ErrorController", errorController).name;