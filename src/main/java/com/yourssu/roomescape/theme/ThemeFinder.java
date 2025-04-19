package com.yourssu.roomescape.theme;

import org.springframework.stereotype.Component;

import static com.yourssu.roomescape.exception.ExceptionMessage.NO_EXIST_THEME;

@Component
public class ThemeFinder {

    private final ThemeRepository themeRepository;

    public ThemeFinder(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme findTheme(Long themeId){
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException(NO_EXIST_THEME.getMessage()));
    }
}
