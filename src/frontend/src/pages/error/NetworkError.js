import React from "react";
import { Link } from "react-router-dom";

export const NetworkError = () => (
  <div>
    <h1>Something went wrong</h1>
    <Link to="/">Back to homepage</Link>
  </div>
);
