import angular from 'angular';
import uirouter from 'angular-ui-router';
import routing from './user.routes';
import userController from './user.controller';
import userService from './user.service';
import transactionService from './user.transaction.service';
import changePasswordController from './change.password.controller';
import myOffersController from './myoffers/myoffers.controller';
import watchListController from './watch-list/watch-list.controller';
import settingsController from './settings/settings.controller';
import settingsEditController from './settings/settings.edit.controller';
import notificationController from './user.notification.controller';
import pendingController from './pending/user.pending.controller';
import requestController from './request/user.request.controller';
import transactionController from './pending/user.pending.transaction.controller';
import transactionRequestController from './request/user.request.transaction.controller';

export default angular.module('app.user', [uirouter]).config(routing)
    .controller("UserController", userController)
    .controller("ChangePasswordController", changePasswordController)
    .controller("MyOffersController", myOffersController)
    .controller("WatchListController", watchListController)
    .controller("SettingsController", settingsController)
    .controller("SettingsEditController", settingsEditController)
    .controller("NotificationController", notificationController)
    .controller("PendingController", pendingController)
    .controller("RequestController", requestController)
    .controller("TransactionController", transactionController)
    .controller("TransactionRequestController", transactionRequestController)
    .service('UserService', userService)
    .service('TransactionService', transactionService)
    .name;
