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
      if (response.data.accessToken) {
          localStorage.setItem("user", JSON.stringify(response.data));
      }
      return response.data;
  };
  
  const logout = () => {
    localStorage.removeItem("user");
  };
  
  const getCurrentUser = () => {
    return JSON.parse(localStorage.getItem("user"));
  };
  
  export default {
    register,
    login,
    logout,
    getCurrentUser
  };