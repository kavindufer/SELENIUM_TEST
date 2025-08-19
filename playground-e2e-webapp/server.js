const express = require('express');
const sqlite3 = require('sqlite3').verbose();
const session = require('express-session');
const bodyParser = require('body-parser');
const multer = require('multer');
const path = require('path');
const fs = require('fs');

const app = express();
const PORT = 3000;

// Database setup
const db = new sqlite3.Database('./playground.db');

// Initialize database
db.serialize(() => {
    // Users table
    db.run(`CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT UNIQUE,
        password TEXT,
        role TEXT,
        email TEXT
    )`);

    // Sample data table
    db.run(`CREATE TABLE IF NOT EXISTS sample_data (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT,
        email TEXT,
        department TEXT,
        salary INTEGER,
        active BOOLEAN
    )`);

    // Insert default users if not exists
    db.get("SELECT COUNT(*) as count FROM users", (err, row) => {
        if (row.count === 0) {
            db.run("INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)", 
                ['admin', 'admin123', 'admin', 'admin@example.com']);
            db.run("INSERT INTO users (username, password, role, email) VALUES (?, ?, ?, ?)", 
                ['user', 'user123', 'user', 'user@example.com']);
        }
    });

    // Insert sample data if not exists
    db.get("SELECT COUNT(*) as count FROM sample_data", (err, row) => {
        if (row.count === 0) {
            const sampleData = [
                ['John Doe', 'john@example.com', 'Engineering', 75000, 1],
                ['Jane Smith', 'jane@example.com', 'Marketing', 65000, 1],
                ['Bob Johnson', 'bob@example.com', 'Sales', 55000, 1],
                ['Alice Brown', 'alice@example.com', 'Engineering', 80000, 0],
                ['Charlie Wilson', 'charlie@example.com', 'HR', 60000, 1],
                ['Diana Prince', 'diana@example.com', 'Marketing', 70000, 1],
                ['Eva Green', 'eva@example.com', 'Sales', 58000, 1],
                ['Frank Miller', 'frank@example.com', 'Engineering', 85000, 1],
                ['Grace Lee', 'grace@example.com', 'HR', 62000, 0],
                ['Henry Ford', 'henry@example.com', 'Engineering', 90000, 1]
            ];
            
            sampleData.forEach(data => {
                db.run("INSERT INTO sample_data (name, email, department, salary, active) VALUES (?, ?, ?, ?, ?)", data);
            });
        }
    });
});

// Middleware
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(session({
    secret: 'selenium-test-secret',
    resave: false,
    saveUninitialized: true,
    cookie: { secure: false }
}));

// Static files
app.use(express.static('public'));
app.use('/uploads', express.static('uploads'));

// Create uploads directory if it doesn't exist
if (!fs.existsSync('uploads')) {
    fs.mkdirSync('uploads');
}

// Multer setup for file uploads
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'uploads/');
    },
    filename: (req, file, cb) => {
        cb(null, Date.now() + '-' + file.originalname);
    }
});
const upload = multer({ storage: storage });

// Authentication middleware
const requireAuth = (req, res, next) => {
    if (req.session.user) {
        next();
    } else {
        res.redirect('/login');
    }
};

const requireAdmin = (req, res, next) => {
    if (req.session.user && req.session.user.role === 'admin') {
        next();
    } else {
        res.status(403).send('Access denied');
    }
};

// Routes
app.get('/', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Selenium Test Playground</h1>
                <p>Welcome to the test application for Selenium E2E testing!</p>
                <nav>
                    <a href="/login">Login</a>
                    <a href="/forms/basic">Forms</a>
                    <a href="/files">Files</a>
                    <a href="/table">Tables</a>
                    <a href="/dynamic">Dynamic Content</a>
                    <a href="/modals">Modals</a>
                    <a href="/alerts">Alerts</a>
                    <a href="/dragdrop">Drag & Drop</a>
                    <a href="/iframe">iFrame</a>
                    <a href="/shadow">Shadow DOM</a>
                    <a href="/scroll">Infinite Scroll</a>
                </nav>
            </div>
        </body>
        </html>
    `);
});

// Login routes
app.get('/login', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Login - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Login</h1>
                <form method="POST" action="/login">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" id="username" name="username" data-test="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" data-test="password" required>
                    </div>
                    <button type="submit" data-test="login-btn">Login</button>
                </form>
                <p>Test users: admin/admin123, user/user123</p>
                <a href="/">Home</a>
            </div>
        </body>
        </html>
    `);
});

