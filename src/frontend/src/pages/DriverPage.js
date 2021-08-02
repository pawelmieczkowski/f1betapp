import { React, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { DriverInfo } from "../components/DriverInfo.js";

export const DriverPage = () => {
    const [driver, setDriver] = useState([]);
    const [image, setImage] = useState([]);
    const { driverId } = useParams();

    useEffect(
        () => {
            const fetchDriver = async () => {
                const response = await fetch(`http://localhost:8080/drivers/${driverId}`);
                const data = await response.json();
                setDriver(data);

                const article = data.url.split("wiki/")[1]
                const imageResponse = await fetch(`https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&origin=*&format=json&titles=${article}&pithumbsize=500`);
                const imageData = await imageResponse.json();
                try {
                    const key = Object.keys(imageData.query.pages)[0]
                    const imageUrl = imageData.query.pages[key].thumbnail.source;
                    setImage(imageUrl);
                } catch (error) {
                    setImage("/Placeholder.svg")
                }

            };
            fetchDriver();
        }, [driverId, setImage]
    );
    return (
        <DriverInfo driver={driver} image={image} />
    );
};