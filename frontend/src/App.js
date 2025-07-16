// frontend/src/App.js
import React, { useState, useEffect } from 'react';
import Login from './auth/Login';
import Register from './auth/Register';
import JobForm from "./components/jobForm";
import JobList from "./components/jobList";
import JobStats from "./components/jobStats";
import { ToastContainer } from 'react-toastify'; // Import ToastContainer
import 'react-toastify/dist/ReactToastify.css'; // Import toastify CSS

import "./App.css"

function App() {
  const [user, setUser] = useState(null);
  const [mode, setMode] = useState('login');
  const [jobListRefreshKey, setJobListRefreshKey] = useState(0);

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
    setMode('jobs');
    setJobListRefreshKey(prev => prev + 1);
  };

  const handleRegister = (userData) => {
    setUser(userData);
    setMode('jobs');
    setJobListRefreshKey(prev => prev + 1);
  };

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
    setMode('login');
    setJobListRefreshKey(0);
  };

  const handleJobChange = () => {
    setJobListRefreshKey(prev => prev + 1);
  };

  return (
    <div className="container">
      <h1>Job Application Tracker 🎯</h1>

      {user ? (
        <>
          <p>Welcome, {user.name}!</p>
          <button onClick={handleLogout} className="logout-button">Logout</button>

          <JobForm user={user} onJobAdd={handleJobChange} />
          <JobStats userId={user.id} key={`stats-${jobListRefreshKey}`} />
          <JobList user={user} onJobChange={handleJobChange} key={`list-${jobListRefreshKey}`} />
        </>
      ) : (
        <div className="auth-container">
          {mode === 'login' ? (
            <>
              <Login onLogin={handleLogin} />
              <p>Don't have an account? <button onClick={() => setMode('register')}>Register</button></p>
            </>
          ) : (
            <>
              <Register onRegister={handleRegister} />
              <p>Already have an account? <button onClick={() => setMode('login')}>Login</button></p>
            </>
          )}
        </div>
      )}
      <ToastContainer /> {/* Add this component */}
    </div>
  );
}

export default App;