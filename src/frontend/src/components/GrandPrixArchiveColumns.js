import { FlagIcon } from "react-flag-kit";
import { getCountryCode } from './CountryCode'

export const COLUMNS = [
    {
        Header: '',
        accessor: 'round'
    },
    {
        Header: '',
        accessor: 'circuit.country',
        Cell: ({ row }) => <div className="flag-column"><FlagIcon code={getCountryCode(row.original.circuit.country)} size={20} /></div>
    },
    {
        Header: 'Name',
        accessor: 'name'
    },
    {
        Header: 'Date',
        accessor: 'date'
    },
    {
        Header: 'Winner',
        accessor: 'driverName'
    },
    {
        Header: 'Circuit',
        accessor: 'circuit.name'
    },
];