app.post('/login', (req, res) => {
    const { username, password } = req.body;
    
    db.get("SELECT * FROM users WHERE username = ? AND password = ?", [username, password], (err, user) => {
        if (user) {
            req.session.user = user;
            res.redirect('/profile');
        } else {
            res.send(`
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Login Failed</title>
                    <link rel="stylesheet" href="/styles.css">
                </head>
                <body>
                    <div class="container">
                        <h1>Login Failed</h1>
                        <p>Invalid username or password</p>
                        <a href="/login">Try again</a>
                    </div>
                </body>
                </html>
            `);
        }
    });
});

app.get('/logout', (req, res) => {
    req.session.destroy();
    res.redirect('/login');
});

// Profile page
app.get('/profile', requireAuth, (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Profile - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Profile</h1>
                <p>Welcome, ${req.session.user.username}!</p>
                <p data-test="role">Role: ${req.session.user.role}</p>
                <p>Email: ${req.session.user.email}</p>
                <nav>
                    <a href="/">Home</a>
                    ${req.session.user.role === 'admin' ? '<a href="/admin">Admin Panel</a>' : ''}
                    <a href="/logout">Logout</a>
                </nav>
            </div>
        </body>
        </html>
    `);
});

// Admin page
app.get('/admin', requireAdmin, (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Admin Panel - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1 data-test="admin-title">Admin Panel</h1>
                <p data-test="admin-content">This is the admin-only area.</p>
                <p>You have administrative privileges.</p>
                <nav>
                    <a href="/profile">Profile</a>
                    <a href="/">Home</a>
                    <a href="/logout">Logout</a>
                </nav>
            </div>
        </body>
        </html>
    `);
});

// Forms routes
app.get('/forms/basic', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Forms - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Basic Form</h1>
                <form method="POST" action="/forms/basic">
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" id="email" name="email" data-test="email" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" id="password" name="password" data-test="password" minlength="8" required>
                    </div>
                    <div class="form-group">
                        <label for="plan">Plan:</label>
                        <select id="plan" name="plan" data-test="plan">
                            <option value="">Select a plan</option>
                            <option value="basic">Basic</option>
                            <option value="premium">Premium</option>
                            <option value="enterprise">Enterprise</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="age">Age:</label>
                        <input type="number" id="age" name="age" data-test="age" min="18" max="100">
                    </div>
                    <button type="submit" data-test="submit">Submit</button>
                </form>
                <div id="success" data-test="success" style="display: none; color: green;"></div>
                <div id="error" data-test="error" style="display: none; color: red;"></div>
                <a href="/">Home</a>
            </div>
        </body>
        </html>
    `);
});

app.post('/forms/basic', (req, res) => {
    const { email, password, plan, age } = req.body;
    let errors = [];
    
    // Validation
    if (!email || !email.includes('@')) {
        errors.push('Invalid email address');
    }
    if (!password || password.length < 8) {
        errors.push('Password must be at least 8 characters');
    }
    if (!plan) {
        errors.push('Please select a plan');
    }
    if (age && (age < 18 || age > 100)) {
        errors.push('Age must be between 18 and 100');
    }
    
    if (errors.length > 0) {
        res.send(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>Form Error</title>
                <link rel="stylesheet" href="/styles.css">
            </head>
            <body>
                <div class="container">
                    <h1>Form Validation Error</h1>
                    <div data-test="error" style="color: red;">${errors.join(', ')}</div>
                    <a href="/forms/basic">Try again</a>
                </div>
            </body>
            </html>
        `);
    } else {
        const userRole = req.session.user ? req.session.user.role : 'guest';
        res.send(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>Form Success</title>
                <link rel="stylesheet" href="/styles.css">
            </head>
            <body>
                <div class="container">
                    <h1>Form Submitted Successfully</h1>
                    <div data-test="success" style="color: green;">Thank you ${userRole} for submitting the form!</div>
                    <p>Email: ${email}</p>
                    <p>Plan: ${plan}</p>
                    <p>Age: ${age}</p>
                    <a href="/forms/basic">Submit another</a> | <a href="/">Home</a>
                </div>
            </body>
            </html>
        `);
    }
});

// Files routes
app.get('/files', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Files - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>File Upload & Download</h1>
                
                <h2>Upload File</h2>
                <form method="POST" action="/files/upload" enctype="multipart/form-data">
                    <div class="form-group">
                        <label for="file">Choose file:</label>
                        <input type="file" id="file" name="file" data-test="file-input" required>
                    </div>
                    <button type="submit" data-test="upload-btn">Upload</button>
                </form>
                
                <h2>Download Sample Files</h2>
                <a href="/files/download/sample.txt" data-test="download-txt">Download Sample TXT</a><br>
                <a href="/files/download/sample.csv" data-test="download-csv">Download Sample CSV</a><br>
                
                <div id="upload-result" data-test="upload-result"></div>
                <a href="/">Home</a>
            </div>
        </body>
        </html>
    `);
});

