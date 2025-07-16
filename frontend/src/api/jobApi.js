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

// Original job operations, adjusted to use the common base URL
export const fetchAllJobs = (userId) => api.get(`/jobs?userId=${userId}`);
export const addJob = (job) => api.post('/jobs', job);
export const deleteJob = (id) => api.delete(`/jobs/${id}`); // Correct, matches backend

// --- New and Updated functions for user stories ---

// 1. Filter jobs by tech stack
// Corresponds to GET /api/jobs/filter?userId={userId}&techStack={techStack}
export const filterJobsByTechStack = (userId, techStack) => {
  return api.get(`/jobs/filter`, { params: { userId, techStack } });
};

// 2. Sort applications by deadline
// Corresponds to GET /api/jobs/sorted-by-deadline?userId={userId}
export const sortJobsByDeadline = (userId) => {
  return api.get(`/jobs/sorted-by-deadline`, { params: { userId } });
};

// 3. Get grouped stats
// Corresponds to GET /api/jobs/stats?userId={userId}
export const getJobStats = (userId) => {
  return api.get(`/jobs/stats`, { params: { userId } });
};

// --- Removed/Obsolete functions from your old file ---
// The following functions are removed because their corresponding backend endpoints
// no longer exist or have been replaced by the new, more specific endpoints.
// export const filterByStatus = (status) => axios.get(`${API_BASE}/status/${status}`);
// export const sortByDeadline = () => axios.get(`${API_BASE}/sorted`); // Replaced by sortJobsByDeadline
// export const groupByStatus = () => axios.get(`${API_BASE}/grouped`); // Replaced by getJobStats