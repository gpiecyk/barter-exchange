import 'bootstrap/dist/css/bootstrap.css';
import 'angular-material/angular-material.min.css';
import './css/style.css';

import angular from 'angular';
import uirouter from 'angular-ui-router';
import resource from 'angular-resource';
import angularAnimate from 'angular-animate';
import angularAria from 'angular-aria';
import angularMaterial from 'angular-material';
import firebase from 'firebase';
import angularFire from 'angularfire';
import routing from './app.config';
import theme from './app.theme'
import appController from './app.controller';
import angularBootstrap from 'angular-ui-bootstrap';

import 'lf-ng-md-file-input/dist/lf-ng-md-file-input.min.css';
import 'lf-ng-md-file-input/dist/lf-ng-md-file-input.js';
import 'angular-bootstrap-lightbox/dist/angular-bootstrap-lightbox.min.js';
import 'angular-bootstrap-lightbox/dist/angular-bootstrap-lightbox.min.css';

import home from './features/home';
import user from './features/user';
import login from './features/login';
import singUp from './features/signup';
import error from './features/error';
import offer from './features/offer';

// TODO initialize this config
const config = {
    apiKey: "",
    authDomain: "your-firebase-url.firebaseapp.com",
    databaseURL: "https://your-firebase-url.firebaseio.com",
    storageBucket: "your-firebase-url.appspot.com",
    messagingSenderId: "number"
};
firebase.initializeApp(config);
const rootRef = firebase.database().ref();

const ngModule = angular.module('app', [angularFire, uirouter, resource, angularMaterial, angularBootstrap, 'lfNgMdFileInput', 'bootstrapLightbox',
    home, user, login, singUp, error, offer])
    .config(routing).config(theme)
    .value("firebaseRootRef", rootRef)
    .controller("AppController", appController);
