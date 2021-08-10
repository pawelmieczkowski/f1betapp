class TokenService {
    getLocalRefreshToken() {
        return JSON.parse(localStorage.getItem("refreshToken"));
    }

    getLocalAccessToken() {
        return JSON.parse(localStorage.getItem("token"));
    }

    updateLocalAccessToken(token) {
        localStorage.setItem("token", JSON.stringify(token));
    }

    getUser() {
        return JSON.parse(localStorage.getItem("user"));
    }

    setUser(user, token, refreshToken) {
        localStorage.setItem("user", JSON.stringify(user));
        localStorage.setItem("token", JSON.stringify(token));
        localStorage.setItem("refreshToken", JSON.stringify(refreshToken));
    }

    removeUser() {
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        localStorage.removeItem("refreshToken");
    }
}

export default new TokenService();