app.post('/files/upload', upload.single('file'), (req, res) => {
    if (req.file) {
        res.send(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>Upload Success</title>
                <link rel="stylesheet" href="/styles.css">
            </head>
            <body>
                <div class="container">
                    <h1>File Uploaded Successfully</h1>
                    <div data-test="upload-result" style="color: green;">
                        File "${req.file.originalname}" uploaded successfully!
                    </div>
                    <p>Size: ${req.file.size} bytes</p>
                    <a href="/files">Upload another</a> | <a href="/">Home</a>
                </div>
            </body>
            </html>
        `);
    } else {
        res.send(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>Upload Failed</title>
                <link rel="stylesheet" href="/styles.css">
            </head>
            <body>
                <div class="container">
                    <h1>Upload Failed</h1>
                    <div data-test="upload-result" style="color: red;">No file was uploaded</div>
                    <a href="/files">Try again</a>
                </div>
            </body>
            </html>
        `);
    }
});

app.get('/files/download/:filename', (req, res) => {
    const filename = req.params.filename;
    
    if (filename === 'sample.txt') {
        res.setHeader('Content-disposition', 'attachment; filename=sample.txt');
        res.setHeader('Content-type', 'text/plain');
        res.send('This is a sample text file for testing downloads.');
    } else if (filename === 'sample.csv') {
        res.setHeader('Content-disposition', 'attachment; filename=sample.csv');
        res.setHeader('Content-type', 'text/csv');
        res.send('Name,Email,Department\nJohn Doe,john@example.com,Engineering\nJane Smith,jane@example.com,Marketing');
    } else {
        res.status(404).send('File not found');
    }
});

