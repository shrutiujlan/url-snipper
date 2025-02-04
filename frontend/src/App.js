import React, { useState } from 'react';
import axios from 'axios';

function App() {
  const [longUrl, setLongUrl] = useState('');
  const [shortUrl, setShortUrl] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Function to handle the form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');  // Clear error state
    setLoading(true);  // Set loading state

    try {
      const response = await axios.post('http://localhost:8080/api/v1/', {
        url: longUrl
      });

      console.log(response.data);  // Update the shortUrl state with the response data
      setShortUrl(response.data.shortUrl);
      setLongUrl('');  // Clear the input field
    } catch (err) {
      if (err.response && err.response.status === 400) {
        setError('Invalid URL. Please enter a valid one.');
      } else {
        setError('An error occurred. Please try again.');
      }
    } finally {
      setLoading(false);  // Set loading state to false
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="p-6 max-w-md w-full bg-white rounded-xl shadow-lg space-y-6">
        <h2 className="text-2xl font-bold text-center">URL Shortener</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:border-blue-300"
            placeholder="Enter the long URL"
            value={longUrl}
            onChange={(e) => setLongUrl(e.target.value)}
            required
          />
          
          {loading ? (
            <button className="w-full p-3 bg-blue-600 text-white rounded-lg" disabled>
              Shortening...
            </button>
          ) : (
            <button type="submit" className="w-full p-3 bg-blue-600 text-white rounded-lg">
              Shorten URL
            </button>
          )}
        </form>

        {error && <p className="text-red-600">{error}</p>}
        
        {shortUrl && (
          <div className="mt-4 text-center">
            <p>Shortened URL:</p>
            <a
              href={shortUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="text-blue-600"
            >
              {shortUrl}
            </a>
            <button
              className="ml-2 text-sm bg-gray-200 p-1 rounded"
              onClick={() => navigator.clipboard.writeText(shortUrl)}
            >
              Copy
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
