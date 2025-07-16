import React from 'react';

function JobList({ jobs, onDelete }) {
  return (
    <div>
      {jobs.map(job => (
        // In JobList.js, change the div inside the map
        <div key={job.id} className="job-card"> {/* Changed this class */}
            <h3>{job.position} @ {job.company}</h3>
            <p>Status: <strong>{job.status}</strong></p> {/* Added strong for status */}
            <p>Deadline: <strong>{job.deadline}</strong></p>
            <button onClick={() => onDelete(job.id)}>Delete</button>
        </div>
      ))}
    </div>
  );
}

export default JobList;