// Table routes
app.get('/table', (req, res) => {
    const page = parseInt(req.query.page) || 1;
    const limit = 5;
    const offset = (page - 1) * limit;
    const sort = req.query.sort || 'name';
    const order = req.query.order || 'ASC';
    const filter = req.query.filter || '';
    
    let query = "SELECT * FROM sample_data";
    let params = [];
    
    if (filter) {
        query += " WHERE name LIKE ? OR email LIKE ? OR department LIKE ?";
        params = [`%${filter}%`, `%${filter}%`, `%${filter}%`];
    }
    
    query += ` ORDER BY ${sort} ${order} LIMIT ? OFFSET ?`;
    params.push(limit, offset);
    
    db.all(query, params, (err, rows) => {
        db.get("SELECT COUNT(*) as total FROM sample_data" + (filter ? " WHERE name LIKE ? OR email LIKE ? OR department LIKE ?" : ""), 
               filter ? [`%${filter}%`, `%${filter}%`, `%${filter}%`] : [], (err, count) => {
            const totalPages = Math.ceil(count.total / limit);
            
            res.send(`
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Table - Selenium Test Playground</title>
                    <link rel="stylesheet" href="/styles.css">
                </head>
                <body>
                    <div class="container">
                        <h1>Data Table</h1>
                        
                        <div class="table-controls">
                            <form method="GET" style="display: inline;">
                                <input type="text" name="filter" placeholder="Filter..." value="${filter}" data-test="filter-input">
                                <button type="submit" data-test="filter-btn">Filter</button>
                            </form>
                        </div>
                        
                        <table data-test="data-table">
                            <thead>
                                <tr>
                                    <th><a href="?sort=name&order=${sort === 'name' && order === 'ASC' ? 'DESC' : 'ASC'}&filter=${filter}&page=${page}" data-test="sort-name">Name</a></th>
                                    <th><a href="?sort=email&order=${sort === 'email' && order === 'ASC' ? 'DESC' : 'ASC'}&filter=${filter}&page=${page}" data-test="sort-email">Email</a></th>
                                    <th><a href="?sort=department&order=${sort === 'department' && order === 'ASC' ? 'DESC' : 'ASC'}&filter=${filter}&page=${page}" data-test="sort-department">Department</a></th>
                                    <th><a href="?sort=salary&order=${sort === 'salary' && order === 'ASC' ? 'DESC' : 'ASC'}&filter=${filter}&page=${page}" data-test="sort-salary">Salary</a></th>
                                    <th>Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                ${rows.map(row => `
                                    <tr data-test="table-row">
                                        <td data-test="name-cell">${row.name}</td>
                                        <td data-test="email-cell">${row.email}</td>
                                        <td data-test="dept-cell">${row.department}</td>
                                        <td data-test="salary-cell">$${row.salary}</td>
                                        <td data-test="status-cell">${row.active ? 'Active' : 'Inactive'}</td>
                                    </tr>
                                `).join('')}
                            </tbody>
                        </table>
                        
                        <div class="pagination" data-test="pagination">
                            ${page > 1 ? `<a href="?page=${page - 1}&sort=${sort}&order=${order}&filter=${filter}" data-test="prev-page">Previous</a>` : ''}
                            <span data-test="page-info">Page ${page} of ${totalPages}</span>
                            ${page < totalPages ? `<a href="?page=${page + 1}&sort=${sort}&order=${order}&filter=${filter}" data-test="next-page">Next</a>` : ''}
                        </div>
                        
                        <a href="/">Home</a>
                    </div>
                </body>
                </html>
            `);
        });
    });
});

// Dynamic content routes
app.get('/dynamic', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Dynamic Content - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Dynamic Content</h1>
                <button onclick="loadContent()" data-test="load-btn">Load Dynamic Content</button>
                <button onclick="loadDelayedContent()" data-test="delayed-btn">Load Delayed Content (3s)</button>
                <div id="content" data-test="dynamic-content"></div>
                <div id="delayed-content" data-test="delayed-content"></div>
                <a href="/">Home</a>
            </div>
            <script>
                function loadContent() {
                    document.getElementById('content').innerHTML = '<p data-test="loaded-text">Dynamic content loaded!</p>';
                }
                
                function loadDelayedContent() {
                    document.getElementById('delayed-content').innerHTML = '<p>Loading...</p>';
                    setTimeout(() => {
                        document.getElementById('delayed-content').innerHTML = '<p data-test="delayed-text">Delayed content loaded after 3 seconds!</p>';
                    }, 3000);
                }
            </script>
        </body>
        </html>
    `);
});

// Modals routes
app.get('/modals', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Modals - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
            <style>
                .modal { display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.4); }
                .modal-content { background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 300px; }
                .close { color: #aaa; float: right; font-size: 28px; font-weight: bold; cursor: pointer; }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Modals</h1>
                <button onclick="openModal()" data-test="open-modal">Open Modal</button>
                
                <div id="myModal" class="modal" data-test="modal">
                    <div class="modal-content">
                        <span class="close" onclick="closeModal()" data-test="close-modal">&times;</span>
                        <p data-test="modal-text">This is a modal dialog!</p>
                        <button onclick="modalAction()" data-test="modal-action">Modal Action</button>
                    </div>
                </div>
                
                <div id="result" data-test="modal-result"></div>
                <a href="/">Home</a>
            </div>
            <script>
                function openModal() {
                    document.getElementById('myModal').style.display = 'block';
                }
                
                function closeModal() {
                    document.getElementById('myModal').style.display = 'none';
                }
                
                function modalAction() {
                    document.getElementById('result').innerHTML = '<p>Modal action performed!</p>';
                    closeModal();
                }
                
                window.onclick = function(event) {
                    const modal = document.getElementById('myModal');
                    if (event.target == modal) {
                        closeModal();
                    }
                }
            </script>
        </body>
        </html>
    `);
});

