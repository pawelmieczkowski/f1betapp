import "./RaceResultTable.scss"
import { React, useMemo } from 'react';
import { useTable } from 'react-table'
import { COLUMNS } from './RaceResultColumns'

export const RaceResultTable = ({ raceResults }) => {

    const columns = useMemo(() => COLUMNS, []);
    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow
    } = useTable({
        columns: columns,
        data: raceResults
    })
    return (
        <div className="RaceResultTable">
            <table className="table" {...getTableProps()}>
                <thead>{
                    headerGroups.map(headerGroup => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map(column => (
                                <th {...column.getHeaderProps()}>{column.render('Header')}</th>
                            ))}
                        </tr>
                    ))
                }
                    <tr>
                    </tr>
                </thead>
                <tbody className="table-body" {...getTableBodyProps()}>
                    {rows.map((row) => {
                        prepareRow(row)
                        return (
                            <tr {...row.getRowProps()}>
                                {row.cells.map(cell => {
                                    return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                })}
                            </tr>
                        )
                    })}
                </tbody>
            </table>
        </div>
    );
}