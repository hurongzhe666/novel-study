package io.github.hrz666.novelstudy.service;


import io.github.hrz666.novelstudy.core.common.resp.PageRespDto;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.req.BookSearchReqDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;

public interface SearchService {
    /**
     * 搜索书籍
     *
     * @param condition 搜索条件
     * @return 书籍列表
     */
    RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition);
}
