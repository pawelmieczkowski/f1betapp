import "./DriverInfo.scss"
export const DriverInfo = ({ driver, image }) => {



    return (
        <section className="DriverInfo">
            <div className="driver-name">
                {driver.forename} {driver.surname}
            </div>
            <section className="driver-photo">
                <img src={image} alt={driver.forename}/>
            </section>
            <section className="driver-info">
                <div className="driver-info-left">
                    Date of birth:
                </div>
                <div>
                    {driver.dateOfBirth}
                </div>
                <div className="driver-info-left">
                    Nationality:
                </div>
                <div>
                    {driver.nationality}
                </div>
                <section className="driver-wikipage">
                    <a href={driver.url} target="_blank" rel="noopener noreferrer">Article on wikipedia</a>
                </section >
            </section>
        </section >
    );
};