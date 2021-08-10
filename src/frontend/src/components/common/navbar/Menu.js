import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import AuthService from "../../../services/auth.service";

const SECTION = styled.section`
  display: flex;
  padding: 12px 0;
  .menu {
    display: flex;
    flex-flow: row nowrap;
    align-items: flex-end;
    justify-content: center;
    flex: 1 1000px;
    a {
      text-decoration: none;
      flex: 1;
      color: var(--color-light-grey);
      font-size: 20px;
      transition: all 0.25s ease;
      &:hover {
        color: var(--color-white);
      }
      &:active {
        letter-spacing: 1px;
      }
    }
    .a-active {
      color: var(--color-white);
    }

    .border {
      border-right: 1px solid;
      border-color: var(--color-grey);
    }
  }

  .right-nav {
    flex: 1 calc(100% - 1000px);
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-end;
  }

  .login {
    flex: 1 calc(100% - 1000px);
    padding-right: 10px;
    gap: 20px;
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-end;
    a {
      text-decoration: none;
      padding: 8px 20px;
      text-align: center;
      width: 5rem;
      border-radius: 20px;
      background: var(--color-white);
      border: 1px solid var(--color-secondary);
      color: var(--color-secondary);
      letter-spacing: 0.5px;
      font-size: 14px;
      font-weight: bold;
      cursor: pointer;
      transition: all 0.25s ease;
      &:hover {
        color: var(--color-white);
        background: var(--color-secondary);
      }
      &:active {
        transform: scale(0.95);
      }
    }
    .register-button {
      color: var(--color-white);
      background: var(--color-secondary);
      &:hover {
        color: var(--color-white);
        background: var(--color-secondary);
      }
    }
  }
  .logged {
    flex: 1 calc(100% - 1000px);
    justify-content: flex-end;
  }

  @media (max-width: 768px) {
    .menu {
      align-items: center;
      justify-content: flex-start;
      flex-flow: column nowrap;
      flex: 0.5;
      .border {
        border-right: 0px solid;
      }
    }
    .login {
      gap: 40px;
      align-items: center;
      justify-content: center;
      flex-flow: column nowrap;
    }

    flex-direction: column;
    background-color: var(--color-purple);
    position: fixed;
    top: 0;
    right: 0;
    height: 100vh;
    width: 250px;
    padding-top: 3.5rem;
    transition: transform 0.3s ease-in-out;
    transform: ${({ open }) => (open ? "translateX(0)" : "translateX(100%)")};
  }
`;

export const Menu = ({ open, parentCallback }) => {
  const [logged, setLogged] = useState("");

  const logout = () => {
    AuthService.logout()
    setLogged(false);
  }

  useEffect(() => {
    setLogged(!!localStorage.getItem("user"));
  }, []);


  return (
    <SECTION className="Menu" open={open}>
      <section className="menu">
        <Link className="border" to={"/"} onClick={() => parentCallback(false)}>
          RESULTS
        </Link>
        <Link className="border" to={"/"} onClick={() => parentCallback(false)}>
          DRIVERS
        </Link>
        <Link className="border" to={"/"} onClick={() => parentCallback(false)}>
          TEAMS
        </Link>
        <Link to={"/"} onClick={() => parentCallback(false)}>
          QUIZ
        </Link>
      </section>
      <section className="right-nav">
        {!logged ? (
          <section className="login">
            <Link
              className="register-button"
              to={"/register"}
              onClick={() => parentCallback(false)}
            >
              REGISTER
            </Link>
            <Link
              className="login-button"
              to={"/login"}
              onClick={() => parentCallback(false)}
            >
              SIGN IN
            </Link>
          </section>
        ) : (
          <section className="logged">
            <button onClick={() => logout()}>LOGOUT</button>
          </section>
        )}
      </section>
    </SECTION>
  );
};
