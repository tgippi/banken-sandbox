import React from 'react';
import './App.css';
import Banken from "./Banken";
import IbanForm from "./IbanForm";

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <IbanForm></IbanForm>
                <Banken></Banken>
            </header>
        </div>
    );
}

export default App;
