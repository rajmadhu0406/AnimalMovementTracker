describe('Create User Form', () => {
    beforeEach(() => {
      cy.visit('/'); // Visit the app homepage
      cy.window().then((win) => {
        win.localStorage.setItem('token', 'test-token'); // Mock auth token
      });
    });
  
    it('should fill and submit the create user form successfully', () => {
      cy.visit('/create-user'); // Navigate to create user page
  
      // Fill out form fields
      cy.get('input[name="firstName"]').type('John');
      cy.get('input[name="lastName"]').type('Doe');
      cy.get('input[name="email"]').type('john.doe@example.com');
      cy.get('input[name="username"]').type('johndoe123');
      cy.get('input[name="password"]').type('Password123');
      cy.get('select[name="roleType"]').select('USER');
  
      // Submit form
      cy.get('button[type="submit"]').click();
  
      // Verify success message
      cy.contains('User created successfully.').should('be.visible');
    });
  
    it('should show an error if password is too short', () => {
      cy.visit('/create-user');
  
      // Fill out form with short password
      cy.get('input[name="firstName"]').type('Jane');
      cy.get('input[name="lastName"]').type('Doe');
      cy.get('input[name="email"]').type('jane.doe@example.com');
      cy.get('input[name="username"]').type('janedoe123');
      cy.get('input[name="password"]').type('123'); // Invalid password
      cy.get('select[name="roleType"]').select('VIEWER');
  
      // Submit form
      cy.get('button[type="submit"]').click();
  
      // Verify error message
      cy.contains('Password must be at least 8 characters long.').should('be.visible');
    });
  
    it('should show unauthorized error if user is not logged in', () => {
      cy.window().then((win) => {
        win.localStorage.removeItem('token'); // Remove auth token
      });
  
      cy.visit('/create-user');
      cy.contains('Unauthorized: Please log in to create a user.').should('be.visible');
    });
  });
  