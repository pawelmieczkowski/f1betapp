import './CircuitMenuPage.scss'
import React from 'react'
import { Link } from 'react-router-dom';
import { MenuTable } from '../components/MenuTable';
import { useQuery } from '../services/errorHandling/useQuery'
import { RingSpinner } from '../components/common/spinner/RingSpinner';


export const CircuitMenuPage = () => {

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

    const circuits = useQuery({
        url: `http://localhost:8080/circuits/all`
    }).data;

    return (
        <section className="CircuitMenuPage">
            {
                circuits.length > 0 ? (
                    <div>
                        <h1 className='title'>
                            FORMULA 1 CIRCUITS
                        </h1>
                        <MenuTable columns={columns} results={circuits} />
                    </div>
                ) : (
                    <RingSpinner />
                )
            }
        </section>
    );
}