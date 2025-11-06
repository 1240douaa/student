package com.example.studentservice.controller;

import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*") // Pour éviter les problèmes CORS si nécessaire
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * GET /students - Récupère tous les étudiants
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    /**
     * POST /students - Crée un nouvel étudiant
     */
    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        try {
            // S'assurer que l'ID est null pour une nouvelle création
            student.setId(null);
            
            // Sauvegarder l'étudiant
            Student savedStudent = studentRepository.save(student);
            
            // Log pour débogage
            System.out.println("✅ Étudiant créé : " + savedStudent);
            
            // Retourner 201 CREATED avec le corps de la réponse
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /students/{id} - Récupère un étudiant par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /students/{id} - Met à jour un étudiant existant
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
     * DELETE /students/{id} - Supprime un étudiant
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
     * GET /students/search?name=xxx - Recherche par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchByName(@RequestParam String name) {
        List<Student> students = studentRepository.findByFirstNameContaining(name);
        return ResponseEntity.ok(students);
    }

    /**
     * GET /students/university/{universityId} - Récupère les étudiants d'une université
     */
    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<Student>> getByUniversityId(@PathVariable Long universityId) {
        List<Student> students = studentRepository.findByUniversityId(universityId);
        return ResponseEntity.ok(students);
    }
}