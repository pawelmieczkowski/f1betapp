import "./GrandPrixArchivePage.scss"
import {React, useEffect, useState} from 'react';
import {GrandPrixArchiveTable} from '../components/GrandPrixArchiveTable';
import {GrandPrixYearSelector} from '../components/GrandPrixYearSelector';
import {useQuery} from '../services/errorHandling/useQuery'
import {RingSpinner} from '../components/common/spinner/RingSpinner';

export const GrandPrixArchivePage = () => {
  const [grandsPrix, setGrandPrix] = useState([]);
  const [yearSelected, setYearSelected] = useState(2021);
  const [years, setYears] = useState([]);

  const handleCallback = (childData) => {
    setYearSelected(childData)
  }

  const fetchedYears = useQuery({
    url: `${process.env.REACT_APP_API_ROOT_URL}/api/grands-prix/years`
  }).data;

  const fetchedGrandPrix = useQuery({
    url: `${process.env.REACT_APP_API_ROOT_URL}/api/grands-prix?year=${yearSelected}`
  }).data;

  useEffect(
    () => {
      fetchedYears.sort().reverse();
      setYears(fetchedYears);
    }, [fetchedYears]
  );
  useEffect(
    () => {
      fetchedGrandPrix.sort((a, b) => a.date > b.date ? 1 : -1);
      setGrandPrix(fetchedGrandPrix);
    }, [fetchedGrandPrix]
  );

  return (
    <div className="GrandPrixArchivePage">
      {
        grandsPrix.length > 0 ? (
          <div>
            <div className="title">
              Formula 1: Grand Prix archives
            </div>
            <GrandPrixYearSelector parentCallback={handleCallback} years={years} yearSelected={yearSelected} />
            <GrandPrixArchiveTable grandsPrix={grandsPrix} />
          </div>
        ) : (
          <RingSpinner />
        )
      }
    </div>
  );
}
