import React, { useState } from 'react'
import { Menu } from './Menu'
import styled from 'styled-components';

const BURGER = styled.div`

width: 2rem;
  height: 2rem;
  position: absolute;
  top: 15px;
  right: 20px;
  z-index: 20;
  display: none;
    
  @media (max-width: 900px) {
    display: flex;
    justify-content: space-around;
    flex-flow: column nowrap;
  }

    div{
        width: 2rem;
    height: 0.25rem;
    background-color: ${({ open }) => open ? '#ccc' : '#333'};
    border-radius: 10px;
    transform-origin: 1px;
    transition: all 0.3s linear;
    }
    

    Menu {
        flex: 1;
    }
`

const Navbar = () => {
    const [open, setOpen] = useState(false);

    const handleCallback = (childData) => {
        setOpen(childData)
    }

    return (
        <div>
            <BURGER open={open} onClick={() => setOpen(!open)}>
                <div />
                <div />
                <div />
            </BURGER>
            <Menu open={open} parentCallback={handleCallback} />
        </div>
    )
}
export default Navbar;