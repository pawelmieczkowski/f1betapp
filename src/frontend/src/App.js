import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import { GrandPrixArchivePage } from './pages/GrandPrixArchivePage';
import { RaceResultPage } from './pages/RaceResultPage';

function App() {
  return (
    <div className="App">
      <Router>
        <Switch>
          <Route path="/race-result/:grandPrixId">
            <RaceResultPage />
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
