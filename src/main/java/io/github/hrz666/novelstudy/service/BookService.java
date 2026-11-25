package io.github.hrz666.novelstudy.service;


import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.resp.BookCategoryRespDto;

import java.util.List;

public interface BookService {
    /**
     * 小说分类列表查询接口
     */
    RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection);
}
