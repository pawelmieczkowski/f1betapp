import { Link } from 'react-router-dom';
export const COLUMNS = [
    {
        Header: 'Position',
        accessor: 'finishingPosition'
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
        Header: 'Points',
        accessor: 'points'
    },
    {
        Header: 'Laps',
        accessor: 'laps'
    },
    {
        Header: 'Time',
        accessor: 'time'
    },
    {
        Header: 'Fastest Lap',
        accessor: 'fastestLapTime'
    }
];