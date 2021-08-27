import "./RaceResultPage.scss"
import { React, useEffect, useState, } from 'react';
import { useParams } from 'react-router-dom';
import { RaceResultTable } from '../components/RaceResultTable';
import { QualificationResultTable } from '../components/QualificationResultTable';
import { RaceResultGrandPrixDetails } from '../components/RaceResultGrandPrixDetails';
import { CircuitTab } from '../components/CircuitTab';
import { useQuery } from '../services/errorHandling/useQuery'
import { RingSpinner } from '../components/common/spinner/RingSpinner';

export const RaceResultPage = () => {
  const [grandPrixRaceResult, setGrandPrixRaceResult] = useState({ raceResult: [], qualificationResult: [] });
  const { grandPrixId } = useParams();
  const [qualificationExists, setQualificationExists] = useState(false);
  const [raceExists, setRaceExists] = useState(false);
  const [selected, setSelected] = useState(0);

  const handleChange = (newValue) => {
    setSelected(newValue);
  };

  const fetchedResults = useQuery({
    url: `http://localhost:8080/grands-prix/${grandPrixId}/all-results`
  }).data;

  useEffect(
    () => {
      if (fetchedResults.name) {
        Object.entries(fetchedResults.raceResult).forEach(([, value]) => {
          if (value.driverNumber === null) value.driverNumber = "-";
          if (value.points === 0) value.points = "";
          if (value.fastestLapTime === null) value.fastestLapTime = "-";
          if (value.time === null) value.time = value.status;
        })
        setGrandPrixRaceResult(fetchedResults);
        setQualificationExists(fetchedResults.qualificationResult.length > 0);
        setRaceExists(fetchedResults.raceResult.length > 0);
        setSelected(fetchedResults.raceResult.length > 0 ? 0 : 2);
      }
    }, [fetchedResults]);

  return (
    <div className="RaceResultPage">
      {
        grandPrixRaceResult.circuit ? (
          <div>
            <div className="title">
              <RaceResultGrandPrixDetails grandPrix={grandPrixRaceResult} />
            </div>
            <div className="tab-panel">
              <button className={selected === 0 ? "tab-button active" : "tab-button"} onClick={() => handleChange(0)} disabled={!raceExists}>
                Race Results
              </button>
              <button className={selected === 1 ? "tab-button active" : "tab-button"} onClick={() => handleChange(1)} disabled={!qualificationExists}>
                Qualification
              </button>
              <button className={selected === 2 ? "tab-button active" : "tab-button"} onClick={() => handleChange(2)}>
                Circuit
              </button>
            </div>
            {(selected === 0) && <div>
              <RaceResultTable raceResults={grandPrixRaceResult.raceResult} /></div>}
            {(selected === 1) && <div>
              <QualificationResultTable raceResults={grandPrixRaceResult.qualificationResult} /></div>}
            {(selected === 2) && <div>
              <CircuitTab circuit={grandPrixRaceResult.circuit} /></div>}
          </div>
        ) : (
          <RingSpinner />
        )
      }
    </div >
  );
}
