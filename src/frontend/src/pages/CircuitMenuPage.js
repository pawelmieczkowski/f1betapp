import './CircuitMenuPage.scss'
import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom';
import { MenuTable } from '../components/MenuTable';


export const CircuitMenuPage = () => {
    const [circuits, SetCircuits] = useState([]);

    const columns = [
        {
            Header: 'Name',
            accessor: 'name',
            filter: 'fuzzyText',
            width: 400,
            Cell: ({ row }) => <Link to={'/circuits/' + row.original.id}>{row.original.name}</Link>
        },
        {
            Header: 'Location',
            accessor: 'location',
            filter: 'fuzzyText',
            width: 300,
            Cell: ({ row }) => <Link to={'/circuits/' + row.original.id}>{row.original.location}</Link>
        },
        {
            Header: 'Country',
            accessor: 'country',
            filter: 'fuzzyText',
            width: 300,
            Cell: ({ row }) => <Link to={'/circuits/' + row.original.id}>{row.original.country}</Link>
        },
    ];

    useEffect(() => {
        const fetchCircuits = async () => {
            const response = await fetch(`http://localhost:8080/circuits/all`);
            const data = await response.json();
            SetCircuits(data);
        }
        fetchCircuits();
    }, [])


    return (
        <section className="CircuitMenuPage">
            <h1 className='title'>
                FORMULA 1 CIRCUITS
            </h1>
            <MenuTable columns={columns} results={circuits} />
        </section>
    );
}