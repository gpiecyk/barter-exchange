export default class NotificationController {
    constructor($mdDialog, $mdToast, $state) {
        this.$mdDialog = $mdDialog;
        this.$mdToast = $mdToast;
        this.$state = $state;
        this.playNotificationSound();
    }

    playNotificationSound() {
        let audio = new Audio('./audio/facebook_notification.mp3');
        audio.play();
    }

    closeToast() {
        this.$mdToast.hide();
    }

    openMoreInfo() {
        this.$state.go('user.request');
        this.closeToast();
    }
}