// Alerts routes
app.get('/alerts', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Alerts - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Alerts</h1>
                <button onclick="showAlert()" data-test="alert-btn">Show Alert</button>
                <button onclick="showConfirm()" data-test="confirm-btn">Show Confirm</button>
                <button onclick="showPrompt()" data-test="prompt-btn">Show Prompt</button>
                <div id="result" data-test="alert-result"></div>
                <a href="/">Home</a>
            </div>
            <script>
                function showAlert() {
                    alert('This is an alert!');
                    document.getElementById('result').innerHTML = '<p>Alert was shown</p>';
                }
                
                function showConfirm() {
                    if (confirm('Are you sure?')) {
                        document.getElementById('result').innerHTML = '<p>Confirmed!</p>';
                    } else {
                        document.getElementById('result').innerHTML = '<p>Cancelled!</p>';
                    }
                }
                
                function showPrompt() {
                    const name = prompt('What is your name?');
                    if (name) {
                        document.getElementById('result').innerHTML = '<p>Hello, ' + name + '!</p>';
                    } else {
                        document.getElementById('result').innerHTML = '<p>No name provided</p>';
                    }
                }
            </script>
        </body>
        </html>
    `);
});

// Drag & Drop routes
app.get('/dragdrop', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Drag & Drop - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
            <style>
                .drag-container { display: flex; gap: 20px; margin: 20px 0; }
                .drag-box { width: 200px; height: 200px; border: 2px dashed #ccc; padding: 10px; }
                .draggable { width: 80px; height: 80px; background: #007bff; color: white; text-align: center; line-height: 80px; cursor: move; margin: 5px; }
                .drop-zone { min-height: 150px; background: #f8f9fa; }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Drag & Drop</h1>
                <div class="drag-container">
                    <div class="drag-box">
                        <h3>Source</h3>
                        <div class="draggable" draggable="true" data-test="drag-item-1">Item 1</div>
                        <div class="draggable" draggable="true" data-test="drag-item-2">Item 2</div>
                    </div>
                    <div class="drag-box drop-zone" data-test="drop-zone">
                        <h3>Drop Zone</h3>
                    </div>
                </div>
                <div id="result" data-test="drag-result"></div>
                <a href="/">Home</a>
            </div>
            <script>
                document.querySelectorAll('.draggable').forEach(item => {
                    item.addEventListener('dragstart', function(e) {
                        e.dataTransfer.setData('text/plain', this.textContent);
                    });
                });
                
                const dropZone = document.querySelector('.drop-zone');
                dropZone.addEventListener('dragover', function(e) {
                    e.preventDefault();
                });
                
                dropZone.addEventListener('drop', function(e) {
                    e.preventDefault();
                    const data = e.dataTransfer.getData('text/plain');
                    const draggedElement = document.createElement('div');
                    draggedElement.className = 'draggable';
                    draggedElement.textContent = data;
                    draggedElement.style.background = '#28a745';
                    this.appendChild(draggedElement);
                    document.getElementById('result').innerHTML = '<p>Dropped: ' + data + '</p>';
                });
            </script>
        </body>
        </html>
    `);
});

// iFrame routes
app.get('/iframe', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>iFrame - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>iFrame Test</h1>
                <iframe src="/iframe/content" width="600" height="400" data-test="test-iframe"></iframe>
                <a href="/">Home</a>
            </div>
        </body>
        </html>
    `);
});

app.get('/iframe/content', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>iFrame Content</title>
            <style>
                body { font-family: Arial, sans-serif; padding: 20px; background: #f0f0f0; }
            </style>
        </head>
        <body>
            <h2 data-test="iframe-title">Content inside iFrame</h2>
            <p data-test="iframe-text">This content is loaded within an iframe.</p>
            <button onclick="iframeAction()" data-test="iframe-button">Click me!</button>
            <div id="iframe-result" data-test="iframe-result"></div>
            <script>
                function iframeAction() {
                    document.getElementById('iframe-result').innerHTML = '<p>Button clicked inside iframe!</p>';
                }
            </script>
        </body>
        </html>
    `);
});

