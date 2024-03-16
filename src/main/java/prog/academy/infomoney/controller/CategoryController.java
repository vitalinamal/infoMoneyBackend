package prog.academy.infomoney.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.academy.infomoney.dto.request.CategoryRequest;
import prog.academy.infomoney.dto.response.CategoryResponse;
import prog.academy.infomoney.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/protected/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequest category) {
        categoryService.createCategory(category);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesByProfile(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findAllCategoriesByProfile(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest category) {
        categoryService.updateCategory(id, category);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
