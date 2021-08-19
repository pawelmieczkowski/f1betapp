import './CircuitMenuPage.scss'
import React, { useState, useEffect, useMemo } from 'react'
import { Link } from 'react-router-dom';
import {
    useTable,
    usePagination,
    useFilters
} from 'react-table'
import { matchSorter } from 'match-sorter'

export const COLUMNS = [
    {
        Header: 'Name',
        accessor: 'name',
        filter: 'fuzzyText',
        width: 400,
        Cell: ({ row }) => <Link to={'/circuits/' + row.original.id}>{row.original.name}</Link>
    },
    {
        Header: 'Location',
        accessor: 'location',
        filter: 'fuzzyText',
        width: 300,
        Cell: ({ row }) => <Link to={'/circuits/' + row.original.id}>{row.original.location}</Link>
    },
    {
        Header: 'Country',
        accessor: 'country',
        filter: 'fuzzyText',
        width: 300,
        Cell: ({ row }) => <Link to={'/circuits/' + row.original.id}>{row.original.country}</Link>
    },
];

// Define a default UI for filtering
function DefaultColumnFilter({
    column: { filterValue, preFilteredRows, setFilter },
}) {
    const count = preFilteredRows.length

    return (
        <input
            value={filterValue || ''}
            onChange={e => {
                setFilter(e.target.value || undefined)
            }}
            placeholder={`Search ${count} records...`}
        />
    )
}

function fuzzyTextFilterFn(rows, id, filterValue) {
    return matchSorter(rows, filterValue, { keys: [row => row.values[id]] })
}

// Let the table remove the filter if the string is empty
fuzzyTextFilterFn.autoRemove = val => !val

export const CircuitMenuPage = () => {
    const columns = useMemo(() => COLUMNS, []);
    const [circuits, SetCircuits] = useState([]);

    const filterTypes = React.useMemo(
        () => ({
            fuzzyText: fuzzyTextFilterFn,
        }),
        []
    )
    const defaultColumn = React.useMemo(
        () => ({
            //default Filter UI
            Filter: DefaultColumnFilter,
        }),
        []
    )

    useEffect(() => {
        const fetchCircuits = async () => {
            const response = await fetch(`http://localhost:8080/circuits/all`);
            const data = await response.json();
            SetCircuits(data);
        }
        fetchCircuits();
    }, [])

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
    } = useTable(
        {
            columns: columns,
            data: circuits,
            defaultColumn,
            filterTypes,
            initialState: { pageIndex: 0, pageSize: 20 }
        },
        useFilters,
        usePagination
    )

    return (
        <section className="CircuitMenuPage">
            <div className="title">
                FORMULA 1 CIRCUITS
            </div>
            <div className='scrollable'>
                <table className="table" {...getTableProps()}>
                    <thead>{
                        headerGroups.map(headerGroup => (
                            <tr {...headerGroup.getHeaderGroupProps()}>
                                {headerGroup.headers.map(column => (
                                    <th {...column.getHeaderProps({
                                        style: {
                                            width: column.width
                                        }
                                    })} >{column.render('Header')}
                                        <div>{column.canFilter ? column.render('Filter') : null}</div>
                                    </th>
                                ))}
                            </tr>
                        ))
                    }
                        <tr>
                        </tr>
                    </thead>
                    <tbody className="table-body" {...getTableBodyProps()}>
                        {page.map((row) => {
                            prepareRow(row)
                            return (
                                <tr {...row.getRowProps()} className="table-row">
                                    {row.cells.map(cell => {
                                        return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                    })}
                                </tr>
                            )
                        })}
                    </tbody>
                </table>
                </div>
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