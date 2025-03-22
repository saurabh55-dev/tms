
ERRORS
-View CLASSES   --solved



PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS login(username TEXT NOT NULL PRIMARY KEY, password TEXT DEFAULT NULL);

CREATE TABLE IF NOT EXISTS course (course_id INTEGER PRIMARY KEY AUTOINCREMENT, course_name TEXT DEFAULT NULL, duration TEXT DEFAULT NULL, fees INTEGER DEFAULT NULL);

CREATE TABLE IF NOT EXISTS classes (class_id INTEGER PRIMARY KEY AUTOINCREMENT, course_id INTEGER DEFAULT NULL, start_time TEXT DEFAULT NULL, end_time TEXT DEFAULT NULL, FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS student (student_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT DEFAULT NULL, phone TEXT DEFAULT NULL, address TEXT DEFAULT NULL, email TEXT DEFAULT NULL);

CREATE TABLE IF NOT EXISTS student_course (student_id INTEGER NOT NULL, course_id INTEGER NOT NULL, PRIMARY KEY (student_id, course_id), FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS student_class (student_id INTEGER NOT NULL, class_id INTEGER NOT NULL, PRIMARY KEY (student_id, class_id), FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (class_id) REFERENCES classes (class_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS attendance (attendance_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER DEFAULT NULL, date TEXT DEFAULT NULL, is_present INTEGER DEFAULT 0, class_id INTEGER DEFAULT NULL, FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (class_id) REFERENCES classes (class_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS payment (payment_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER DEFAULT NULL, course_id INTEGER DEFAULT NULL, payment_amount INTEGER DEFAULT NULL, payment_method TEXT DEFAULT NULL, payment_date TEXT DEFAULT NULL, FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS payment_due (student_id INTEGER NOT NULL PRIMARY KEY, due_amount INTEGER DEFAULT NULL, FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS income (id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, amount REAL NOT NULL, remarks TEXT, date TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS expense (id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, amount REAL NOT NULL, purpose TEXT NOT NULL, remarks TEXT, date TEXT NOT NULL);