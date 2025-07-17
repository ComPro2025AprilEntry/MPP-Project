// frontend/src/components/jobDetailsModal.js
import React from 'react';
import "./JobDetailsModal.css"; // Ensure this CSS file exists and is linked

// Added 'onEdit' to the props
function JobDetailsModal({ job, onClose, onEdit }) {
    if (!job) return null; // Don't render if no job is provided

    // Handler for the "Edit Application" button
    const handleEditClick = () => {
        onClose(); // Close the details modal first
        onEdit(job); // Then trigger the edit function with the current job
    };

    return (
        <div className="modal-backdrop">
            <div className="modal-content">
                <h2>Job Application Details</h2>
                <p><strong>Company:</strong> {job.company}</p>
                <p><strong>Position:</strong> {job.position}</p>
                <p><strong>Tech Stack:</strong> {job.techStack && job.techStack.length > 0 ? job.techStack.join(', ') : 'N/A'}</p>
                <p><strong>Applied Date:</strong> {job.appliedDate || 'N/A'}</p> {/* Handle null/undefined */}
                <p><strong>Deadline:</strong> {job.deadline || 'N/A'}</p> {/* Handle null/undefined */}
                <p><strong>Status:</strong> {job.status}</p>
                <p><strong>Application ID:</strong> {job.id}</p> {/* Display ID for reference */}
                <div className="modal-actions"> {/* Container for buttons */}
                    <button onClick={handleEditClick} className="edit-button">Edit Application</button>
                    <button onClick={onClose} className="close-button">Close</button>
                </div>
            </div>
        </div>
    );
}

export default JobDetailsModal;