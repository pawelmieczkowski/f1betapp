import './Header.scss'
import React from "react";
import {Navbar} from '../../common'


function Header() {
  return (
    <section className="header">
      <section className="page">
        <section className="header-logo">
          <a href='/' >
            <img src="/logo_f1_100x100.png" alt="LOGO" />
          </a>
        </section>
        <section className="navbar">
          <Navbar />
        </section>
      </section>
    </section>
  );
}

export default Header;
