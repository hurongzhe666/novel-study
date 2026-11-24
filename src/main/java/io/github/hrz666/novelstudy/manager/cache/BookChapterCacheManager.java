package io.github.hrz666.novelstudy.manager.cache;

import io.github.hrz666.novelstudy.core.constant.CacheConsts;
import io.github.hrz666.novelstudy.dao.entity.BookChapter;
import io.github.hrz666.novelstudy.dao.mapper.BookChapterMapper;
import io.github.hrz666.novelstudy.dto.resp.BookChapterRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 小说章节 缓存管理类
 *
 * @author hrz666
 * @date 2026/11/18
 */
@Component
@RequiredArgsConstructor
public class BookChapterCacheManager {

    private final BookChapterMapper bookChapterMapper;

    /**
     * 查询小说章节信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public BookChapterRespDto getChapter(Long chapterId) {
        BookChapter bookChapter = bookChapterMapper.selectById(chapterId);
        return BookChapterRespDto.builder()
            .id(chapterId)
            .bookId(bookChapter.getBookId())
            .chapterNum(bookChapter.getChapterNum())
            .chapterName(bookChapter.getChapterName())
            .chapterWordCount(bookChapter.getWordCount())
            .chapterUpdateTime(bookChapter.getUpdateTime())
            .isVip(bookChapter.getIsVip())
            .build();
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public void evictBookChapterCache(Long chapterId) {
        // 调用此方法自动清除小说章节信息的缓存
    }

}
