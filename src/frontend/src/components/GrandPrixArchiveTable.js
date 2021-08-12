import './GrandPrixArchiveTable.scss'
import { React, useMemo, } from 'react';
import { useHistory } from 'react-router-dom'
import { useTable } from 'react-table'
import { COLUMNS } from './GrandPrixArchiveColumns'

export const GrandPrixArchiveTable = ({ grandsPrix }) => {

  const columns = useMemo(() => COLUMNS, []);
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
  }

  return (
    <div className='GrandPrixArchiveTable'>
      <table className="table"{...getTableProps()}>
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
        <tbody className="table-body"{...getTableBodyProps()} >
          {rows.map((row, ) => {
            prepareRow(row)
            return (
              <tr {...row.getRowProps()} className="table-row" onClick={() => handleRowClick(row)}  >
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
