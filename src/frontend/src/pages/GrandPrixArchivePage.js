import { React } from 'react';
import { GrandPrixTable } from '../components/GrandPrixTable';
import { GrandPrixYearSelector } from '../components/GrandPrixYearSelector';

export const GrandPrixArchivePage = () => {
  return (
    <div className="GrandPrixArchivePage">
      <h1>Formula 1: Grand Prix archives</h1>
      <GrandPrixYearSelector />
    </div>
  );
}
