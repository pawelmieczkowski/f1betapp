import './Header.css'
import React from "react";
import { Navbar } from '../../common'


function Header() {
  return (
    <section className="header">
      <section className="header-logo">
        <a href='/' >LOGO</a>
      </section>
      <section className="header-navbar">
        <Navbar />
      </section>
    </section>
  );
}

export default Header;
