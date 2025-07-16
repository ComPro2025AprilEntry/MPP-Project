// src/api/jobApi.js
import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/jobs';

export const fetchAllJobs = (userId) => axios.get(`${API_BASE}?userId=${userId}`);
export const filterByStatus = (status) => axios.get(`${API_BASE}/status/${status}`);
export const sortByDeadline = () => axios.get(`${API_BASE}/sorted`);
export const groupByStatus = () => axios.get(`${API_BASE}/grouped`);
export const addJob = (job) => axios.post(API_BASE, job);
export const deleteJob = (id) => axios.delete(`${API_BASE}/${id}`);
