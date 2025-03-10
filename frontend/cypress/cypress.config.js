// cypress.config.js
const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200', // adjust if your dev server is running on a different port
    viewportWidth: 1280,
    viewportHeight: 720
  },
});
