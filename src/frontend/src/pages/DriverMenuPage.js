import './DriverMenuPage.scss'
import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom';
import { MenuTable } from '../components/MenuTable';


export const DriverMenuPage = () => {
    const [drivers, setDrivers] = useState([]);

    const columns = [
        {
            Header: 'Forename',
            accessor: 'forename',
            filter: 'fuzzyText',
            width: 300,
            Cell: ({ row }) => <Link to={'/drivers/' + row.original.id}>{row.original.forename}</Link>
        },
        {
            Header: 'Surname',
            accessor: 'surname',
            filter: 'fuzzyText',
            width: 300,
            Cell: ({ row }) => <Link to={'/drivers/' + row.original.id}>{row.original.surname}</Link>
        },
        {
            Header: 'Nationality',
            accessor: 'nationality',
            filter: 'fuzzyText',
            width: 300,
            Cell: ({ row }) => <Link to={'/drivers/' + row.original.id}>{row.original.nationality}</Link>
        },
    ];

    useEffect(() => {
        const fetchDrivers = async () => {
            const response = await fetch(`http://localhost:8080/drivers/all`);
            const data = await response.json();
            setDrivers(data);
        }
        fetchDrivers();
    }, [])


    return (
        <section className="DriverMenuPage">
            <h1 className='title'>
                FORMULA 1 DRIVERS
            </h1>
            <MenuTable columns={columns} results={drivers} />
        </section>
    );
}