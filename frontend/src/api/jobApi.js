// src/api/jobApi.js
import axios from 'axios';

// Ensure this matches your backend URL.
// During development, it's 'http://localhost:8080/api'.
// For deployment, it will be your deployed backend URL (e.g., from Vercel env variable).
// Using process.env.REACT_APP_API_BASE_URL is best practice for deployment.
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Helper for authenticated requests (e.g., for userId in headers)
const getConfig = (userId) => {
    return userId ? { headers: { 'X-User-Id': userId } } : {};
};

// Job Application Endpoints
export const fetchAllJobs = (userId) => api.get(`/jobs?userId=${userId}`);
export const addJob = (job) => api.post('/jobs', job);

// Corrected deleteJob to send userId in header as required by backend
export const deleteJob = (id, userId) => api.delete(`/jobs/${id}`, getConfig(userId));

// NEW: Update Job Function - Now Exported!
// Corresponds to PUT /api/jobs/{id}
export const updateJob = (job, userId) => {
    // Backend expects id in path, job object in body, and userId in header
    return api.put(`/jobs/${job.id}`, job, getConfig(userId));
};

export const getJobById = (id, userId) => {
    return api.get(`/jobs/${id}`, getConfig(userId));
};

// Filtering, Sorting, and Stats Endpoints
export const filterJobsByTechStack = (userId, techStack) => {
    return api.get(`/jobs/filter`, { params: { userId, techStack } });
};

export const filterJobsByStatus = (userId, status) => {
    return api.get(`/jobs/filter`, { params: { userId, status } });
};

export const sortJobsByDeadline = (userId) => {
    return api.get(`/jobs/sorted-by-deadline`, { params: { userId } });
};

export const getJobStats = (userId) => {
    return api.get(`/jobs/stats`, { params: { userId } });
};
