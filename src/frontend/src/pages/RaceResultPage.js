import "./RaceResultPage.scss"
import { React, useEffect, useState, } from 'react';
import { useParams } from 'react-router-dom';
import { RaceResultTable } from '../components/RaceResultTable';
import { QualificationResultTable } from '../components/QualificationResultTable';
import { RaceResultGrandPrixDetails } from '../components/RaceResultGrandPrixDetails';
import { CircuitTab } from '../components/CircuitTab';


export const RaceResultPage = () => {

  const [grandPrixRaceResult, setGrandPrixRaceResult] = useState({ raceResult: [], qualificationResult: [] });
  const { grandPrixId } = useParams();
  const [qualificationExists, setQualificationExists] = useState(false);
  const [raceExists, setRaceExists] = useState(false);
  const [selected, setSelected] = useState(0);

  const handleChange = (newValue) => {
    setSelected(newValue);
  };

  useEffect(
    () => {
      const fetchRaceResults = async () => {
        const response = await fetch(`http://localhost:8080/grands-prix/${grandPrixId}/all-results`);
        const data = await response.json();
        console.log(data)
        if (data.name) {
          Object.entries(data.raceResult).forEach(([, value]) => {
            if (value.driverNumber === null) value.driverNumber = "-";
            if (value.points === 0) value.points = "";
            if (value.fastestLapTime === null) value.fastestLapTime = "-";
            if (value.time === null) value.time = value.status;
          })
          setGrandPrixRaceResult(data);
          setQualificationExists(data.qualificationResult.length > 0);
          setRaceExists(data.raceResult.length > 0);
          setSelected(data.raceResult.length > 0 ? 0 : 2);
        }

      };
      fetchRaceResults();
    }, [grandPrixId]);

  return (
    <div className="RaceResultPage">
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
        <button className={selected === 2 ? "tab-button active" : "tab-button" } onClick={() => handleChange(2)}>
          Circuit
        </button>
      </div>
      {(selected === 0) && <div>
        <RaceResultTable raceResults={grandPrixRaceResult.raceResult} /></div>}
      {(selected === 1) && <div>
        <QualificationResultTable raceResults={grandPrixRaceResult.qualificationResult} /></div>}
      {(selected === 2) && <div>
        <CircuitTab circuit={grandPrixRaceResult.circuit} /></div>}
    </div >
  );
}
