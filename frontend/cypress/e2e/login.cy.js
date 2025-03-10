describe('Login Page E2E Tests', () => {
    // Before each test, visit the login page
    beforeEach(() => {
      // If you've configured the baseUrl in cypress.config.js:
      cy.visit('/login');
  
      // If you have NOT set a baseUrl in cypress.config.js, then:
      // cy.visit('http://localhost:4200/login');
    });
  
    it('should display the login form', () => {
      // Check if the login form elements are visible
      cy.get('h2').contains('Login').should('be.visible');
      cy.get('#username').should('be.visible');
      cy.get('#password').should('be.visible');
      cy.contains('button', 'Login').should('be.visible');
    });
  
    it('should log in successfully with valid credentials', () => {
      // Optionally intercept the login call if you want to mock the response
      cy.intercept('POST', 'http://localhost:8080/api/auth/login', {
        statusCode: 200,
        body: {
          token: 'mock-jwt-token' // The backend token you'd expect.
        }
      }).as('loginRequest');
  
      // Type in valid credentials
      cy.get('#username').type('validUser');
      cy.get('#password').type('validPassword');
  
      // Click the login button
      cy.contains('button', 'Login').click();
  
      // If you are intercepting, wait for the request to finish
      cy.wait('@loginRequest');
  
      // Now that login is successful, you may check that you have navigated to
      // the home page or you can check some known element that only appears after login
      // For example, let's assume you navigate to '/' and show a welcome message:
      cy.url().should('eq', 'http://localhost:4200/'); // or eq(baseUrl + '/')
      // You can also check local storage for the token if your AuthService sets it
      cy.window().then((window) => {
        const token = window.localStorage.getItem('token');
        expect(token).to.equal('mock-jwt-token');
      });
    });
  
    it('should show an error message with invalid credentials', () => {
      // Intercept to simulate a 401 or 400 error
      cy.intercept('POST', 'http://localhost:8080/api/auth/login', {
        statusCode: 401,
        body: {
          message: 'Invalid username or password'
        }
      }).as('loginRequest');
  
      // Type in invalid credentials
      cy.get('#username').type('invalidUser');
      cy.get('#password').type('invalidPassword');
  
      // Click the login button
      cy.contains('button', 'Login').click();
  
      // Wait for the request
      cy.wait('@loginRequest');
  
      // Check for the error message
      cy.get('.alert.alert-danger').contains('Invalid username or password');
      // Ensure the user remains on the login page
      cy.url().should('include', '/login');
    });
  
    it('should require username and password fields', () => {
      // Attempt to login without entering credentials
      cy.contains('button', 'Login').click();
  
      // Since the form requires them, you could check for Angular's form validation,
      // or check for some error. Alternatively, if your form simply tries to login,
      // you'd see an error message. Adjust accordingly:
  
      // Checking if the "Login failed. Please try again." message shows
      // or the page remains the same:
      cy.url().should('include', '/login');
    });
  });
  