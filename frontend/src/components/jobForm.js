// frontend/src/components/jobForm.js
import React, { useState, useEffect, useMemo } from 'react'; // Import useMemo
import { addJob, updateJob } from '../api/jobApi'; // Ensure updateJob is imported
import { v4 as uuidv4 } from 'uuid';
import { toast } from 'react-toastify';

// Add jobToEdit and onJobEditComplete to props
function JobForm({ user, onJobAdd, jobToEdit, onJobEditComplete }) {
  // Use useMemo to memoize initialFormState based on jobToEdit
  const initialFormState = useMemo(() => {
    return jobToEdit ? {
      id: jobToEdit.id,
      company: jobToEdit.company || '',
      position: jobToEdit.position || '',
      // Ensure techStack is a comma-separated string for the input
      techStack: Array.isArray(jobToEdit.techStack) ? jobToEdit.techStack.join(', ') : (jobToEdit.techStack || ''),
      appliedDate: jobToEdit.appliedDate || '',
      deadline: jobToEdit.deadline || '',
      status: jobToEdit.status || 'Applied',
      userId: jobToEdit.userId // Use existing userId from jobToEdit
    } : { // Default state for adding a new job
      company: '',
      position: '',
      techStack: '',
      appliedDate: '',
      deadline: '',
      status: 'Applied',
      userId: user?.id || '' // Use user.id for new jobs
    };
  }, [jobToEdit, user]); // Dependencies for useMemo

  const [form, setForm] = useState(initialFormState);
  const [error, setError] = useState('');

  // Update form state when jobToEdit changes (entering/exiting edit mode)
  useEffect(() => {
    setForm(initialFormState);
  }, [initialFormState]); // Depend on initialFormState which is memoized

  // Ensure userId is set on form for new jobs if user loads after component mounts
  useEffect(() => {
    if (!jobToEdit && user?.id && form.userId !== user.id) {
        setForm(prevForm => ({ ...prevForm, userId: user.id }));
    }
  }, [user, jobToEdit, form.userId]);


  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!user?.id) {
      setError('User not logged in.');
      toast.error('Please log in to add/update a job.');
      return;
    }

    if (!form.company || !form.position || !form.appliedDate || !form.status) {
      setError('Company, position, applied date, and status are required.');
      toast.error('Please fill in all required fields.');
      return;
    }

    const techStackList = form.techStack.split(',').map(s => s.trim()).filter(s => s);

    try {
      if (jobToEdit) { // If jobToEdit exists, it's an update operation
        const updatedJob = { ...form, techStack: techStackList, userId: user.id }; // Ensure correct userId
        await updateJob(updatedJob, user.id); // Pass updatedJob and userId
        toast.success('Job application updated successfully!');
        onJobEditComplete(); // Notify App.js that edit is complete (will refresh lists/stats)
      } else { // Otherwise, it's an add operation
        const jobToSend = {
          ...form,
          id: uuidv4(), // Generate new ID for new jobs
          techStack: techStackList,
          appliedDate: form.appliedDate || null,
          deadline: form.deadline || null,
          userId: user.id // Ensure userId is set for new jobs
        };
        await addJob(jobToSend);
        toast.success('Job application added successfully!');
        setForm(initialFormState); // Reset form for new entry
        onJobAdd(); // Notify App.js that a new job was added
      }
    } catch (err) {
      console.error('Error adding/updating job:', err);
      setError('Failed to process job application. Please try again.');
      toast.error('Failed to process job application.');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="job-form">
      <h2>{jobToEdit ? 'Edit Application' : 'Add New Application'}</h2>
      <input
        name="company"
        placeholder="Company"
        value={form.company}
        onChange={handleChange}
        required
      /><br />
      <input
        name="position"
        placeholder="Position"
        value={form.position}
        onChange={handleChange}
        required
      /><br />
      <input
        name="techStack"
        placeholder="Tech Stack (comma-separated, e.g., React, Java, SQL)"
        value={form.techStack}
        onChange={handleChange}
      /><br />
      <label htmlFor="appliedDate">Applied Date:</label>
      <input
        type="date"
        name="appliedDate"
        value={form.appliedDate}
        onChange={handleChange}
        required
      /><br />
      <label htmlFor="deadline">Deadline:</label>
      <input
        type="date"
        name="deadline"
        value={form.deadline}
        onChange={handleChange}
      /><br />
      <select name="status" value={form.status} onChange={handleChange} required>
        <option value="Applied">Applied</option>
        <option value="Interviewing">Interviewing</option>
        <option value="Offer">Offer</option>
        <option value="Rejected">Rejected</option>
        <option value="Accepted">Accepted</option>
      </select><br />
      {error && <p className="error-message">{error}</p>}
      <button type="submit">{jobToEdit ? 'Update Job' : 'Add Job'}</button>
      {jobToEdit && ( // Show cancel button only in edit mode
        <button type="button" onClick={onJobEditComplete} className="cancel-button">
          Cancel
        </button>
      )}
    </form>
  );
}

export default JobForm;