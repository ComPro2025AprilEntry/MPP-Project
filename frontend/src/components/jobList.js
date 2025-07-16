import React, { useState, useEffect, useCallback } from 'react';
import { fetchAllJobs, deleteJob, filterJobsByTechStack, sortJobsByDeadline } from '../api/jobApi';
import { toast } from 'react-toastify';

function JobList({ user, onJobChange }) {
  const [jobs, setJobs] = useState([]);
  const [initialLoading, setInitialLoading] = useState(true);
  const [isFetching, setIsFetching] = useState(false);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedFilterTechStack, setDebouncedFilterTechStack] = useState('');
  const [sortOption, setSortOption] = useState('');

  const userId = user?.id;

  const loadJobs = useCallback(async () => {
    if (!userId) {
      setInitialLoading(false);
      setIsFetching(false);
      return;
    }

    if (jobs.length === 0 && !initialLoading && !isFetching) {
        setInitialLoading(true);
    }
    setIsFetching(true);
    setError('');

    try {
      let response;
      if (debouncedFilterTechStack) {
        response = await filterJobsByTechStack(userId, debouncedFilterTechStack);
      } else if (sortOption === 'deadline') {
        response = await sortJobsByDeadline(userId);
      } else {
        response = await fetchAllJobs(userId);
      }
      setJobs(response.data);
    } catch (err) {
      console.error('Error fetching jobs:', err);
      setError('Failed to load job applications.');
      toast.error('Failed to load job applications.');
    } finally {
      setInitialLoading(false);
      setIsFetching(false);
    }
  }, [userId, debouncedFilterTechStack, sortOption, initialLoading, isFetching, jobs.length]);

  useEffect(() => {
    loadJobs();
  }, [loadJobs]);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedFilterTechStack(searchTerm);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm]);

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this job application?')) {
      try {
        await deleteJob(id);
        toast.success('Job deleted successfully!');
        onJobChange();
      } catch (err) {
        console.error('Error deleting job:', err);
        toast.error('Failed to delete job.');
      }
    }
  };

  // --- Render Logic Changes ---
  if (initialLoading) {
      return <p>Loading jobs for the first time...</p>;
  }

  // Error message can be displayed permanently or as a toast
  // For persistent display, keep this. For toast-only, remove this block.
  if (error) {
      return <p className="error-message">{error}</p>;
  }

  return (
    <div className="job-list-container">
      <h2>Your Applications</h2>

      {/* FILTER AND SORT CONTROLS - ALWAYS RENDER THESE */}
      <div className="job-controls">
        <input
          type="text"
          placeholder="Filter by Tech Stack (e.g., React, Java)"
          value={searchTerm}
          onChange={(e) => {
            setSearchTerm(e.target.value);
            setSortOption('');
          }}
          className="filter-input"
        />
        <button
          onClick={() => {
            setSortOption(sortOption === 'deadline' ? '' : 'deadline');
            setSearchTerm('');
            setDebouncedFilterTechStack('');
          }}
          className={`sort-button ${sortOption === 'deadline' ? 'active' : ''}`}
        >
          {sortOption === 'deadline' ? 'Unsort by Deadline' : 'Sort by Deadline'}
        </button>
      </div>

      {/* Conditional Spinner/Overlay when fetching, without hiding existing content */}
      {isFetching && (
        <div className="loading-overlay">
          <div className="spinner"></div>
        </div>
      )}

      {/* Display the job list or the "No applications" message conditionally */}
      {jobs.length === 0 && !isFetching && !initialLoading ? ( // Only show this if no jobs AND not currently fetching AND not initial loading
        <p>No job applications found matching your criteria. Try a different search!</p>
      ) : (
        <div className="job-list">
          {jobs.map(job => (
            <div key={job.id} className="job-card">
              <h3>{job.company} - {job.position}</h3>
              <p><strong>Tech Stack:</strong> {job.techStack && job.techStack.length > 0 ? job.techStack.join(', ') : 'N/A'}</p>
              <p><strong>Applied Date:</strong> {job.appliedDate}</p>
              <p><strong>Deadline:</strong> {job.deadline || 'N/A'}</p>
              <p><strong>Status:</strong> {job.status}</p>
              <button onClick={() => handleDelete(job.id)}>Delete</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default JobList;