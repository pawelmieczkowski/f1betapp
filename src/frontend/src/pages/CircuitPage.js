import "./CircuitPage.scss"
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { CircuitResults } from "../components/CircuitResults.js";
import { CircuitInfo } from "../components/CircuitInfo.js";
import { useQuery } from '../services/errorHandling/useQuery'
import { RingSpinner } from '../components/common/spinner/RingSpinner';

export const CircuitPage = () => {
    const { circuitId } = useParams();
    const [results, setResults] = useState([]);

    const circuit = useQuery({
        url: `http://localhost:8080/circuits/${circuitId}`
    }).data;

    const fetchedResults = useQuery({
        url: `http://localhost:8080/grands-prix/circuit?id=${circuitId}`
    }).data;

    useEffect(() => {
        fetchedResults.sort((a, b) => a.date > b.date ? 1 : -1).reverse();
        setResults(fetchedResults);
    }, [fetchedResults]);

    return (
        <section className="CircuitPage">
            {
                results.length > 0 ? (
                    <div>
                        <CircuitInfo circuit={circuit} />
                        <CircuitResults results={results} />
                    </div>
                ) : (
                    <RingSpinner />
                )
            }
        </section>
    );
}