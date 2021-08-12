import "./DriverInfo.scss"
import { Link } from 'react-router-dom';

export const DriverInfo = ({ driver, image, highestPositionResults, highestPosition }) => {

    return (
        <section className="DriverInfo">
            <div className="driver-name">
                {driver.forename} {driver.surname}
            </div>
            <section className="driver-photo">
                <img src={image} alt={driver.forename} />
            </section>
            <section className="driver-info">
                <div className="driver-info-left">
                    <div className="text">
                        <div className="title">
                            Date of birth:
                        </div>
                        <div>
                            {driver.dateOfBirth}
                        </div>
                        <div className="title">
                            Nationality:
                        </div>
                        <div>
                            {driver.nationality}
                        </div>
                    </div>
                    <div className="driver-wikipage">
                        <a href={driver.url} target="_blank" rel="noopener noreferrer">Read more on wikipedia</a>
                    </div >
                </div>
                <div className="driver-info-right">
                    <div>
                        Highest position: #{highestPosition}
                    </div>
                    <div className="scrollable">
                        {highestPositionResults.map(position =>
                            <div key={position.grandPrix.date}>
                                <Link to={'/race-result/' + position.grandPrix.id}> {position.grandPrix.year} {position.grandPrix.name}</Link>
                            </div>
                        )}
                    </div>
                </div>
            </section>
        </section >
    );
};