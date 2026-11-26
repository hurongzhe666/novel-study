package io.github.hrz666.novelstudy.service;


import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.resp.*;

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

    /**
     * 小说章节列表查询接口
     */
    RestResp<List<BookChapterRespDto>> listChapters(Long bookId);

    /**
     * 小说内容查询接口
     */
    RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId);


    /**
     * 获取上一章ID
     */
    RestResp<Long> getPreChapterId(Long chapterId);

    /**
     * 获取下一章ID
     */
    RestResp<Long> getNextChapterId(Long chapterId);

    /**
     * 获取小说访问排名列表
     */
    RestResp<List<BookRankRespDto>> listVisitRankBooks();


    /**
     * 获取小说最新上传列表
     */
    RestResp<List<BookRankRespDto>> listNewestRankBooks();


    /**
     * 获取小说更新排名列表
     */
    RestResp<List<BookRankRespDto>> listUpdateRankBooks();
}
