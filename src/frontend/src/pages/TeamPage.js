import { React, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { TeamInfo } from "../components/TeamInfo.js";

export const TeamPage = () => {
    const [team, setTeam] = useState([]);
    const { teamName } = useParams();

    useEffect(
        () => {
            const fetchTeam = async () => {
                const response = await fetch(`http://localhost:8080/teams?name=${teamName}`);
                const data = await response.json();
                setTeam(data);
            };
            fetchTeam();
        }, [teamName,]
    );
    return (
        <TeamInfo team={team} />
    );
};