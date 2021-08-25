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
import { CircuitMenuPage } from "./pages/CircuitMenuPage";
import { Login } from "./pages/LoginPage";
import { DriverMenuPage } from "./pages/DriverMenuPage";
import { TeamMenuPage } from "./pages/TeamMenuPage";
import { QuizPage } from "./pages/QuizPage";

import ErrorHandler from "./services/errorHandling/ErrorHandler";
import { Page404 } from "./pages/error/Page404"

function App() {
  return (
    <div className="App">
      <Router>
        <ErrorHandler>
          <div className="header">
            <Header />
          </div>
          <div className="page-content">
            <Switch>
              <Route
                path="/race-result/:grandPrixId"
                component={RaceResultPage}
              />
              <Route path="/quiz" component={QuizPage} />
              <Route path="/drivers/:driverId" component={DriverPage} />
              <Route path="/circuits/:circuitId" component={CircuitPage} />
              <Route path="/circuits" component={CircuitMenuPage} />
              <Route path="/drivers" component={DriverMenuPage} />
              <Route path="/teams/:teamName" component={TeamPage} />
              <Route path="/teams" component={TeamMenuPage} />
              <Route exact path="/register" component={Register} />
              <Route exact path="/login" component={Login} />
              <Route exact path="/profile" component={ProfilePage} />
              <Route path="/results" component={GrandPrixArchivePage} />
              <Route exact path="/" component={GrandPrixArchivePage} />
              <Route component={Page404} />
            </Switch>
          </div>
        </ErrorHandler>
      </Router>
    </div>
  );
}

export default App;
