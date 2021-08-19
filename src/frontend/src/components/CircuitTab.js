import React, { useState, useEffect } from 'react'
import { CircuitInfo } from './CircuitInfo';
import { CircuitResults } from './CircuitResults';
import './CircuitTab.scss'

export const CircuitTab = ({circuit}) => {
    const [results, setResults] = useState([]);

    useEffect(() => {
        const fetchResults = async () => {
            const response = await fetch(`http://localhost:8080/grands-prix/circuit?id=${circuit.id}`);
            const data = await response.json();
            data.sort((a, b) => a.date > b.date ? 1 : -1).reverse();
            setResults(data);
        };
        fetchResults();
    }, [circuit]);

    return (
        <section className="CircuitTab">
            <CircuitInfo circuit={circuit} />
            <CircuitResults results={results} />
        </section>
    );
}