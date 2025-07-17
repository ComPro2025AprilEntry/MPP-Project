// frontend/src/components/jobList.js
import React, { useState, useEffect, useCallback } from 'react';
import { fetchAllJobs, deleteJob, filterJobsByTechStack, sortJobsByDeadline } from '../api/jobApi';
import { toast } from 'react-toastify';

function JobList({ user, onJobChange, onJobSelected }) {
  const [jobs, setJobs] = useState([]);
  const [initialLoading, setInitialLoading] = useState(true);
  const [isFetching, setIsFetching] = useState(false);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedFilterTechStack, setDebouncedFilterTechStack] = useState('');
  const [sortOption, setSortOption] = useState('');

  const userId = user?.id;

  // Effect for debouncing the tech stack filter
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedFilterTechStack(searchTerm);
    }, 500); // 500ms debounce

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm]);

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
      // Filter takes precedence over sort if both are active
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
  }, [userId, initialLoading, isFetching, jobs.length, debouncedFilterTechStack, sortOption]);

  useEffect(() => {
    loadJobs();
  }, [loadJobs]);

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this job application?")) {
      try {
        await deleteJob(id, userId); // Ensure userId is passed for the header
        toast.success("Job application deleted successfully!");
        onJobChange(); // Notify App.js to refresh lists/stats
      } catch (err) {
        console.error("Error deleting job:", err);
        toast.error("Failed to delete job application.");
      }
    }
  };

  // Status colors for better visual appeal
  const getStatusColor = (status) => {
    switch (status) {
      case 'Applied': return '#A3D9FF';       // Soft Light Blue
      case 'Interviewing': return '#FFECB3';  // Soft Light Amber/Yellow
      case 'Offer': return '#C8E6C9';         // Soft Light Green
      case 'Rejected': return '#FFC9C9';      // Soft Light Red/Pink
      case 'Accepted': return '#5cb85c';      // Reusing --secondary-green from App.css for a definitive success
      default: return '#D3D3D3';             // Light Grey
    }
  };

  if (initialLoading) {
    return <div className="job-list-container">Loading job applications...</div>;
  }

  if (error) {
    return <div className="job-list-container error-message">{error}</div>;
  }

  return (
    <div className="job-list-container">
      <h3>Your Job Applications</h3>

      {/* Filter and Sort Controls */}
      <div className="job-controls">
        <input
          type="text"
          placeholder="Filter by Tech Stack..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="filter-input"
        />
        <button
          onClick={() => {
            setSortOption(sortOption === 'deadline' ? '' : 'deadline');
            setSearchTerm(''); // Clear search when sorting
            setDebouncedFilterTechStack(''); // Clear debounced filter when sorting
          }}
          className={`sort-button ${sortOption === 'deadline' ? 'active' : ''}`}
        >
          {sortOption === 'deadline' ? 'Unsort by Deadline' : 'Sort by Deadline'}
        </button>
      </div>

      {isFetching && (
        <div className="loading-overlay">
          <div className="spinner"></div>
        </div>
      )}

      {jobs.length === 0 && !isFetching && !initialLoading ? (
        <p>
            {searchTerm || sortOption ?
                "No job applications found matching your criteria. Try a different search!" :
                "You haven't added any job applications yet."
            }
        </p>
      ) : (
        <div className="job-list">
          {jobs.map(job => (
            <div key={job.id} className="job-card">
              <div className="job-card-header">
                <h4 className="job-title">{job.position}</h4>
                <span className="job-status" style={{ backgroundColor: getStatusColor(job.status) }}>{job.status}</span>
              </div>
              <p className="job-company">{job.company}</p>
              <p className="job-deadline">Deadline: {job.deadline || 'N/A'}</p>
              <div className="job-card-actions">
                <button onClick={() => onJobSelected(job)} className="view-edit-button">View/Edit</button>
                <button onClick={() => handleDelete(job.id)} className="delete-button">Delete</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default JobList;