import "./RaceResultPage.scss"
import { React, useEffect, useState, } from 'react';
import { useParams } from 'react-router-dom';
import { RaceResultTable } from '../components/RaceResultTable';
import { QualificationResultTable } from '../components/QualificationResultTable';
import { RaceResultGrandPrixDetails } from '../components/RaceResultGrandPrixDetails';
import { CircuitInfo } from '../components/CircuitInfo';

import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`tabpanel-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired,
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
  };
}

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    backgroundColor: theme.palette.background.paper,
  },
}));

export const RaceResultPage = () => {

  const classes = useStyles();
  const [value, setValue] = useState(0);
  const [qualificationExists, setQualificationExists] = useState(false);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const [grandPrixRaceResult, setGrandPrixRaceResult] = useState({ raceResult: [], qualificationResult: [] });
  const { grandPrixId } = useParams();

  useEffect(
    () => {
      const fetchRaceResults = async () => {
        const response = await fetch(`http://localhost:8080/grands-prix/${grandPrixId}/all-results`);
        const data = await response.json();
        if (data.name) {
          Object.entries(data.raceResult).forEach(([, value]) => {
            if (value.driverNumber === null) value.driverNumber = "-";
            if (value.points === 0) value.points = "";
            if (value.fastestLapTime === null) value.fastestLapTime = "-";
            if (value.time === null) value.time = value.status;
          })
        }
        setQualificationExists(data.qualificationResult.length > 0 ? true : false);

        setGrandPrixRaceResult(data);
      };
      fetchRaceResults();
    }, [grandPrixId]);

  return (
    <div className="RaceResultPage">
      <div className="title">
        <RaceResultGrandPrixDetails grandPrix={grandPrixRaceResult} />
      </div>
      <div className="tab-panel">
        <Tabs value={value} onChange={handleChange}>
          <Tab label="Race Results" {...a11yProps(0)} />
          <Tab label="Qualification" {...a11yProps(1)} disabled={!qualificationExists} />
          <Tab label="Circuit" {...a11yProps(2)} />
        </Tabs>
      </div>
      <TabPanel value={value} index={0}>
        <RaceResultTable raceResults={grandPrixRaceResult.raceResult} />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <QualificationResultTable raceResults={grandPrixRaceResult.qualificationResult} />
      </TabPanel>
      <TabPanel value={value} index={2}>
        <CircuitInfo circuit={grandPrixRaceResult.circuit} />
      </TabPanel>
    </div >
  );
}
