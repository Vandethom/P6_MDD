package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.security.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService implements IThemeService {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    @Override
    public Theme getThemeById(Long id) {
        return themeRepository.findById(id)
                .orElseThrow(() -> new AuthException("Thème non trouvé"));
    }

    @Override
    public Theme createTheme(Theme theme) {
        return themeRepository.save(theme);
    }

    @Override
    public Theme updateTheme(Long id, Theme updatedTheme) {
        Theme existingTheme = getThemeById(id);
        
        if (updatedTheme.getTitle() != null) {
            existingTheme.setTitle(updatedTheme.getTitle());
        }
        if (updatedTheme.getDescription() != null) {
            existingTheme.setDescription(updatedTheme.getDescription());
        }
        
        return themeRepository.save(existingTheme);
    }

    @Override
    public void deleteTheme(Long id) {
        getThemeById(id); // Vérifier si le thème existe
        themeRepository.deleteById(id);
    }
}
