package com.example.weather.controller;

import com.example.weather.domain.Diary;
import com.example.weather.service.DiaryService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {
    final private DiaryService diaryService;

    @Tag(name = "다이어리 생성", description = "날짜와 일기 내용을 입력해 다이어리를 생성합니다.")
    @PostMapping("/create/diary")
    void createDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "날짜 형식:YYYY-MM-DD") LocalDate date,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "일기의 내용") String text
    ) {
        diaryService.createDiary(date, text);
    }

    @Tag(name = "다이어리 읽기", description = "날짜를 입력해 해당일의 다이어리 하나를 읽어옵니다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "날짜 형식:YYYY-MM-DD") LocalDate date
    ) {
        return diaryService.readDiary(date);
    }

    @Tag(name = "다이어리 리스트 읽기", description = "날짜를 입력해 해당일의 다이어리를 모두 읽어옵니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "시작일", description = "날짜 형식:YYYY-MM-DD") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(name = "종료일", description = "날짜 형식:YYYY-MM-DD") LocalDate endDate) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @Tag(name = "다이어리 수정", description = "날짜와 일기 내용을 입력해 해당일의 첫번째 일기 내용을 수정합니다.")
    @PutMapping("/update/diary")
    void updateDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "날짜 형식:YYYY-MM-DD") LocalDate date,
                     @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수정할 내용") String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @Tag(name = "다이어리 삭제", description = "날짜를 입력해 해당일의 일기를 모두 삭제합니다.")
    @DeleteMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(description = "날짜 형식:YYYY-MM-DD") LocalDate date) {
        diaryService.deleteDiary(date);
    }

}
