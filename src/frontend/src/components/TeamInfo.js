import "./TeamInfo.scss"
export const TeamInfo = ({ team}) => {



    return (
        <section className="TeamInfo">
            <div className="team-name">
                {team.name}
            </div>
            <section className="team-info">
                <div className="team-info-left">
                    Nationality:
                </div>
                <div>
                     {team.nationality} 
                </div>
                <section className="team-wikipage">
                    <a href={team.url} target="_blank" rel="noopener noreferrer">Article on wikipedia</a>
                </section >
            </section>
        </section >
    );
};