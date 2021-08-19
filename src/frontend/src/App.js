import "./App.scss";
import React from "react";
import { Header } from "./components/common";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import { GrandPrixArchivePage } from "./pages/GrandPrixArchivePage";
import { RaceResultPage } from "./pages/RaceResultPage";
import { DriverPage } from "./pages/DriverPage";
import { TeamPage } from "./pages/TeamPage";
import { ProfilePage } from "./pages/ProfilePage";
import { Register } from "./pages/RegisterPage";
import { CircuitPage } from "./pages/CircuitPage";
import { Login } from "./pages/LoginPage";
import { DriverMenuPage } from "./pages/DriverMenuPage";

function App() {
  return (
    <div className="App">
      <Router>
        <div className="header">
          <Header />
        </div>
        <div className="page-content">
          <Switch>
            <Route
              path="/race-result/:grandPrixId"
              component={RaceResultPage}
            />
            <Route path="/quiz" component={GrandPrixArchivePage} />
            <Route path="/drivers/:driverId" component={DriverPage} />
            <Route path="/circuits/:circuitId" component={CircuitPage} />
            <Route path="/drivers" component={DriverMenuPage} />
            <Route path="/teams/:teamName" component={TeamPage} />
            <Route exact path="/register" component={Register} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/profile" component={ProfilePage} />
            <Route path="/results" component={GrandPrixArchivePage} />
            <Route path="/" component={GrandPrixArchivePage} />
          </Switch>
        </div>
      </Router>
    </div>
  );
}

export default App;
