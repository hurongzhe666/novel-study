package io.github.hrz666.novelstudy.service.impl;

import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.resp.BookCategoryRespDto;
import io.github.hrz666.novelstudy.manager.cache.BookCategoryCacheManager;
import io.github.hrz666.novelstudy.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookCategoryCacheManager bookCategoryCacheManager;

    /**
     * 小说分类列表查询接口
     */
    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheManager.listCategory(workDirection));
    }
}
