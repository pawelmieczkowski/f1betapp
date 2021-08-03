import { Link } from 'react-router-dom';
export const COLUMNS = [
    {
        Header: 'Position',
        accessor: 'result'
    },
    {
        Header: 'Number',
        accessor: 'driverNumber'
    },
    {
        Header: 'Driver',
        id: "driver",
        accessor: 'driverName',
        Cell: ({ row }) => <Link to={'/drivers/' + row.original.driverId}>{row.original.driverName}</Link>
    },
    {
        Header: 'Constructor',
        accessor: 'teamName',
        Cell: ({ row }) => <Link to={'/teams/' + row.original.teamName}>{row.original.teamName}</Link>
    },
    {
        Header: 'Q1',
        accessor: 'q1time'
    },
    {
        Header: 'Q2',
        accessor: 'q2time'
    },
    {
        Header: 'Q3',
        accessor: 'q3time'
    }
];