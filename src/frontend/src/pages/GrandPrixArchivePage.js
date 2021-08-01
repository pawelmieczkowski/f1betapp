import { React, useState, useEffect } from 'react';
import { GrandPrixArchiveTable } from '../components/GrandPrixArchiveTable';
import { GrandPrixYearSelector } from '../components/GrandPrixYearSelector';


export const GrandPrixArchivePage = () => {

  const [grandsPrix, setGrandPrix] = useState([]);
  const [yearSelected, setYearSelected] = useState(2021);
  const [years, setYears] = useState([]);

  const handleCallback = (childData) => {
    setYearSelected(childData.year)
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
      <h1>Formula 1: Grand Prix archives</h1>
      <GrandPrixYearSelector parentCallback={handleCallback} years={years} />
      <GrandPrixArchiveTable grandsPrix={grandsPrix} />
    </div>
  );
}
