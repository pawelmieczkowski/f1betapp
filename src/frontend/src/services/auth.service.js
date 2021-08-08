import axios from "axios";

const API_REGISTRATION_URL = "http://localhost:8080/registration"

const register = (username, email, password) => {
    return axios.post(API_REGISTRATION_URL, {
      username,
      email,
      password,
    });
  };


  
  export default {
    register,

  };