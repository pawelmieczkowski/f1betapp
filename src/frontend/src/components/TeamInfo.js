import "./TeamInfo.scss"
export const TeamInfo = ({ team, highestPositionResults, highestPosition }) => {



    return (
        <section className="TeamInfo">
            <div className="team-name">
                {team.name}
            </div>
            <section className="team-info">
                <div className="team-info-left">
                    <div className="text">
                        <div className="title">
                            Nationality:
                        </div>
                        <div>
                            {team.nationality}
                        </div>
                    </div>
                </div>
                <div className="team-wikipage">
                    <a href={team.url} target="_blank" rel="noopener noreferrer">Article on wikipedia</a>
                </div >
            </section>
        </section>
    );
};