import { React, useState, useEffect, useMemo } from 'react';
import { useTable } from 'react-table'
import { COLUMNS } from './DriverResultsColumns'
import { GrandPrixYearSelector } from './GrandPrixYearSelector';
import './DriverResults.scss'

export const DriverResults = ({ results }) => {
    const [yearSelected, setYearSelected] = useState(2021);
    const [years, setYears] = useState([]);
    const columns = useMemo(() => COLUMNS, []);
    const [resultsSelected, setResultsSelected] = useState([]);

    useEffect(
        () => {
            let years = new Set();
            results.forEach(i => years.add(i.grandPrix.year));
            setYears(Array.from(years).sort().reverse());
        }, [results]
    );

    useEffect(
        () => {
            const filtered = results.filter(value => value.grandPrix.year === yearSelected);
            filtered.sort().reverse();
            setResultsSelected(filtered);
        }, [yearSelected, results]
    );

    const handleCallback = (childData) => {
        setYearSelected(parseInt(childData))
    }

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow
    } = useTable({
        columns: columns,
        data: resultsSelected
    })


    return (
        <section className="DriverResult">
            <section className="year-selector">
                <GrandPrixYearSelector parentCallback={handleCallback} years={years} yearSelected={yearSelected} />
            </section>
            <section className="scrollable">
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
                                <tr {...row.getRowProps()} className="table-row">
                                    {row.cells.map(cell => {
                                        return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                    })}
                                </tr>
                            )
                        })}
                    </tbody>
                </table>
            </section>
        </section>
    );
}
