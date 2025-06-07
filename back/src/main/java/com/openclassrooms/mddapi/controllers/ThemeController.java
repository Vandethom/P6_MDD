package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.services.ArticleService;
import com.openclassrooms.mddapi.services.IThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/themes")
@CrossOrigin(origins = "http://localhost:4200")
public class ThemeController {

    private final IThemeService  themeService;
    private final ArticleService articleService;

    @Autowired
    public ThemeController(
        IThemeService themeService, 
        ArticleService articleService
        ) {
        this.themeService   = themeService;
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<Theme>> getAllThemes() {
        List<Theme> themes = themeService.getAllThemes();

        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theme> getThemeById(@PathVariable Long id) {
        Theme theme = themeService.getThemeById(id);

        return ResponseEntity.ok(theme);
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme createdTheme = themeService.createTheme(theme);

        return ResponseEntity.ok(createdTheme);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theme> updateTheme(@PathVariable Long id, @RequestBody Theme theme) {
        Theme updatedTheme = themeService.updateTheme(id, theme);

        return ResponseEntity.ok(updatedTheme);
    }    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);

        return ResponseEntity.noContent().build();
    }
      @GetMapping("/{id}/articles")
    public ResponseEntity<List<ArticleDTO>> getArticlesByTheme(@PathVariable Long id, @RequestParam(defaultValue = "desc") String sort) {
        try {
            List<ArticleDTO> articles = articleService.getArticlesByTheme(id, sort);
            
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
