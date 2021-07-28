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
        Cell: ({ row }) => <a href={'http://localhost:8080/drivers/' + row.original.driverId}>{row.original.driverName}</a>
    },
    {
        Header: 'Constructor',
        accessor: 'teamName',
        Cell: ({ row }) => <a href={'http://localhost:8080/teams?name=' + row.original.teamName}>{row.original.teamName}</a>
    },
    {
        Header: 'Points',
        accessor: 'points'
    },
    {
        Header: 'laps',
        accessor: 'laps'
    },
    {
        Header: 'time',
        accessor: 'time'
    },
    {
        Header: 'Fastest Lap',
        accessor: 'fastestLapTime'
    }
];