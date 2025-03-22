package com.saurabh.trainingmanagementsystem.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClassService{

    public List<ClassModel> getAllClasses() {
        List<ClassModel> classes = new ArrayList<>();

        // SQL query to fetch classes and the number of enrolled students
        String query = "SELECT c.class_id, c.course_id, co.course_name, c.start_time, c.end_time, " +
                "COUNT(sc.student_id) AS enrolled_students " +
                "FROM classes c " +
                "LEFT JOIN student_class sc ON c.class_id = sc.class_id " +
                "LEFT JOIN course co ON c.course_id = co.course_id " +
                "GROUP BY c.class_id, c.course_id, co.course_name, c.start_time, c.end_time;";
        try(Connection connection = DatabaseDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()){
            // Iterate through the result set and create ClassModel objects
            while (resultSet.next()) {
                int classId = resultSet.getInt("class_id");
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String startTime = resultSet.getString("start_time");
                String endTime = resultSet.getString("end_time");
                int enrolledStudents = resultSet.getInt("enrolled_students");

                // Create a ClassModel object
                ClassModel classModel = new ClassModel(classId, courseId, courseName, startTime, endTime, enrolledStudents);
                classes.add(classModel);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return classes;
    }
}