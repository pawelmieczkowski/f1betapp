import { FlagIcon } from "react-flag-kit";
import { getCountryCode } from './CountryCode'

export const COLUMNS = [
    {
        Header: '',
        accessor: 'circuit.country',
        Cell: ({ row }) => <FlagIcon code={getCountryCode(row.original.circuit.country)} size={20} />
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
        Header: 'Circuit',
        accessor: 'circuit.name'
    },
];