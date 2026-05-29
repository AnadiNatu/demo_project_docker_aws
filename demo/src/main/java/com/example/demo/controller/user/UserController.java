package com.example.demo.controller.user;

//import com.example.demo.dto.UserDTO;
//import com.example.demo.service.user.UserService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
//    private final UserService service;

    @GetMapping
    public String helloWorld() {
        return "Hello World";
    }
//
//    public UserController(UserService service) { this.service = service; }
//
//    @GetMapping("/{username}")
//    public ResponseEntity<UserDTO> byUsername(@PathVariable String username) {
//        return ResponseEntity.ok(service.getByUsername(username));
//    }
//
//    @GetMapping("/department/{departmentId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Page<UserDTO>> byDepartment(@PathVariable Long departmentId,
//                                                      @RequestParam(defaultValue = "0") int page,
//                                                      @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(service.listByDepartment(departmentId, PageRequest.of(page, size)));
//    }
//
//    @GetMapping("/project/{projectId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Page<UserDTO>> byProject(@PathVariable Long projectId,
//                                                   @RequestParam(defaultValue = "0") int page,
//                                                   @RequestParam(defaultValue = "10") int size) {
//        return ResponseEntity.ok(service.listByProject(projectId, PageRequest.of(page, size)));
//    }
}
