import axios from "axios";
import TokenService from "./token.service";

const API_REGISTRATION_URL = "http://localhost:8080/registration"
const API_LOGIN_URL = "http://localhost:8080/login"

const register = (username, email, password) => {
  return axios.post(API_REGISTRATION_URL, {
    username,
    email,
    password,
  });
};

const login = async (username, password) => {
  const response = await axios
    .post(API_LOGIN_URL, {
      username,
      password,
    });
  if (response.data) {
    TokenService.setUser(response.data, response.headers.authorization, response.headers.refresh);
  }
  return response.data;
};

const logout = () => {
  TokenService.removeUser();
};

const getCurrentUser = () => {
  return TokenService.getUser();
};

const getToken = () => {
  return TokenService.getLocalAccessToken();
};

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
  getToken
};

export default AuthService