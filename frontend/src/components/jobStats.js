import React, { useState, useEffect } from 'react';
import { getJobStats } from '../api/jobApi'; // Assuming jobApi.js is in src/api

function JobStats({ userId }) {
  const [stats, setStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!userId) {
      setLoading(false);
      return;
    }

    const fetchStats = async () => {
      setLoading(true);
      setError('');
      try {
        const response = await getJobStats(userId);
        setStats(response.data);
      } catch (err) {
        console.error('Error fetching job stats:', err);
        setError('Failed to load job statistics.');
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [userId]); // Re-fetch when userId changes

  if (loading) return <p>Loading statistics...</p>;
  if (error) return <p className="error-message">{error}</p>;

  // Check if stats object is empty
  if (Object.keys(stats).length === 0) {
    return <p>No job statistics available yet.</p>;
  }

  return (
    <div className="job-stats-container">
      <h2>Job Statistics</h2>
      <ul className="stats-list">
        {Object.entries(stats).map(([status, count]) => (
          <li key={status}>
            <strong>{status}:</strong> {count}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default JobStats;