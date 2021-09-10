import './TeamMenuPage.scss'
import React from 'react'
import {Link} from 'react-router-dom';
import {MenuTable} from '../components/MenuTable';
import {useQuery} from '../services/errorHandling/useQuery'
import {RingSpinner} from '../components/common/spinner/RingSpinner';

export const TeamMenuPage = () => {
    const teams = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/teams/all`
    }).data;

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

    return (
        <section className="TeamMenuPage">
            {
                teams.length > 0 ? (
                    <div>
                        <h1 className='title'>
                            FORMULA 1 TEAMS
                        </h1>
                        <MenuTable columns={columns} results={teams} />
                    </div>
                ) : (
                    <RingSpinner />
                )
            }
        </section>
    );
}