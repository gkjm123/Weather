package com.example.weather.repository;

import com.example.weather.domain.Memo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void findAllMemoTest() {
        List<Memo> memoList = jpaMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }

    @Test
    void insertMemoTest() {
        //given
        Memo newMemo = new Memo(10, "this is jpa memo");

        //when
        jpaMemoRepository.save(newMemo);

        //then
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertFalse(memoList.isEmpty());
    }

    @Test
    void findByIdTest() {
        //given
        Memo newMemo = new Memo(11, "jpa");

        //when
        Memo memo = jpaMemoRepository.save(newMemo);

        //then
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals(result.get().getText(), "jpa");
    }
}