const config = {
  baseUrl: 'http://localhost:8080',      // Default for HTTP API requests
};

// Check if running in production
if (window.location.hostname !== 'localhost') {
  config.baseUrl = 'http://44.196.216.74';      // HTTPS for API
}

export default config;