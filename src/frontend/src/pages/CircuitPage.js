import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { CircuitResults } from "../components/CircuitResults.js";
import { CircuitInfo } from "../components/CircuitInfo.js";
import "./CircuitPage.scss"

export const CircuitPage = () => {
    const { circuitId } = useParams();
    const [circuit, setCircuit] = useState([]);
    const [results, setResults] = useState([]);

    useEffect(() => {
        const fetchCircuit = async () => {
            const response = await fetch(`http://localhost:8080/circuits/${circuitId}`);
            const data = await response.json();
            console.log(data)
            setCircuit(data);
        };
        fetchCircuit();
    }, [circuitId])

    useEffect(() => {
        const fetchResults = async () => {
            const response = await fetch(`http://localhost:8080/grands-prix/circuit?id=${circuitId}`);
            const data = await response.json();
            data.sort((a, b) => a.date > b.date ? 1 : -1).reverse();
            setResults(data);
        };
        fetchResults();
    }, [circuitId]);

    return (
        <section className="CircuitPage">
            <CircuitInfo circuit={circuit} />
            <h1>F1 Grand Prix held on this circuit:</h1>
            <CircuitResults results={results} />
        </section>
    );
}