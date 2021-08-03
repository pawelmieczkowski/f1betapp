import "./CircuitInfo.scss"
export const CircuitInfo = ({ circuit}) => {
    
    return (
        <section className="CircuitInfo">
            <div className="circuit-name">
                {circuit.name}
            </div>
            <section className="circuit-wikipage">
                <a href={circuit.url} target="_blank" rel="noopener noreferrer">Article on wikipedia</a>
            </section >
        </section>
    );
};