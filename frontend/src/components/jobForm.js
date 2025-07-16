// frontend/src/components/JobForm.js
import React, { useState, useEffect } from 'react';
import { addJob } from '../api/jobApi';
import { v4 as uuidv4 } from 'uuid';
import { toast } from 'react-toastify'; // Import toast

function JobForm({ user, onJobAdd }) {
  const initialFormState = {
    company: '',
    position: '',
    techStack: '',
    appliedDate: '',
    deadline: '',
    status: 'Applied',
    userId: user?.id || ''
  };
  const [form, setForm] = useState(initialFormState);
  const [error, setError] = useState('');

  useEffect(() => {
    setForm(prevForm => ({ ...prevForm, userId: user?.id || '' }));
  }, [user]);

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!user?.id) {
      setError('User not logged in.');
      toast.error('Please log in to add a job.'); // Use toast for error
      return;
    }

    if (!form.company || !form.position || !form.appliedDate || !form.status) {
      setError('Company, position, applied date, and status are required.');
      toast.error('Please fill in all required fields.'); // Use toast for error
      return;
    }

    const techStackList = form.techStack.split(',').map(s => s.trim()).filter(s => s);

    try {
      const jobToSend = {
        ...form,
        id: uuidv4(),
        techStack: techStackList,
        appliedDate: form.appliedDate || null,
        deadline: form.deadline || null,
        userId: user.id
      };
      await addJob(jobToSend);
      toast.success('Job application added successfully!'); // Use toast for success
      setForm(initialFormState);
      onJobAdd();
    } catch (err) {
      console.error('Error adding job:', err);
      setError('Failed to add job application. Please try again.');
      toast.error('Failed to add job application.'); // Use toast for error
    }
  };

  return (
    <form onSubmit={handleSubmit} className="job-form">
      <h2>Add New Application</h2>
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
      {error && <p className="error-message">{error}</p>} {/* Keep in-component error for form validation */}
      <button type="submit">Add Job</button>
    </form>
  );
}

export default JobForm;