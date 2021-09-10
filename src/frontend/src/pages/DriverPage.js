import {React, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {RingSpinner} from "../components/common/spinner/RingSpinner.js";
import {DriverInfo} from "../components/DriverInfo.js";
import {DriverResults} from "../components/DriverResults.js";
import {useQuery} from '../services/errorHandling/useQuery'

export const DriverPage = () => {
    const [image, setImage] = useState([]);
    const [highestPosition, setHighestPosition] = useState([]);
    const [highestPositionResults, setHighestPositionResults] = useState([]);
    const [results, setResults] = useState([{ grandPrix: [{ year: [] }] }]);
    const { driverId } = useParams();

    const driver = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/drivers/${driverId}`,
    }).data;


    const fetchedResults = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/race-result/driver?id=${driverId}`,
    }).data;

    useEffect(() => {
        const fetchDriver = async () => {
            if (driver.url) {
                const article = driver.url.split("wiki/")[1];
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
            }
        };
        fetchDriver();
    }, [driver, setImage]);

    useEffect(() => {
        let highest = 100;
        let highestPositions = [];
        fetchedResults.forEach(i => {
            if (parseInt(i.finishingPosition) < highest) {
                highest = i.finishingPosition
                highestPositions = [];
                highestPositions.push(i)
            } else if (i.finishingPosition === highest) {
                highestPositions.push(i)
            }
        });
        highestPositions.sort((a, b) => a.grandPrix.date > b.grandPrix.date ? -1 : 1);
        setHighestPosition(highest);
        setHighestPositionResults(highestPositions);
        setResults(fetchedResults);
    }, [fetchedResults]);

    return (
        <section>
            {
                results.length > 0 ? (
                    <div>
                        <DriverInfo driver={driver} image={image} highestPositionResults={highestPositionResults} highestPosition={highestPosition} />
                        <DriverResults results={results} />
                    </div>
                ) : (
                    <RingSpinner />
                )
            }
        </section>
    );
};
