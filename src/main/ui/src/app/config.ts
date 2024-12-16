const config = {
  baseUrl: 'http://localhost:8080',      // Default for HTTP API requests
};

// Check if running in production
if (window.location.hostname !== 'localhost') {
  config.baseUrl = 'https://liquidtory.com';      // HTTPS for API
}

export default config;