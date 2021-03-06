import React from 'react';
import {useParams} from 'react-router-dom';
import {TeamInfo} from "../components/TeamInfo.js";
import {TeamResults} from "../components/TeamResults.js";
import {useQuery} from '../services/errorHandling/useQuery'
import {RingSpinner} from '../components/common/spinner/RingSpinner';

export const TeamPage = () => {
    const { teamName } = useParams();

    const team = useQuery({
        url: `${process.env.REACT_APP_API_ROOT_URL}/api/teams?name=${teamName}`
    }).data;

    return (
        <section>
            {
                team.name ? (
                    <div>
                        <TeamInfo team={team} />
                        <TeamResults teamName={teamName} />
                    </div>
                ) : (
                    <RingSpinner />
                )
            }
        </section>
    );
};