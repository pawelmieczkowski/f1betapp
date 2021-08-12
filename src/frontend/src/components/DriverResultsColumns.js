import { Link } from 'react-router-dom';

export const COLUMNS = [
    {
        Header: 'Date',
        accessor: 'grandPrix.date'
    },
    {
        Header: 'Grand Prix',
        accessor: 'grandPrix.name',
        Cell: ({ row }) => <Link className='grand-prix-column link' to={`/race-result/${row.original.grandPrix.id}`}>{row.original.grandPrix.name}</Link>
    },
    {
        Header: 'Constructor',
        accessor: 'teamName',
        Cell: ({ row }) => <Link className="link" to={'/teams/' + row.original.teamName}>{row.original.teamName}</Link>
    },
    {
        Header: 'Points',
        accessor: 'points'
    },
    {
        Header: 'Starting Position',
        accessor: 'startingGridPosition'
    },
    {
        Header: 'Finished',
        accessor: 'finishingPosition'
    }
];