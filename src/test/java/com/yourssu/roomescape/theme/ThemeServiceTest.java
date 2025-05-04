package com.yourssu.roomescape.theme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    private Theme testTheme1;
    private Theme testTheme2;

    @BeforeEach
    void setUp() {
        testTheme1 = new Theme(1L, "공포 테마", "무서운 공포 테마입니다.");
        testTheme2 = new Theme(2L, "추리 테마", "추리 게임 테마입니다.");
    }

    @Test
    @DisplayName("테마 생성 성공")
    void createTheme_ShouldSaveAndReturnTheme() {
        // Given
        Theme newTheme = new Theme("새 테마", "새로운 테마 설명입니다.");
        when(themeRepository.save(any(Theme.class))).thenReturn(new Theme(3L, "새 테마", "새로운 테마 설명입니다."));

        // When
        Theme result = themeService.createTheme(newTheme);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("새 테마");
        assertThat(result.getDescription()).isEqualTo("새로운 테마 설명입니다.");
        verify(themeRepository, times(1)).save(newTheme);
    }

    @Test
    @DisplayName("모든 테마 조회 성공")
    void getAllThemes_ShouldReturnAllThemes() {
        // Given
        List<Theme> themes = Arrays.asList(testTheme1, testTheme2);
        when(themeRepository.findAll()).thenReturn(themes);

        // When
        List<Theme> result = themeService.getAllThemes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testTheme1, testTheme2);
        verify(themeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("테마 삭제 성공")
    void deleteTheme_ShouldCallRepositoryDeleteById() {
        // Given
        Long themeId = 1L;
        doNothing().when(themeRepository).deleteById(themeId);

        // When
        themeService.deleteTheme(themeId);

        // Then
        verify(themeRepository, times(1)).deleteById(themeId);
    }
}
