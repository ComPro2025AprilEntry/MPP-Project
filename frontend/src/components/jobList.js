// frontend/src/components/jobList.js
import React, { useState, useEffect, useCallback } from 'react';
import { fetchAllJobs, deleteJob, filterJobsByTechStack, sortJobsByDeadline, filterJobsByStatus } from '../api/jobApi';
import { toast } from 'react-toastify';

function JobList({ user, onJobChange, onJobSelected }) {
  // ... (rest of your state variables remain the same)
  const [jobs, setJobs] = useState([]);
  const [initialLoading, setInitialLoading] = useState(true);
  const [isFetching, setIsFetching] = useState(false);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedFilterTechStack, setDebouncedFilterTechStack] = useState('');
  const [sortOption, setSortOption] = useState('');
  const [statusFilter, setStatusFilter] = useState('');

  const userId = user?.id;

  // ... (useEffect for debouncing remains the same)
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedFilterTechStack(searchTerm);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [searchTerm]);

  // ... (loadJobs useCallback remains the same)
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
      } else if (statusFilter) {
        response = await filterJobsByStatus(userId, statusFilter);
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
  }, [userId, initialLoading, isFetching, jobs.length, debouncedFilterTechStack, sortOption, statusFilter]);

  useEffect(() => {
    loadJobs();
  }, [loadJobs]);


  const confirmDeleteToast = (jobId, onConfirm, onCancel) => {
    // Add a defensive check for toast.POSITION
    const position = toast && toast.POSITION ? toast.POSITION.TOP_CENTER : 'top-center'; // Fallback to string if needed for debugging

    toast.warn(
      ({ closeToast }) => (
        <div style={{ textAlign: 'center' }}>
          <p style={{ margin: '0 0 15px 0', fontSize: '1rem' }}>Are you sure you want to delete this job application?</p>
          <button
            onClick={() => {
              onConfirm(jobId);
              closeToast();
            }}
            style={{
              marginRight: '10px',
              backgroundColor: '#dc3545',
              color: 'white',
              border: 'none',
              padding: '8px 15px',
              borderRadius: '5px',
              cursor: 'pointer',
              fontSize: '0.9rem'
            }}
          >
            Yes, Delete
          </button>
          <button
            onClick={() => {
              onCancel();
              closeToast();
            }}
            style={{
              backgroundColor: '#6c757d',
              color: 'white',
              border: 'none',
              padding: '8px 15px',
              borderRadius: '5px',
              cursor: 'pointer',
              fontSize: '0.9rem'
            }}
          >
            Cancel
          </button>
        </div>
      ),
      {
        autoClose: false,
        closeButton: false,
        draggable: false,
        closeOnClick: false,
        position: position, // <--- MODIFIED: Use the 'position' variable
        className: 'custom-toast-confirm'
      }
    );
  };

  const handleDelete = async (id) => {
    confirmDeleteToast(
      id,
      async (jobIdToConfirmDelete) => {
        try {
          await deleteJob(jobIdToConfirmDelete, userId);
          toast.success("Job application deleted successfully!");
          onJobChange();
        } catch (err) {
          console.error("Error deleting job:", err);
          toast.error("Failed to delete job application.");
        }
      },
      () => {
        toast.info("Deletion cancelled.");
      }
    );
  };

  // ... (getStatusColor function remains the same)
  const getStatusColor = (status) => {
    switch (status) {
      case 'Applied': return '#A3D9FF';
      case 'Interviewing': return '#FFECB3';
      case 'Offer': return '#C8E6C9';
      case 'Rejected': return '#FFC9C9';
      case 'Accepted': return '#5cb85c';
      default: return '#D3D3D3';
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

      <div className="job-controls">
        <input
          type="text"
          placeholder="Filter by Tech Stack..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="filter-input"
        />
        <select
          value={statusFilter}
          onChange={(e) => {
            setStatusFilter(e.target.value);
            setSearchTerm('');
            setDebouncedFilterTechStack('');
            setSortOption('');
          }}
          className="filter-select"
        >
          <option value="">All Statuses</option>
          <option value="Applied">Applied</option>
          <option value="Interviewing">Interviewing</option>
          <option value="Offer">Offer</option>
          <option value="Rejected">Rejected</option>
          <option value="Accepted">Accepted</option>
        </select>
        <button
          onClick={() => {
            setSortOption(sortOption === 'deadline' ? '' : 'deadline');
            setSearchTerm('');
            setDebouncedFilterTechStack('');
            setStatusFilter('');
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
            {searchTerm || sortOption || statusFilter ?
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