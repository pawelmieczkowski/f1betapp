import React from "react";
import AuthService from "../services/auth.service";

export const ProfilePage = () => {
  const currentUser = AuthService.getCurrentUser();
  const token = AuthService.getToken();

  return (
    <div className="container">
      <header className="jumbotron">
      </header>
      <p>
        {currentUser.username}
        {currentUser.email}
        {currentUser.appUserRole}
        <strong>Token:</strong> {token.substring(0, 20)} ...{" "}
        {token.substr(token.length - 20)}
      </p>
    </div>
  );
};