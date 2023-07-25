package com.example.couphoneserver.domain.service;

import com.example.couphoneserver.domain.entity.ParentCategory;
import com.example.couphoneserver.repository.ParentCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ParentCategoryServiceTest {
    @Autowired
    private ParentCategoryService parentCategoryService;

    @Autowired
    private ParentCategoryRepository parentCategoryRepository;

    @Test
    @Rollback(value = false)
    public void 카테고리_추가() throws Exception {
//        // given
//        ParentCategory parentCategory = ParentCategory.builder()
//                .name("문화")
//                .build();
//        // when
//        Long savedId = parentCategoryService.join(parentCategory);
//        // then
//        ParentCategory findMember = parentCategoryRepository.findById(savedId)
//                .orElseThrow(() -> new IllegalArgumentException("  주어진 id 와 매칭하는 회원을 찾을 수 없습니다.  "));
//        System.out.println(savedId.toString());
//        assertEquals(parentCategory.getName(), findMember.getName());

//        문화 > 스포츠/영화
//        카페 > 일반 카페/보드게임카페/키즈카페/기타
//        식당
//        마트
//        뷰티 > 미용실/피어싱가게+렌즈/화장품가게
//        패션 > 이너웨어/일반의류/스포츠웨어/액세사리/신발

        List<ParentCategory> pcList = new ArrayList<ParentCategory>();

        ParentCategory pc1 = ParentCategory.builder().name("카페").build();
        ParentCategory pc2 = ParentCategory.builder().name("식당").build();
        ParentCategory pc3 = ParentCategory.builder().name("마트").build();
        ParentCategory pc4 = ParentCategory.builder().name("뷰티").build();
        ParentCategory pc5 = ParentCategory.builder().name("패션").build();

        pcList.add(pc1);
        pcList.add(pc2);
        pcList.add(pc3);
        pcList.add(pc4);
        pcList.add(pc5);

        parentCategoryRepository.saveAll(pcList);
    }

}
