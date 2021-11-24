import './login.css';
import angular from 'angular';
import loginController from './login.controller';
import loginService from './login.service';
import loginForm from  './directives/login_form';
import resetPassword from './directives/reset_password';

export default angular.module('app.login', [])
    .controller("LoginController", loginController)
    .directive("loginForm", loginForm)
    .directive("resetPassword", resetPassword)
    .service("LoginService", loginService)
    .name;