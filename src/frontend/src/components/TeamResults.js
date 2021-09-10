import './DriverResults.scss'
import React, {useEffect, useMemo, useState} from 'react';
import {useTable} from 'react-table'
import {COLUMNS} from './TeamResultsColumns'
import {GrandPrixYearSelector} from './GrandPrixYearSelector';
import {useQuery} from '../services/errorHandling/useQuery'

export const TeamResults = ({ teamName }) => {
    const [yearSelected, setYearSelected] = useState([]);
    const [isYear, setIsYear] = useState(false);
    const [years, setYears] = useState([]);
    const columns = useMemo(() => COLUMNS, []);

    const fetchedYears = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/race-result/years?teamName=${teamName}`
    }).data;

    useEffect(
        () => {
            if (fetchedYears.length > 0) {
                fetchedYears.sort().reverse();
                setYears(fetchedYears);
                const maxYear = Math.max.apply(Math, fetchedYears)
                setYearSelected(maxYear);
                setIsYear(true);
            }
        }, [fetchedYears]
    );

    const resultsSelected = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/race-result/team?name=${teamName}&year=${yearSelected}`,
        isYear: isYear
    }).data;

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
