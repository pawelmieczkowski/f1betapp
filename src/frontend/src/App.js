import './App.scss';
import React from 'react';
import {Header} from './components/common'
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import {GrandPrixArchivePage} from './pages/GrandPrixArchivePage';
import {RaceResultPage} from './pages/RaceResultPage';
import {DriverPage} from './pages/DriverPage';
import {TeamPage} from './pages/TeamPage';
import {Register} from './components/Register';
import {Login} from './components/Login';

function App() {
    return (
        <div className="App">
            <Header/>
            <div className="page-content">
                <Router>
                    <Switch>
                        <Route path="/race-result/:grandPrixId">
                            <RaceResultPage/>
                        </Route>
                        <Route path="/drivers/:driverId" component={DriverPage}/>
                        <Route path="/teams/:teamName">
                            <TeamPage/>
                        </Route>
                        <Route exact path="/register">
                            <Register/>
                        </Route>
                        <Route exact path="/login">
                            <Login/>
                        </Route>
                        <Route path="/">
                            <GrandPrixArchivePage/>
                        </Route>
                    </Switch>
                </Router>
            </div>
        </div>
    );
}

export default App;
