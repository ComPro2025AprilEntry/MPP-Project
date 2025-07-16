import React, { useState } from 'react';
import axios from 'axios';

function Login({ onLogin }) {
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState(''); // State to hold error messages

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError(''); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); // Clear previous errors before a new submission

    // Basic client-side validation
    if (!form.email || !form.password) {
      setError('Please enter both email and password.');
      return;
    }

    try {
      const res = await axios.post('http://localhost:8080/api/users/login', form);
      if (res.data) {
        localStorage.setItem('user', JSON.stringify(res.data));
        onLogin(res.data);
      } else {
        // This case might happen if the backend sends an empty success response
        // but typically a proper error status would be sent.
        setError('Login failed. Please check your credentials.');
      }
    } catch (err) {
      // More specific error handling based on backend response
      if (err.response && err.response.status === 401) {
        setError('Invalid email or password.');
      } else if (err.response && err.response.data && err.response.data.message) {
        setError(err.response.data.message); // Use message from backend if available
      } else {
        setError('Login failed. Please try again later.');
      }
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>
      <input
        name="email"
        placeholder="Email"
        value={form.email}
        onChange={handleChange}
        required
      /><br />
      <input
        type="password"
        name="password"
        placeholder="Password"
        value={form.password}
        onChange={handleChange}
        required
      /><br />
      {error && <p className="error-message">{error}</p>}{/* Display error message */}
      <button type="submit">Login</button>
    </form>
  );
}

export default Login;