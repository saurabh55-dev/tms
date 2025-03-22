package com.saurabh.trainingmanagementsystem.Models;

import java.io.File;
import java.sql.*;

public class DatabaseDriver {

    public static final String DBPATH = System.getProperty("user.home") + "/Management_System/Database/system.db";
    private static final String URL = "jdbc:sqlite:" + DBPATH;

    public static final String SCHEMA = "PRAGMA foreign_keys = ON;";
    private static final String LOGIN = "CREATE TABLE IF NOT EXISTS login (username TEXT NOT NULL PRIMARY KEY, password TEXT DEFAULT NULL);";
    public static final String COURSE = "CREATE TABLE IF NOT EXISTS course (course_id INTEGER PRIMARY KEY AUTOINCREMENT, course_name TEXT DEFAULT NULL, duration TEXT DEFAULT NULL, fees INTEGER DEFAULT NULL);";
    public static final String CLASSES = "CREATE TABLE IF NOT EXISTS classes (class_id INTEGER PRIMARY KEY AUTOINCREMENT, course_id INTEGER DEFAULT NULL, start_time TEXT DEFAULT NULL, end_time TEXT DEFAULT NULL, FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE);";
    public static final String STUDENT = "CREATE TABLE IF NOT EXISTS student (student_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT DEFAULT NULL, phone TEXT DEFAULT NULL, address TEXT DEFAULT NULL, email TEXT DEFAULT NULL);";
    public static final String STUDENT_COURSE = "CREATE TABLE IF NOT EXISTS student_course (student_id INTEGER NOT NULL, course_id INTEGER NOT NULL, PRIMARY KEY (student_id, course_id), FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE);";
    public static final String STUDENT_CLASS = "CREATE TABLE IF NOT EXISTS student_class (student_id INTEGER NOT NULL, class_id INTEGER NOT NULL, PRIMARY KEY (student_id, class_id), FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (class_id) REFERENCES classes (class_id) ON DELETE CASCADE);";
    public static final String ATTENDANCE = "CREATE TABLE IF NOT EXISTS attendance (attendance_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER DEFAULT NULL, date TEXT DEFAULT NULL, is_present INTEGER DEFAULT 0, class_id INTEGER DEFAULT NULL, FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (class_id) REFERENCES classes (class_id) ON DELETE CASCADE);";
    public static final String PAYMENT = "CREATE TABLE IF NOT EXISTS payment (payment_id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER DEFAULT NULL, course_id INTEGER DEFAULT NULL, payment_amount INTEGER DEFAULT NULL, payment_method TEXT DEFAULT NULL, payment_date TEXT DEFAULT NULL, FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE, FOREIGN KEY (course_id) REFERENCES course (course_id) ON DELETE CASCADE);";
    public static final String PAYMENT_DUE = "CREATE TABLE IF NOT EXISTS payment_due (student_id INTEGER NOT NULL PRIMARY KEY, due_amount INTEGER DEFAULT NULL, FOREIGN KEY (student_id) REFERENCES student (student_id) ON DELETE CASCADE);";
    public static final String INCOME = "CREATE TABLE IF NOT EXISTS income (id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, amount REAL NOT NULL, remarks TEXT, date TEXT NOT NULL);";
    public static final String EXPENSE = "CREATE TABLE IF NOT EXISTS expense (id INTEGER PRIMARY KEY AUTOINCREMENT, topic TEXT NOT NULL, amount REAL NOT NULL, purpose TEXT NOT NULL, remarks TEXT, date TEXT NOT NULL);";

    public static final String CHECK_DATA = "SELECT username FROM login WHERE username = 'admin';";
    public static final String DATA = "INSERT INTO login (username, password) VALUES (?, ?);";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        initDbPath();
        try (var connection = getConnection();
             var statement = connection.createStatement()) {
            statement.execute(SCHEMA);
            statement.execute(LOGIN);
            statement.execute(COURSE);
            statement.execute(CLASSES);
            statement.execute(STUDENT);
            statement.execute(STUDENT_COURSE);
            statement.execute(STUDENT_CLASS);
            statement.execute(ATTENDANCE);
            statement.execute(PAYMENT);
            statement.execute(PAYMENT_DUE);
            statement.execute(INCOME);
            statement.execute(EXPENSE);
            System.out.println("Database initialized Successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertDefaultAdmin() {
        try (var connection = getConnection();
             var pstmt = connection.prepareStatement(CHECK_DATA);
             var pstmt2 = connection.prepareStatement(DATA)) {
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                pstmt2.setString(1, "admin");
                pstmt2.setString(2, "admin");
                pstmt2.executeUpdate();
                System.out.println("Default Admin Inserted");
            } else {
                System.out.println("Default Admin Already Exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDbPath() {
        try {
            // Ensure directory exists
            File dbFile = new File(DBPATH);
            File parentDir = dbFile.getParentFile(); // Get parent directory

            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs(); // Create directories if they don't exist
                if (created) {
                    System.out.println("Database directory created successfully: " + parentDir.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
