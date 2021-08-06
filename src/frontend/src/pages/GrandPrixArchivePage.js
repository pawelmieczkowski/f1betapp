import "./GrandPrixArchivePage.scss"
import { React, useState, useEffect } from 'react';
import { GrandPrixArchiveTable } from '../components/GrandPrixArchiveTable';
import { GrandPrixYearSelector } from '../components/GrandPrixYearSelector';


export const GrandPrixArchivePage = () => {

  const [grandsPrix, setGrandPrix] = useState([]);
  const [yearSelected, setYearSelected] = useState(2021);
  const [years, setYears] = useState([]);

  const handleCallback = (childData) => {
    setYearSelected(childData)
  }

  useEffect(
    () => {
      const fetchYears = async () => {
        const response = await fetch(`http://localhost:8080/grands-prix/years`);
        const data = await response.json();
        data.sort().reverse();
        setYears(data);
      };
      fetchYears();
    }, []
  );
  useEffect(
    () => {
      const fetchRaceResults = async () => {
        const response = await fetch(`http://localhost:8080/grands-prix?year=${yearSelected}`);
        const data = await response.json();
        data.sort((a, b) => a.date > b.date ? 1 : -1);
        setGrandPrix(data);
      };
      fetchRaceResults();
    }, [yearSelected]
  );

  return (
    <div className="GrandPrixArchivePage">
      <div className="title">
        Formula 1: Grand Prix archives
      </div>
      <GrandPrixYearSelector parentCallback={handleCallback} years={years} yearSelected={yearSelected}/>
      <GrandPrixArchiveTable grandsPrix={grandsPrix} />
    </div>
  );
}
