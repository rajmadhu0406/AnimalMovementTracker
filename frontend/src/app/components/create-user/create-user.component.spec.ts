import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CreateUserComponent } from './create-user.component';
import { ApiService } from '../../services/api.service';
import { ErrorService } from '../../services/error.service';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';

describe('CreateUserComponent', () => {
  let component: CreateUserComponent;
  let fixture: ComponentFixture<CreateUserComponent>;
  let apiServiceMock: any;
  let errorServiceMock: any;

  beforeEach(async () => {
    apiServiceMock = {
      request: jasmine.createSpy('request')
    };
    errorServiceMock = {
      setErrorMessage: jasmine.createSpy('setErrorMessage')
    };

    await TestBed.configureTestingModule({
      // Because CreateUserComponent is standalone, put it in 'imports'
      imports: [FormsModule, CreateUserComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceMock },
        { provide: ErrorService, useValue: errorServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CreateUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    // Clear spy calls so each test has a fresh slate
    apiServiceMock.request.calls.reset();
    errorServiceMock.setErrorMessage.calls.reset();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty user form', () => {
    expect(component.newUser).toEqual({
      firstName: '',
      lastName: '',
      email: '',
      username: '',
      password: '',
      role: { roleType: 'USER' }
    });
  });

  it('should require auth token to create user', async () => {
    component.authToken = null;
    await component.createUser(); // triggers "Unauthorized..." error
    expect(errorServiceMock.setErrorMessage)
      .toHaveBeenCalledWith('Unauthorized: Please log in to create a user.');
  });

  it('should validate password length', async () => {
    component.authToken = 'test-token';
    component.newUser.password = 'short'; // < 8 chars
    await component.createUser(); // triggers "Password too short" error
    expect(errorServiceMock.setErrorMessage)
      .toHaveBeenCalledWith('Password must be at least 8 characters long.');
  });

  it('should successfully create user', async () => {
    component.authToken = 'test-token';
    const userForRequest = {
      firstName: 'John',
      lastName: 'Doe',
      email: 'john@example.com',
      username: 'johndoe',
      password: 'password123',
      role: { roleType: 'USER' }
    };
    component.newUser = userForRequest;

    apiServiceMock.request.and.returnValue(of({}));

    await component.createUser(); // triggers actual API call

    // Expect the correct user data to have been sent
    expect(apiServiceMock.request).toHaveBeenCalledWith(
      'POST',
      'users',
      userForRequest,
      'test-token'
    );

    // The success path sets a success message and empties out newUser
    expect(errorServiceMock.setErrorMessage).toHaveBeenCalledWith('User created successfully.');
    expect(component.newUser).toEqual(component.getEmptyUser());
  });

  
});
