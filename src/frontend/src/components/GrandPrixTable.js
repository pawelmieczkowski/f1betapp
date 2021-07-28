import { React, useEffect } from 'react';

export const GrandPrixTable = () => {

  useEffect(
    () => {
      const fetchMatches = async () => {
        const response = await fetch('http://localhost:8080/grands-prix/1');
        const data = await response.json();
      };
      fetchMatches();
    },
  );

  return (
    <div className="GrandPrixTable">
      <h1>TABLE</h1>
    </div >
  );
}
