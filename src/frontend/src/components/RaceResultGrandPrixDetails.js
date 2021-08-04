import './RaceResultGrandPrixDetails.scss'

export const RaceResultGrandPrixDetails = ({ grandPrix }) => {


    return (
        <div className="RaceResultGrandPrixDetails">
            <div className="grand-prix-name">
                {grandPrix.year} {grandPrix.name}
            </div>
        </div>
    )
}