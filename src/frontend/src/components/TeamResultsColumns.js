import { Link } from 'react-router-dom';

export const COLUMNS = [
    {
        Header: 'Date',
        Cell: ({ row }) => {
            return (
                <div>
                    {row.original.grandPrix.date}
                </div>

            )
        }
    },
    {
        Header: 'Grand Prix',
        accessor: 'grandPrix.name',
        Cell: ({ row }) => <Link className='grand-prix-column link' to={`/race-result/${row.original.grandPrix.id}`}>{row.original.grandPrix.name}</Link>
    },
    {
        Header: 'Driver',
        accessor: 'driverName',
        Cell: ({ row }) => {
            return (

                <div>
                    <Link className="link" to={'/drivers/' + row.original.driverId}>{row.original.driverName}</Link>
                </div>
            )
        }
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