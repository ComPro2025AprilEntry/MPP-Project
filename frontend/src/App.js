import React, { useEffect, useState, useCallback } from 'react';
import { fetchAllJobs, deleteJob } from './api/jobApi';
import JobList from "./components/jobList";
import JobForm from "./components/jobForm";
import Login from "./auth/Login";
import Register from "./auth/Register";
import "./App.css"

function App() {
  const [jobs, setJobs] = useState([]);
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem('user')));
  const [mode, setMode] = useState('login'); // 'login' or 'register'

  const loadJobs = useCallback(() => {
  if (user) {
    fetchAllJobs(user.id).then(res => setJobs(res.data));
  }
}, [user]);

  useEffect(() => {
    loadJobs();
  }, [loadJobs]);

  const handleDelete = async (id) => {
    await deleteJob(id);
    loadJobs();
  };

  const logout = () => {
    localStorage.removeItem('user');
    setUser(null);
    setJobs([]);
  };

  if (!user) {
    
    return (
    <div className="auth-container"> {/* Changed this class */}
        {mode === 'login' ? (
        <>
            <Login onLogin={setUser} />
            <p>Don't have an account? <button onClick={() => setMode('register')}>Register</button></p>
        </>
        ) : (
        <>
            <Register onRegister={setUser} />
            <p>Already have an account? <button onClick={() => setMode('login')}>Login</button></p>
        </>
        )}
    </div>
    );
  }

  return (
    <div className="container">
      <h1>🎯 Job Tracker</h1>
      <button onClick={logout}>Logout</button>
      <JobForm onJobAdded={loadJobs} />
      <div className="job-list">
        <JobList jobs={jobs} onDelete={handleDelete} />
      </div>
    </div>
  );
}

export default App;
