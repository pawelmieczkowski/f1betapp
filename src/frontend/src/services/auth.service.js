import axios from "axios";

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
    console.log(response)
  if (response.data) {
    localStorage.setItem("token", JSON.stringify(response.headers.authorization))
    localStorage.setItem("user", JSON.stringify(response.data));
  }
  return response.data;
};

const logout = () => {
  localStorage.removeItem("user");
  localStorage.removeItem("token");
};

const getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};

const getToken = () => {
  return JSON.parse(localStorage.getItem("token"));
};

const AuthService = {
  register,
  login,
  logout,
  getCurrentUser,
  getToken
};

export default AuthService