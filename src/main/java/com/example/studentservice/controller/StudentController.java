package com.example.studentservice.controller;

import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")  // ✅ AJOUT DE /api
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * GET /api/students - Récupère tous les étudiants
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    /**
     * POST /api/students - Crée un nouvel étudiant
     */
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        try {
            student.setId(null);
            Student savedStudent = studentRepository.save(student);
            System.out.println("✅ Étudiant créé : " + savedStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/students/{id} - Récupère un étudiant par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        System.out.println("🔍 Recherche de l'étudiant avec ID: " + id);  // Debug
        return studentRepository.findById(id)
                .map(student -> {
                    System.out.println("✅ Étudiant trouvé : " + student);
                    return ResponseEntity.ok(student);
                })
                .orElseGet(() -> {
                    System.out.println("❌ Étudiant non trouvé avec ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * PUT /api/students/{id} - Met à jour un étudiant existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setFirstName(student.getFirstName());
                    existingStudent.setLastName(student.getLastName());
                    existingStudent.setEmail(student.getEmail());
                    existingStudent.setUniversityId(student.getUniversityId());
                    Student updated = studentRepository.save(existingStudent);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/students/{id} - Supprime un étudiant
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/students/search?name=xxx - Recherche par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchByName(@RequestParam String name) {
        List<Student> students = studentRepository.findByFirstNameContaining(name);
        return ResponseEntity.ok(students);
    }

    /**
     * GET /api/students/university/{universityId} - Récupère les étudiants d'une université
     */
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<Student>> getByUniversityId(@PathVariable Long universityId) {
        List<Student> students = studentRepository.findByUniversityId(universityId);
        return ResponseEntity.ok(students);
    }
}