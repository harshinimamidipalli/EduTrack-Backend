package com.edutrack.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edutrack.model.Assignment;
import com.edutrack.repository.AssignmentRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Get all assignments (used by TeacherAssignments.js)
    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // (Optional) old JSON-only endpoint – can keep if you want
    @PostMapping
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    // 🚀 NEW UPLOAD ENDPOINT – used by TeacherAssignments.js
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAssignment(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String deadline,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile pdf) {

        // At least one of description / image / pdf must exist
        boolean hasDescription = description != null && !description.trim().isEmpty();
        boolean hasImage = image != null && !image.isEmpty();
        boolean hasPdf = pdf != null && !pdf.isEmpty();

        if (!hasDescription && !hasImage && !hasPdf) {
            return ResponseEntity.badRequest().body("Please provide description or upload an image/PDF.");
        }

        // Upload directory under the project folder
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String imagePath = null;
        String pdfPath = null;

        try {
            if (hasImage) {
                String imageFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                File imageFile = new File(uploadDir + imageFileName);
                image.transferTo(imageFile);
                // stored path is relative – used by frontend
                imagePath = "uploads/" + imageFileName;
            }

            if (hasPdf) {
                String pdfFileName = UUID.randomUUID() + "_" + pdf.getOriginalFilename();
                File pdfFile = new File(uploadDir + pdfFileName);
                pdf.transferTo(pdfFile);
                pdfPath = "uploads/" + pdfFileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(deadline);
        assignment.setImagePath(imagePath);
        assignment.setPdfPath(pdfPath);

        assignmentRepository.save(assignment);

        return ResponseEntity.ok("Assignment created successfully!");
    }

    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteAssignment(@PathVariable Long id) {
        assignmentRepository.deleteById(id);
    }
}
