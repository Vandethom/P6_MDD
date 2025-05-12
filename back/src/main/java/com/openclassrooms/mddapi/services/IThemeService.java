package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Theme;
import java.util.List;

public interface IThemeService {
    List<Theme> getAllThemes();
    Theme       getThemeById(Long id);
    Theme       createTheme(Theme theme);
    Theme       updateTheme(Long id, Theme theme);
    void        deleteTheme(Long id);
}
