import './GrandPrixArchiveTable.scss'
import { React, useEffect, useMemo, useState } from 'react';
import { useHistory } from 'react-router-dom'
import { useTable } from 'react-table'
import { COLUMNS } from './GrandPrixArchiveColumns'

export const GrandPrixArchiveTable = () => {

  const columns = useMemo(() => COLUMNS, []);
  const [grandsPrix, setGrandsPrix] = useState([]);
  const history = useHistory();
  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow
  } = useTable({
    columns: columns,
    data: grandsPrix,
  });
  const handleRowClick = (row) => {
    history.push(`/race-result/${row.original.id}`)
    console.log(row);
  }

  useEffect(
    () => {
      const fetchMatches = async () => {
        const response = await fetch('http://localhost:8080/grands-prix?year=2000');
        const data = await response.json();
        setGrandsPrix(data);
      };
      fetchMatches();
    }, []
  );

  return (
    <div>
      <table {...getTableProps()}>
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
            <th></th>
          </tr>
        </thead>
        <tbody {...getTableBodyProps()} >
          {rows.map((row, i) => {
            prepareRow(row)
            return (
              <tr {...row.getRowProps()} className="GrandPrixArchiveTable" onClick={() => handleRowClick(row)}  >
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
