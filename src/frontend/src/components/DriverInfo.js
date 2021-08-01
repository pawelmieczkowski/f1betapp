import "./DriverInfo.scss"
export const DriverInfo = ({ driver, image }) => {



    return (
        <section className="DriverInfo">
            <section className="driver-photo">
                <img src={image} alt={driver.forename}></img>
            </section>
            <section className="driver-info">
                <div className="driver-name">
                    {driver.forename} {driver.surname}
                </div>
                <section className="driver-birthdate">
                    <div>
                        Date of birth:
                    </div>
                    <div>
                        {driver.dateOfBirth}
                    </div>
                </section >
                <section className="driver-nationality">
                    <div>
                        Nationality:
                    </div>
                    <div>
                        {driver.nationality}
                    </div>
                </section >
                <section className="driver-wikipage">
                    <a href={driver.url} target="_blank" rel="noopener noreferrer">Article on wikipedia</a>
                </section >
            </section>
        </section >
    );
};