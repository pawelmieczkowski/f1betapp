import { React, useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { DriverInfo } from "../components/DriverInfo.js";
import { DriverResults } from "../components/DriverResults.js";

export const DriverPage = () => {
    const [driver, setDriver] = useState([]);
    const [image, setImage] = useState([]);
    const [highestPosition, setHighestPosition] = useState([]);
    const [highestPositionResults, setHighestPositionResults] = useState([]);
    const [results, setResults] = useState([{ grandPrix: [{ year: [] }] }]);
    const { driverId } = useParams();

    useEffect(() => {
        const fetchDriver = async () => {
            const response = await fetch(`http://localhost:8080/drivers/${driverId}`);
            const data = await response.json();
            setDriver(data);

            const article = data.url.split("wiki/")[1];
            const imageResponse = await fetch(
                `https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&origin=*&format=json&titles=${article}&pithumbsize=500`
            );
            const imageData = await imageResponse.json();
            try {
                const key = Object.keys(imageData.query.pages)[0];
                const imageUrl = imageData.query.pages[key].thumbnail.source;
                setImage(imageUrl);
            } catch (error) {
                setImage("/Placeholder.svg");
            }
        };
        fetchDriver();
    }, [driverId, setImage]);

    useEffect(() => {
        const fetchResults = async () => {
            const response = await fetch(`http://localhost:8080/race-result/driver?id=${driverId}`);
            const data = await response.json();
            let highest = 100;
            let highestPositions = [];
            data.forEach(i => {
                if (parseInt(i.finishingPosition) < highest) {
                    highest = i.finishingPosition
                    highestPositions = [];
                    highestPositions.push(i)
                } else if (i.finishingPosition === highest) {
                    highestPositions.push(i)
                }
            });
            highestPositions.sort((a, b) => a.grandPrix.date > b.grandPrix.date ? 1 : -1).reverse();
            setHighestPosition(highest);
            setHighestPositionResults(highestPositions);
            setResults(data);
        };
        fetchResults();
    }, [driverId]);

    return (
        <section>
            <DriverInfo driver={driver} image={image} highestPositionResults={highestPositionResults} highestPosition={highestPosition} />
            <DriverResults results={results} />
        </section>
    );
};
