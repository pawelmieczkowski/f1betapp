import { React } from 'react';
import {getCountryCode} from '../components/CountryCode'

export const GrandPrixYearSelector = () => {
  console.log(getCountryCode('France'));
  return (
    <div className="GrandPrixYearSelector">
      <h1>Year Selector</h1>
    </div>
  );
}