// Shadow DOM routes
app.get('/shadow', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Shadow DOM - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>Shadow DOM Test</h1>
                <div id="shadow-host" data-test="shadow-host"></div>
                <div id="result" data-test="shadow-result"></div>
                <a href="/">Home</a>
            </div>
            <script>
                const host = document.getElementById('shadow-host');
                const shadow = host.attachShadow({ mode: 'open' });
                
                shadow.innerHTML = \`
                    <style>
                        .shadow-content { padding: 20px; background: #e9ecef; border: 1px solid #ccc; }
                        button { background: #007bff; color: white; padding: 10px; border: none; cursor: pointer; }
                    </style>
                    <div class="shadow-content">
                        <h3 data-test="shadow-title">Content in Shadow DOM</h3>
                        <p data-test="shadow-text">This content is inside a shadow DOM.</p>
                        <button data-test="shadow-button">Shadow Button</button>
                    </div>
                \`;
                
                shadow.querySelector('[data-test="shadow-button"]').addEventListener('click', function() {
                    document.getElementById('result').innerHTML = '<p>Shadow DOM button clicked!</p>';
                });
            </script>
        </body>
        </html>
    `);
});

// Infinite Scroll routes
app.get('/scroll', (req, res) => {
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Infinite Scroll - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
            <style>
                .scroll-item { padding: 20px; margin: 10px 0; background: #f8f9fa; border: 1px solid #ddd; }
                #loading { text-align: center; padding: 20px; display: none; }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>Infinite Scroll</h1>
                <div id="scroll-container" data-test="scroll-container">
                    ${Array.from({length: 10}, (_, i) => `<div class="scroll-item" data-test="scroll-item">Item ${i + 1}</div>`).join('')}
                </div>
                <div id="loading" data-test="loading">Loading more items...</div>
                <a href="/">Home</a>
            </div>
            <script>
                let itemCount = 10;
                let loading = false;
                
                function loadMoreItems() {
                    if (loading) return;
                    loading = true;
                    
                    document.getElementById('loading').style.display = 'block';
                    
                    setTimeout(() => {
                        const container = document.getElementById('scroll-container');
                        for (let i = 0; i < 5; i++) {
                            const item = document.createElement('div');
                            item.className = 'scroll-item';
                            item.setAttribute('data-test', 'scroll-item');
                            item.textContent = 'Item ' + (++itemCount);
                            container.appendChild(item);
                        }
                        document.getElementById('loading').style.display = 'none';
                        loading = false;
                    }, 1000);
                }
                
                window.addEventListener('scroll', function() {
                    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
                        loadMoreItems();
                    }
                });
            </script>
        </body>
        </html>
    `);
});

// CSRF protection route
app.get('/csrf', (req, res) => {
    const token = Math.random().toString(36).substring(2);
    req.session.csrfToken = token;
    
    res.send(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>CSRF Protection - Selenium Test Playground</title>
            <link rel="stylesheet" href="/styles.css">
        </head>
        <body>
            <div class="container">
                <h1>CSRF Protection Test</h1>
                <form method="POST" action="/csrf/submit">
                    <input type="hidden" name="csrf_token" value="${token}" data-test="csrf-token">
                    <div class="form-group">
                        <label for="data">Enter some data:</label>
                        <input type="text" id="data" name="data" data-test="csrf-data" required>
                    </div>
                    <button type="submit" data-test="csrf-submit">Submit with CSRF Token</button>
                </form>
                <a href="/">Home</a>
            </div>
        </body>
        </html>
    `);
});

app.post('/csrf/submit', (req, res) => {
    const { csrf_token, data } = req.body;
    
    if (csrf_token !== req.session.csrfToken) {
        res.status(403).send(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>CSRF Error</title>
                <link rel="stylesheet" href="/styles.css">
            </head>
            <body>
                <div class="container">
                    <h1>CSRF Token Mismatch</h1>
                    <p data-test="csrf-error">Invalid CSRF token</p>
                    <a href="/csrf">Try again</a>
                </div>
            </body>
            </html>
        `);
    } else {
        res.send(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>CSRF Success</title>
                <link rel="stylesheet" href="/styles.css">
            </head>
            <body>
                <div class="container">
                    <h1>CSRF Protection Passed</h1>
                    <p data-test="csrf-success">Data submitted successfully: ${data}</p>
                    <a href="/csrf">Submit more</a> | <a href="/">Home</a>
                </div>
            </body>
            </html>
        `);
    }
});

// Start server
app.listen(PORT, () => {
    console.log(`Selenium Test Playground running on http://localhost:${PORT}`);
});