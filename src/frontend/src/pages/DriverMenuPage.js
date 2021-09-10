import './DriverMenuPage.scss'
import React from 'react'
import {Link} from 'react-router-dom';
import {MenuTable} from '../components/MenuTable';
import {useQuery} from '../services/errorHandling/useQuery'

import {RingSpinner} from '../components/common/spinner/RingSpinner'

export const DriverMenuPage = () => {
    const drivers = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/drivers/all`
    }).data;

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


    return (
        <section className="DriverMenuPage">
            {
                drivers.length > 0 ? (
                    <div>
                        <h1 className='title'>
                            FORMULA 1 DRIVERS
                        </h1>
                        <MenuTable columns={columns} results={drivers} />
                    </div>
                ) : (
                    <RingSpinner />
                )
            }
        </section>
    );
}