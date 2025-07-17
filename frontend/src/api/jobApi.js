// frontend/src/api/jobApi.js

import axios from 'axios';

// Update API_URL to match your backend's base mapping
const API_URL = 'http://localhost:8080/api/jobs'; // ⭐⭐ IMPORTANT: Ensure this matches your JobController @RequestMapping("/api/jobs") ⭐⭐
const AUTH_URL = 'http://localhost:8080/api/auth';

const getAuthHeaders = (userId) => {
    return {
        'X-User-Id': userId, // ⭐⭐ Use X-User-Id as per your JobController @RequestHeader ⭐⭐
    };
};

export const registerUser = async (userData) => {
    return axios.post(`${AUTH_URL}/register`, userData);
};

export const loginUser = async (credentials) => {
    return axios.post(`${AUTH_URL}/login`, credentials);
};

export const fetchAllJobs = async (userId) => {
    // Now uses a query parameter for userId
    return axios.get(API_URL, { params: { userId }, headers: getAuthHeaders(userId) });
};

export const addJob = async (jobData) => { // userId is now part of jobData
    return axios.post(API_URL, jobData); // userId should be set in jobData already
};

export const updateJob = async (id, jobData, userId) => {
    return axios.put(`${API_URL}/${id}`, jobData, { headers: getAuthHeaders(userId) }); // Send userId in header for update
};

export const deleteJob = async (id, userId) => {
    return axios.delete(`${API_URL}/${id}`, { headers: getAuthHeaders(userId) });
};

export const filterJobsByTechStack = async (userId, techStack) => {
    return axios.get(`${API_URL}/filter`, {
        params: { userId, techStack }, // Send as query parameters
        headers: getAuthHeaders(userId)
    });
};

export const sortJobsByDeadline = async (userId) => {
    return axios.get(`${API_URL}/sorted-by-deadline`, {
        params: { userId }, // Send as query parameter
        headers: getAuthHeaders(userId)
    });
};

// ⭐⭐ ADD/CONFIRM THIS FUNCTION IN jobApi.js ⭐⭐
export const filterJobsByStatus = async (userId, status) => {
    try {
        const response = await axios.get(`${API_URL}/filter`, { // Calls the /filter endpoint
            params: { userId, status }, // Sends status as a query parameter
            headers: getAuthHeaders(userId)
        });
        return response;
    } catch (error) {
        console.error('Error filtering jobs by status:', error);
        throw error;
    }
};

export const getJobStats = async (userId) => {
    return axios.get(`${API_URL}/stats`, {
        params: { userId }, // Send as query parameter
        headers: getAuthHeaders(userId)
    });
};