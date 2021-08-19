import React, { useMemo } from 'react';
import { useTable, usePagination } from 'react-table'
import { useHistory } from 'react-router-dom'
import { COLUMNS } from './CircuitColumns'
import './CircuitResults.scss'

export const CircuitResults = ({ results }) => {
    const columns = useMemo(() => COLUMNS, []);
    const history = useHistory();

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        prepareRow,
        page,
        canPreviousPage,
        canNextPage,
        pageOptions,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: { pageIndex, pageSize },
    } = useTable({
        columns: columns,
        data: results,
        initialState: { pageIndex: 0, pageSize: 20 },
    },
        usePagination
    )

    const handleRowClick = (row) => {
        history.push(`/race-result/${row.original.id}`)
      }

    return (
        <section className="CircuitResults">
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
                <tbody {...getTableBodyProps()}>
                    {page.map((row,) => {
                        prepareRow(row)
                        return (
                            <tr {...row.getRowProps()} className="table-row" onClick={() => handleRowClick(row)}> 
                                {row.cells.map(cell => {
                                    return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                })}
                            </tr>
                        )
                    })}
                </tbody>
            </table>
            <div className="pagination">
                <button onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
                    {'<<'}
                </button>{' '}
                <button onClick={() => previousPage()} disabled={!canPreviousPage}>
                    {'<'}
                </button>{' '}
                <button onClick={() => nextPage()} disabled={!canNextPage}>
                    {'>'}
                </button>{' '}
                <button onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
                    {'>>'}
                </button>{' '}
                <span>
                    Page{' '}
                    <strong>
                        {pageIndex + 1} of {pageOptions.length}
                    </strong>{' '}
                </span>
                <span>
                    | Go to page:{' '}
                    <input
                        type="number"
                        defaultValue={pageIndex + 1}
                        onChange={e => {
                            const page = e.target.value ? Number(e.target.value) - 1 : 0
                            gotoPage(page)
                        }}
                        style={{ width: '100px' }}
                    />
                </span>{' '}
                <select
                    value={pageSize}
                    onChange={e => {
                        setPageSize(Number(e.target.value))
                    }}
                >
                    {[10, 20, 30, 40, 50].map(pageSize => (
                        <option key={pageSize} value={pageSize}>
                            Show {pageSize}
                        </option>
                    ))}
                </select>
            </div>
        </section>
    );
}