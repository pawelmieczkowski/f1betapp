import { FlagIcon } from "react-flag-kit";
import { getCountryCode } from './CountryCode'
import { Link } from 'react-router-dom';

export const COLUMNS = [
    {
        Header: '',
        accessor: 'round',
        Cell: ({ row }) => <Link to={'/race-result/' + row.original.id}>{row.original.round}</Link>,
        width: 10
    },
    {
        Header: '',
        accessor: 'circuit.country',
        Cell: ({ row }) =>
            <Link to={'/race-result/' + row.original.id} className="flag-column">
                <FlagIcon code={getCountryCode(row.original.circuit.country)} size={20} />
            </Link>,
        width: 10
    },
    {
        Header: 'Name',
        accessor: 'name',
        Cell: ({ row }) => <Link to={'/race-result/' + row.original.id}>{row.original.name}</Link>,
        width: 200
    },
    {
        Header: 'Date',
        accessor: 'date',
        Cell: ({ row }) => <Link to={'/race-result/' + row.original.id}>{row.original.date}</Link>,
        width: 75
    },
    {
        Header: 'Winner',
        accessor: 'driverName',
        Cell: ({ row }) => <Link to={'/race-result/' + row.original.id}>{row.original.driverName}</Link>,
        width: 125
    },
    {
        Header: 'Circuit',
        accessor: 'circuit.name',
        Cell: ({ row }) => <Link to={'/race-result/' + row.original.id}>{row.original.circuit.name}</Link>,
        width: 200
    },
];