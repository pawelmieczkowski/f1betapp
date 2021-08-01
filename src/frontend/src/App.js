import './App.scss';
import { Header } from './components/common'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import { GrandPrixArchivePage } from './pages/GrandPrixArchivePage';
import { RaceResultPage } from './pages/RaceResultPage';
import { DriverPage } from './pages/DriverPage';

function App() {
  return (
    <div className="App">
      <Header />
      <Router>
        <Switch>
          <Route path="/race-result/:grandPrixId">
            <RaceResultPage />
          </Route>
          <Route path="/drivers/:driverId">
            <DriverPage />
          </Route>
          <Route path="/">
            <GrandPrixArchivePage />
          </Route>
        </Switch>
      </Router>
    </div>
  );
}

export default App;
