package io.github.hrz666.novelstudy.service;


import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.resp.BookCategoryRespDto;
import io.github.hrz666.novelstudy.dto.resp.BookChapterAboutRespDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface BookService {
    /**
     * 小说分类列表查询接口
     */
    RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection);

    /**
     * 小说信息查询接口
     */
    RestResp<BookInfoRespDto> getBookById(Long bookId);

    /**
     * 小说访问次数统计接口
     */
    RestResp<Void> addVisitCount(Long bookId);

    /**
     * 最后一章信息查询接口
     */
    RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId);

    /**
     * 推荐小说列表查询接口
     */
    RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException;
}
