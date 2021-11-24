export default class LoginService {
    constructor($http) {
        this.$http = $http;
        this.loggedIn = this.isLoggedIn();
    }

    login(user) {
        return this.$http.post("http://localhost:8080/login", "username=" + user.email + "&password=" + user.password, {
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            withCredentials: true
        }).then((response) => {return response.data});
    }
    
    logout() {
        return this.$http.post("http://localhost:8080/logout").then((response) => {return response.data});
    }
    
    isLoggedIn() {
        return localStorage.getItem("session") !== null;
    }

    setLoggedIn() {
        this.loggedIn = this.isLoggedIn();
    }
    
    resetPassword(email) {
        return this.$http.post("http://localhost:8080/user/resetPassword", "email=" + email, {
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).then((response) => {return response.data});
    }
}