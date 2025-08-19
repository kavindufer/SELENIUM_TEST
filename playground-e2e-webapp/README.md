# Selenium Test Playground Web Application

A comprehensive web application designed specifically for demonstrating and testing Selenium E2E test scenarios. This application provides all the features and functionality that the Selenium test suite expects.

## Features

This web application includes the following features that align with the Selenium test scenarios:

### 🔐 Authentication & Authorization
- **Login System**: User authentication with different roles (admin/user)
- **Role-based Access**: Admin-only pages and user-specific content
- **Session Management**: Secure session handling

### 📝 Forms & Validation
- **Form Validation**: Client and server-side validation
- **Multiple Input Types**: Text, email, password, select dropdowns, numbers
- **Error Handling**: Comprehensive error messages and success states

### 📁 File Operations
- **File Upload**: Upload files with validation and feedback
- **File Download**: Download sample files (TXT, CSV)
- **File Management**: Secure file handling with proper storage

### 📊 Data Tables
- **Sorting**: Click column headers to sort data
- **Filtering**: Search through table data
- **Pagination**: Navigate through large datasets
- **Dynamic Content**: Real-time table updates

### 🎭 Interactive Elements
- **Modals**: Pop-up dialogs with actions
- **Alerts**: JavaScript alerts, confirms, and prompts
- **Dynamic Content**: Load content with and without delays
- **Drag & Drop**: Interactive drag-and-drop functionality

### 🔧 Advanced Features
- **iFrames**: Embedded content testing
- **Shadow DOM**: Web Components with shadow DOM
- **Infinite Scroll**: Auto-loading content on scroll
- **CSRF Protection**: Security token validation

## Quick Start

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn

### Installation

1. **Install dependencies**:
   ```bash
   cd playground-e2e-webapp
   npm install
   ```

2. **Start the application**:
   ```bash
   npm start
   ```

3. **Access the application**:
   Open your browser and navigate to `http://localhost:3000`

### Development Mode

For development with auto-restart:
```bash
npm run dev
```

## Test Users

The application comes with pre-configured test users:

| Username | Password  | Role  |
|----------|-----------|-------|
| admin    | admin123  | admin |
| user     | user123   | user  |

## Application Structure

```
playground-e2e-webapp/
├── server.js           # Main Express.js server
├── package.json        # Dependencies and scripts
├── playground.db       # SQLite database (auto-created)
├── public/            
│   └── styles.css     # Application styles
├── uploads/           # File upload directory (auto-created)
└── README.md          # This file
```

## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /logout` - Logout user
- `GET /profile` - User profile (requires auth)
- `GET /admin` - Admin panel (requires admin role)

### Forms
- `GET /forms/basic` - Basic form page
- `POST /forms/basic` - Process form submission

### Files
- `GET /files` - File upload/download page
- `POST /files/upload` - Handle file upload
- `GET /files/download/:filename` - Download files

### Data Tables
- `GET /table` - Data table with sorting, filtering, pagination

### Interactive Features
- `GET /dynamic` - Dynamic content loading
- `GET /modals` - Modal dialogs
- `GET /alerts` - JavaScript alerts
- `GET /dragdrop` - Drag & drop interface
- `GET /iframe` - iFrame content
- `GET /shadow` - Shadow DOM example
- `GET /scroll` - Infinite scroll
- `GET /csrf` - CSRF protection demo

## Database

The application uses SQLite for data persistence with the following tables:

### Users Table
- `id` - Primary key
- `username` - Unique username
- `password` - User password (plain text for testing)
- `role` - User role (admin/user)
- `email` - User email

### Sample Data Table
- `id` - Primary key
- `name` - Employee name
- `email` - Employee email
- `department` - Department name
- `salary` - Employee salary
- `active` - Employment status

## Running Selenium Tests

To run the Selenium tests against this application:

1. **Start the web application**:
   ```bash
   cd playground-e2e-webapp
   npm start
   ```

2. **Run Selenium tests** (in another terminal):
   ```bash
   cd ../playground-e2e
   mvn test
   ```

The tests are configured to run against `http://localhost:3000` by default.

## Configuration

The application can be configured by modifying the following:

### Port Configuration
Change the port in `server.js`:
```javascript
const PORT = 3000; // Change this value
```

### Database Location
The SQLite database is created as `playground.db` in the application root. To change this, modify the database connection in `server.js`:
```javascript
const db = new sqlite3.Database('./your-database-name.db');
```

## Features for Selenium Testing

This application specifically includes elements and behaviors that are commonly tested in Selenium E2E tests:

- **Data Test Attributes**: All interactive elements include `data-test` attributes for reliable element selection
- **Various Input Types**: Text, email, password, file, select, number inputs
- **Dynamic Content**: Elements that appear/disappear based on user actions
- **Timing Scenarios**: Delayed content loading for testing waits
- **Authentication Flows**: Login/logout with session management
- **File Operations**: Upload and download functionality
- **Table Operations**: Sorting, filtering, pagination
- **Modal Interactions**: Pop-up dialogs and overlays
- **Alert Handling**: JavaScript alerts, confirms, and prompts
- **Drag & Drop**: Interactive elements for testing mouse actions
- **iFrame Content**: Embedded content requiring frame switching
- **Shadow DOM**: Web Components for advanced DOM testing
- **CSRF Protection**: Security tokens for form submission testing

## Troubleshooting

### Port Already in Use
If port 3000 is already in use, change the PORT variable in `server.js` or kill the process using the port:
```bash
# Find process using port 3000
lsof -i :3000

# Kill process (replace PID with actual process ID)
kill -9 PID
```

### Database Issues
If you encounter database issues, delete the `playground.db` file and restart the application. The database will be recreated with sample data.

### File Upload Issues
Ensure the `uploads` directory has proper write permissions:
```bash
chmod 755 uploads
```

## Contributing

This application is designed specifically for Selenium testing scenarios. When adding new features:

1. Include appropriate `data-test` attributes
2. Follow the existing styling patterns
3. Update this README with new endpoints
4. Ensure compatibility with Selenium WebDriver

## License

MIT License - This application is designed for testing purposes.