package com.edutrack.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;

import com.edutrack.model.Assignment;
import com.edutrack.model.Submission;
import com.edutrack.repository.AssignmentRepository;
import com.edutrack.repository.SubmissionRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private NotificationController notificationController;

    private static final String UPLOAD_DIR = System.getProperty("user.home") + "/EduTrackUploads/";

    // ------------------- UPLOAD SUBMISSION -------------------
    @PostMapping
    public String uploadSubmission(
            @RequestParam("file") MultipartFile file,
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam(value = "textAnswer", required = false) String textAnswer,
            @RequestParam(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            // Ensure upload directory exists
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save PDF
            String pdfFileName = null;
            if (file != null && !file.isEmpty()) {
                pdfFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path pdfPath = uploadPath.resolve(pdfFileName);
                Files.write(pdfPath, file.getBytes());
            }

            // Save text answer
            String textFileName = null;
            if (textAnswer != null && !textAnswer.isBlank()) {
                textFileName = "text_" + System.currentTimeMillis() + ".txt";
                Path textPath = uploadPath.resolve(textFileName);
                Files.writeString(textPath, textAnswer);
            }

            // Save images
            String imageNames = "";
            if (images != null && images.length > 0) {
                for (MultipartFile img : images) {
                    if (!img.isEmpty()) {
                        String imgName = "img_" + System.currentTimeMillis() + "_" + img.getOriginalFilename();
                        Path imgPath = uploadPath.resolve(imgName);
                        Files.write(imgPath, img.getBytes());
                        imageNames += imgName + ",";
                    }
                }
            }

            // Fetch assignment
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            // Create submission object
            Submission submission = new Submission(
                    "student@test.com",
                    pdfFileName,
                    assignment
            );

            submission.setTextFileName(textFileName);
            submission.setImageNames(imageNames);

            submissionRepository.save(submission);

            // Notify teacher
            notificationController.sendNotification("new-submission");

            return "✅ File uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "❌ Failed to upload file: " + e.getMessage();
        }
    }

    // ------------------- GET ALL SUBMISSIONS -------------------
    @GetMapping
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    // ------------------- DOWNLOAD FILE -------------------
    @GetMapping("/files/{fileName}")
public org.springframework.http.ResponseEntity<?> getFile(@PathVariable String fileName) {
    try {
        File file = new File(UPLOAD_DIR + fileName);

        if (!file.exists()) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            if (fileName.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (fileName.matches(".*\\.(png|jpg|jpeg|gif)$")) {
                contentType = "image/png";
            } else if (fileName.endsWith(".txt")) {
                contentType = "text/plain";
            } else {
                contentType = "application/octet-stream";
            }
        }

        org.springframework.core.io.Resource resource =
                new org.springframework.core.io.FileSystemResource(file);

        return org.springframework.http.ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header("Content-Disposition", "inline; filename=" + file.getName())
                .body(resource);

    } catch (Exception e) {
        return org.springframework.http.ResponseEntity.internalServerError().build();
    }
}


    // ------------------- UPDATE MARKS + FEEDBACK -------------------
    @PutMapping("/{id}")
    public Submission updateEvaluation(@PathVariable Long id, @RequestBody Submission updatedData) {

        Submission existing = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        existing.setMarks(updatedData.getMarks());
        existing.setFeedback(updatedData.getFeedback());

        return submissionRepository.save(existing);
    }
}

