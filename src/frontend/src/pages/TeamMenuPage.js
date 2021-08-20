import './TeamMenuPage.scss'
import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom';
import { MenuTable } from '../components/MenuTable';


export const TeamMenuPage = () => {
    const [teams, setTeams] = useState([]);

    const columns = [
        {
            Header: 'Name',
            accessor: 'name',
            filter: 'fuzzyText',
            width: 500,
            Cell: ({ row }) => <Link to={'/teams/' + row.original.name}>{row.original.name}</Link>
        },
        {
            Header: 'Nationality',
            accessor: 'nationality',
            filter: 'fuzzyText',
            width: 500,
            Cell: ({ row }) => <Link to={'/teams/' + row.original.name}>{row.original.nationality}</Link>
        },
    ];

    useEffect(() => {
        const fetchTeams = async () => {
            const response = await fetch(`http://localhost:8080/teams/all`);
            const data = await response.json();
            setTeams(data);
        }
        fetchTeams();
    }, [])


    return (
        <section className="TeamMenuPage">
            <h1 className='title'>
                FORMULA 1 TEAMS
            </h1>
            <MenuTable columns={columns} results={teams} />
        </section>
    );
}