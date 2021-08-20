import React, { useState, useEffect, useMemo } from 'react';
import { useTable } from 'react-table'
import { COLUMNS } from './TeamResultsColumns'
import { GrandPrixYearSelector } from './GrandPrixYearSelector';
import './DriverResults.scss'

export const TeamResults = ({ teamName }) => {
    const [yearSelected, setYearSelected] = useState(2021);
    const [years, setYears] = useState([]);
    const columns = useMemo(() => COLUMNS, []);
    const [resultsSelected, setResultsSelected] = useState([]);

    useEffect(
        () => {
            const fetchYears = async () => {
                const response = await fetch(`http://localhost:8080/race-result/years?teamName=${teamName}`);
                const data = await response.json();
                data.sort().reverse();
                setYears(data);
                const maxYear = Math.max.apply(Math, data)
                setYearSelected(maxYear);
            };
            fetchYears();
        }, [teamName]
    );

    useEffect(() => {
        const fetchResults = async () => {
            const response = await fetch(`http://localhost:8080/race-result/team?name=${teamName}&year=${yearSelected}`);
            const data = await response.json();
            setResultsSelected(data);
        };
        fetchResults();
    }, [teamName, yearSelected]);

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
