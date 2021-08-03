import { React, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { TeamInfo } from "../components/TeamInfo.js";

export const TeamPage = () => {
    const [team, setTeam] = useState([]);
    const [image, setImage] = useState([]);
    const { teamName } = useParams();

    useEffect(
        () => {
            const fetchTeam = async () => {
                const response = await fetch(`http://localhost:8080/teams?name=${teamName}`);
                const data = await response.json();
                setTeam(data);

                const article = data.url.split("wiki/")[1]
                const imageResponse = await fetch(`https://en.wikipedia.org/w/api.php?action=query&origin=*&titles=${article}&generator=images&gimlimit=10&prop=imageinfo&iiprop=url|dimensions|mime&format=json`);
                const imageData = await imageResponse.json();
                try {
                    const keyArray = Object.keys(imageData.query.pages);
                    let imageUrl;
                    for (let i = 0; i < keyArray.length; i++) {
                        let asd = keyArray[i];
                        const url = imageData.query.pages[asd].imageinfo[0].url;
                        if (url && !url.includes('logo') && !url.includes('Flag')) {
                            imageUrl = url;
                            break;
                        }
                    }
                    setImage(imageUrl);
                } catch (error) {
                    setImage("/Placeholder.svg");
                }

            };
            fetchTeam();
        }, [teamName, setImage]
    );
    return (
        <TeamInfo team={team} image={image} />
    );
};