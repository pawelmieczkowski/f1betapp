import "./RaceResultPage.scss"
import { React, useEffect, useState, } from 'react';
import { useParams } from 'react-router-dom';
import { RaceResultTable } from '../components/RaceResultTable';
import { RaceResultGrandPrixDetails } from '../components/RaceResultGrandPrixDetails';

export const RaceResultPage = () => {

  const [grandPrixRaceResult, setGrandPrixRaceResult] = useState({ raceResult: [] });
  const { grandPrixId } = useParams();

  useEffect(
    () => {
      const fetchRaceResults = async () => { 
        const response = await fetch(`http://localhost:8080/grands-prix/${grandPrixId}/results`);
        const data = await response.json();
        if (data.name) {
          Object.entries(data.raceResult).forEach(([, value]) => {
            if (value.driverNumber === null) value.driverNumber = "-";
            if (value.points === 0) value.points = "";
            if (value.fastestLapTime === null) value.fastestLapTime = "-";
            if (value.time === null) value.time = value.status;
          })
        }
        setGrandPrixRaceResult(data);
      };
      fetchRaceResults();
    }, [grandPrixId]);
  if (!grandPrixRaceResult || !grandPrixRaceResult.name) {
    return <h1>Results not found</h1>
  }
  return (
    <div className="RaceResultPage">
      <RaceResultGrandPrixDetails grandPrix={grandPrixRaceResult} />
      <RaceResultTable raceResults={grandPrixRaceResult.raceResult} />
    </div >
  );
}
