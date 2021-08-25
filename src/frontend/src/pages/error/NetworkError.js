import './NetworkError.scss'
import React from "react";
import { Link } from "react-router-dom";

export const NetworkError = () => (
  <div className='NetworkError'>
    <div className='message'>
      <h1>Something went wrong</h1>
      <h2>Server not responding</h2>
      <Link to="/">Back to Homepage</Link>
    </div>
  </div>
);
