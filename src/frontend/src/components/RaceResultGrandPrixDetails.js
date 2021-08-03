import './RaceResultGrandPrixDetails.scss'

export const RaceResultGrandPrixDetails = ({ grandPrix }) => {


    return (
        <div className="RaceResultGrandPrixDetails">
            <div className="grand-prix-name">
                {grandPrix.year} {grandPrix.name}
            </div>
            <div className="button-panel">
                <button onClick="">
                    Race Results
                </button>
                <button onClick="">
                    Qualifying Results
                </button>
                <button onClick="handleCircuit">
                    Circuit
                </button>
            </div>
        </div>
    )
}