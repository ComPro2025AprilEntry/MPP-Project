// src/components/JobForm.js
import React, { useState } from 'react';
import { addJob } from '../api/jobApi';
import { v4 as uuidv4 } from 'uuid';

function JobForm({ onJobAdded }) {
  const [formData, setFormData] = useState({
    company: '',
    position: '',
    techStack: '',
    appliedDate: '',
    deadline: '',
    status: ''
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
      e.preventDefault();
    const user = JSON.parse(localStorage.getItem('user'));
    const newJob = {
      id: uuidv4(),
      company: formData.company,
      position: formData.position,
      techStack: formData.techStack.split(',').map(s => s.trim()),
      userId: user.id,
      appliedDate: formData.appliedDate,
      deadline: formData.deadline,
      status: formData.status
    };

    await addJob(newJob);
    onJobAdded(); // Refresh the list in parent
    setFormData({
      company: '',
      position: '',
      techStack: '',
      appliedDate: '',
      deadline: '',
      status: ''
    });
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginBottom: 20 }}>
      <h2>Add Job Application</h2>
      <input name="company" value={formData.company} onChange={handleChange} placeholder="Company" required /><br />
      <input name="position" value={formData.position} onChange={handleChange} placeholder="Position" required /><br />
      <input name="techStack" value={formData.techStack} onChange={handleChange} placeholder="Tech Stack (comma-separated)" /><br />
      <input name="appliedDate" type="date" value={formData.appliedDate} onChange={handleChange} required /><br />
      <input name="deadline" type="date" value={formData.deadline} onChange={handleChange} required /><br />
      <select name="status" value={formData.status} onChange={handleChange} required>
        <option value="">Select Status</option>
        <option value="Applied">Applied</option>
        <option value="Interviewing">Interviewing</option>
        <option value="Offer">Offer</option>
        <option value="Rejected">Rejected</option>
      </select><br />
      <button type="submit">Add Job</button>
    </form>
  );
}

export default JobForm;
