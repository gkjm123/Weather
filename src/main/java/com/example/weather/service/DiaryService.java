package com.example.weather.service;

import com.example.weather.WeatherApplication;
import com.example.weather.domain.DateWeather;
import com.example.weather.domain.Diary;
import com.example.weather.repository.DateWeatherRepository;
import com.example.weather.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {
    @Value("${openweathermap.key}")
    private String apiKey;

    final private DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDiary() {
        dateWeatherRepository.save(getWeatherFromApi());
    }

    @Transactional
    public void createDiary(LocalDate date, String text) {
        //날씨 데이터 가져오기
        DateWeather weather = getDateWeather(date);

        Diary nowdiary = Diary.builder()
                .weather(weather.getWeather())
                .icon(weather.getIcon())
                .temperature(weather.getTemperature())
                .text(text)
                .date(date).build();

        diaryRepository.save(nowdiary);
        logger.info("날씨 데이터 생성 완료");
    }

    private String getWeatherString() {
        //URL 에 API key 넣고 GET 방식으로 요청, 응답 코드 체크 후 String 반환
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

    private DateWeather getWeatherFromApi() {
        //API 에서 날씨 정보 String 으로 가져오기
        String weatherData = getWeatherString();
        Map<String, Object> parseWeather = parseWeather(weatherData);

        DateWeather dateWeather = DateWeather.builder()
                .date(LocalDate.now())
                .weather(parseWeather.get("main").toString())
                .icon(parseWeather.get("icon").toString())
                .temperature((Double)parseWeather.get("temp")).build();

        return dateWeather;
    }

    private DateWeather getDateWeather(LocalDate date) {
        //DB에 해당날짜 날씨 정보가 있으면 가져오기
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);

        //없으면 API 에 요청
        if (dateWeatherListFromDB.isEmpty()) {
            return getWeatherFromApi();
        } else {
            return dateWeatherListFromDB.get(0);
        }
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
