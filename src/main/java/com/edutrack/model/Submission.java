package com.edutrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentEmail;

    private String fileName;        // PDF file
    private String textFileName;    // stored as .txt file in uploads folder
    private String imageNames;      // comma-separated image filenames

    private Integer marks;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    public Submission() {}

    public Submission(String studentEmail, String fileName, Assignment assignment) {
        this.studentEmail = studentEmail;
        this.fileName = fileName;
        this.assignment = assignment;
        this.marks = null;
        this.feedback = null;
        this.textFileName = null;
        this.imageNames = null;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTextFileName() {
        return textFileName;
    }

    public void setTextFileName(String textFileName) {
        this.textFileName = textFileName;
    }

    public String getImageNames() {
        return imageNames;
    }

    public void setImageNames(String imageNames) {
        this.imageNames = imageNames;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
