// frontend/src/App.js
import React, { useState, useEffect } from 'react';
import Login from './auth/Login';
import Register from './auth/Register';
import JobForm from "./components/jobForm";
import JobList from "./components/jobList";
import JobStats from "./components/jobStats";
import JobDetailsModal from "./components/jobDetailsModal"; // Import the modal
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import "./App.css"; // Assuming App.css contains general styling

function App() {
  const [user, setUser] = useState(null);
  const [mode, setMode] = useState('login');
  const [jobListRefreshKey, setJobListRefreshKey] = useState(0);

  // State for modal and editing
  const [selectedJob, setSelectedJob] = useState(null); // Stores job for details modal
  const [jobToEdit, setJobToEdit] = useState(null);    // Stores job for editing in the form

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogin = (userData) => {
    localStorage.setItem('user', JSON.stringify(userData)); // Store user data
    setUser(userData);
    setMode('jobs');
    setJobListRefreshKey(prev => prev + 1); // Refresh lists/stats after login
  };

  const handleRegister = (userData) => {
    localStorage.setItem('user', JSON.stringify(userData)); // Store user data
    setUser(userData);
    setMode('jobs');
    setJobListRefreshKey(prev => prev + 1); // Refresh lists/stats after register
  };

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
    setMode('login');
    setJobListRefreshKey(0); // Reset key on logout
    setJobToEdit(null); // Clear any job being edited
    setSelectedJob(null); // Clear any selected job
  };

  // This function is called when a job is added or updated, or deleted
  const handleJobChange = () => {
    setJobListRefreshKey(prev => prev + 1); // Increment key to trigger re-fetches
    setJobToEdit(null); // Exit edit mode
    setSelectedJob(null); // Close modal if open
  };

  // Handlers for JobDetailsModal interaction
  const handleJobSelectedForDetails = (job) => {
    setSelectedJob(job); // Set the job to display in the modal
  };

  const handleCloseJobDetails = () => {
    setSelectedJob(null); // Close the modal
  };

  // Handler to initiate edit from JobDetailsModal
  const handleEditFromModal = (job) => {
    setJobToEdit(job); // Set the job to be edited in the form
    setSelectedJob(null); // Close the details modal
  };

  return (
    <div className="container">
      <h1>Job Application Tracker 🎯</h1>

      {user ? (
        <>
          <p>Welcome, {user.name}!</p>
          <button onClick={handleLogout} className="logout-button">Logout</button>

          {/* Render JobForm for adding/editing */}
          <JobForm
            user={user}
            onJobAdd={handleJobChange}
            jobToEdit={jobToEdit} // Pass job if in edit mode
            onJobEditComplete={handleJobChange} // Call handleJobChange after edit
          />

          {/* Display Job Statistics */}
          <JobStats userId={user.id} key={`stats-${jobListRefreshKey}`} />

          {/* Display Job List */}
          <JobList
            user={user}
            onJobChange={handleJobChange} // Pass this to refresh list on delete
            onJobSelected={handleJobSelectedForDetails} // Pass handler for selecting a job
            key={`list-${jobListRefreshKey}`} // Key to force re-render on changes
          />

          {/* Job Details Modal - only rendered when a job is selected */}
          {selectedJob && (
            <JobDetailsModal
              job={selectedJob}
              onClose={handleCloseJobDetails}
              onEdit={handleEditFromModal} // Pass handler for editing from modal
            />
          )}
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
      <ToastContainer position="bottom-right" autoClose={3000} hideProgressBar newestOnTop closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover />
    </div>
  );
}

export default App;