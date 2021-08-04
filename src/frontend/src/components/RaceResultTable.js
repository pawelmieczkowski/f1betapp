import "./RaceResultTable.scss"
import { React, useMemo } from 'react';
import { useTable } from 'react-table'
import { COLUMNS } from './RaceResultColumns'

export const RaceResultTable = ({ raceResults }) => {

    function compare(c, d) {
        const a = c.finishingPosition;
        const b = d.finishingPosition;
        // check for numberhood
        const numA = !isNaN(a);
        const numB = !isNaN(b);
      
        if (numA && numB) {
          return Number(a) - Number(b);
        }
      
        if (numA) return -1;
        if (numB) return 1;
      
        // check for wordhood
        const wordA = /^[a-zA-Z]+$/.test(a);
        const wordB = /^[a-zA-Z]+$/.test(b);
      
        if (wordA && wordB) {
          return a.localeCompare(b);
        }
      
        if (wordA) return -1;
        if (wordB) return 1;
      
        return 1; // or whatever logic to sort within non-alphanumeric values
      }

    const columns = useMemo(() => COLUMNS, []);
    const sorted = raceResults.sort(compare)

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow
    } = useTable({
        columns: columns,
        data: sorted,
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