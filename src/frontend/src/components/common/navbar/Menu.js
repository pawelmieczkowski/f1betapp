import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

const SECTION = styled.section`
display: flex;
justify-content: flex-start;
gap: 40px;
padding: 20px;
display: flex;
a {
    text-decoration: none;
}
.menu {
    gap: 40px;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    flex: 1;
    text-decoration: none;
}

.login {
    gap: 20px;
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-end;
}

@media (max-width: 768px) {
    .menu {
        justify-content: flex-start;
        flex-flow: column nowrap;
        flex: 0.5;
    }
    .login {
        gap: 40px;
        justify-content: center;
        flex-flow: column nowrap;
    }

    flex-direction: column;
    background-color: var(--color-purple);
    position: fixed;
    top: 0;
    right: 0;
    height: 100vh;
    width: 200px;
    padding-top: 3.5rem;
    transition: transform 0.3s ease-in-out;
    transform: ${({ open }) => open ? 'translateX(0)' : 'translateX(100%)'};
}
`

export const Menu = ({open}) => {
    return (
        <SECTION className="Menu" open={open}>
            <section className="menu">
                <Link to={'/'}>RESULTS</Link>
                <Link to={'/'}>DRIVERS</Link>
                <Link to={'/'}>TEAMS</Link>
                <Link to={'/'}>QUIZ</Link>
            </section>
            <section className="login">
                <Link to={'/register'}>SIGN UP</Link>
                <Link to={'/login'}>SIGN IN</Link>
            </section>
        </SECTION>
    )
}