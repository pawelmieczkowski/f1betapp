import { React } from 'react';
import { GrandPrixArchiveTable } from '../components/GrandPrixArchiveTable';
import { GrandPrixYearSelector } from '../components/GrandPrixYearSelector';


export const GrandPrixArchivePage = () => {
  return (
    <div className="GrandPrixArchivePage">
      <h1>Formula 1: Grand Prix archives</h1>
      <GrandPrixYearSelector />
      <GrandPrixArchiveTable />
    </div>
  );
}
