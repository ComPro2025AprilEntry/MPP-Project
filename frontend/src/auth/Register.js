import React, { useState } from 'react';
import axios from 'axios';

function Register({ onRegister }) {
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState(''); // State to hold error messages

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError(''); // Clear error when user types
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); // Clear previous errors before a new submission

    // Basic client-side validation
    if (!form.name || !form.email || !form.password) {
      setError('All fields are required.');
      return;
    }
    if (form.password.length < 6) { // Example: minimum password length
      setError('Password must be at least 6 characters long.');
      return;
    }
    // Basic email format validation (can be more robust with regex)
    if (!/\S+@\S+\.\S+/.test(form.email)) {
        setError('Please enter a valid email address.');
        return;
    }


    try {
      const res = await axios.post('http://localhost:8080/api/users/register', form);
      localStorage.setItem('user', JSON.stringify(res.data));
      onRegister(res.data);
    } catch (err) {
      // More specific error handling based on backend response
      if (err.response && err.response.status === 409) { // e.g., 409 Conflict for existing user
        setError('User with this email already exists.');
      } else if (err.response && err.response.data && err.response.data.message) {
        setError(err.response.data.message); // Use message from backend if available
      } else {
        setError('Registration failed. Please try again later.');
      }
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Register</h2>
      <input
        name="name"
        placeholder="Name"
        value={form.name}
        onChange={handleChange}
        required
      /><br />
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
      <button type="submit">Register</button>
    </form>
  );
}

export default Register;