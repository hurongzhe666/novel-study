package io.github.hrz666.novelstudy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.core.constant.DatabaseConsts;
import io.github.hrz666.novelstudy.dao.entity.BookChapter;
import io.github.hrz666.novelstudy.dao.mapper.BookChapterMapper;
import io.github.hrz666.novelstudy.dao.mapper.BookInfoMapper;
import io.github.hrz666.novelstudy.dto.resp.BookCategoryRespDto;
import io.github.hrz666.novelstudy.dto.resp.BookChapterAboutRespDto;
import io.github.hrz666.novelstudy.dto.resp.BookChapterRespDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;
import io.github.hrz666.novelstudy.manager.cache.BookCategoryCacheManager;
import io.github.hrz666.novelstudy.manager.cache.BookChapterCacheManager;
import io.github.hrz666.novelstudy.manager.cache.BookContentCacheManager;
import io.github.hrz666.novelstudy.manager.cache.BookInfoCacheManager;
import io.github.hrz666.novelstudy.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookCategoryCacheManager bookCategoryCacheManager;
    private final BookInfoCacheManager bookInfoCacheManager;
    private final BookInfoMapper bookInfoMapper;
    private final BookChapterCacheManager bookChapterCacheManager;
    private final BookContentCacheManager bookContentCacheManager;
    private final BookChapterMapper bookChapterMapper;


    // 推荐小说数量
    private static final Integer REC_BOOK_COUNT = 4;

    /**
     * 小说分类列表查询接口
     */
    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheManager.listCategory(workDirection));
    }

    /**
     * 小说信息查询接口
     */
    @Override
    public RestResp<BookInfoRespDto> getBookById(Long bookId) {
        return RestResp.ok(bookInfoCacheManager.getBookInfo(bookId));
    }

    /**
     * 小说访问次数统计接口
     */
    @Override
    public RestResp<Void> addVisitCount(Long bookId) {
        bookInfoMapper.addVisitCount(bookId);
        return RestResp.ok();
    }

    /**
     * 最后一章信息查询接口
     */
    @Override
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId) {
        //先根据传进来的id查询到小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);

        //再根据小说信息查询到最新一章信息
        BookChapterRespDto bookChapterRespDto = bookChapterCacheManager.getChapter(bookInfo.getLastChapterId());

        //获取到章节内容
        String content = bookContentCacheManager.getBookContent(bookChapterRespDto.getId());

        // 查询章节总数
        QueryWrapper<BookChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId);
        Long chapterTotal = bookChapterMapper.selectCount(chapterQueryWrapper);


        return RestResp.ok(BookChapterAboutRespDto.builder()
                .chapterInfo(bookChapterRespDto)
                .chapterTotal(chapterTotal)
                .contentSummary(content.substring(0, 30))
                .build());
    }

    /**
     * 推荐小说列表查询接口
     */
    @Override
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId)
            throws NoSuchAlgorithmException {
        //通过小说id查询出小说信息,获取小说类别
        Long categoryId = bookInfoCacheManager.getBookInfo(bookId).getCategoryId();
        //通过小说类别查询出该类别下最新更新的500本小说的id
        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(categoryId);

        // 检查列表是否为空或不足
        if (CollectionUtils.isEmpty(lastUpdateIdList)) {
            return RestResp.ok(Collections.emptyList());
        }

        // 排除当前书籍，同时确保有足够的推荐书籍用于展示
        List<Long> candidateIdList = lastUpdateIdList.stream()
                .filter(id -> !Objects.equals(id, bookId))
                .toList();

        if (candidateIdList.isEmpty()) {
            return RestResp.ok(Collections.emptyList());
        }

        // 确定实际推荐的书籍数量最大为4，最小为现有数量
        int actualRecCount = Math.min(REC_BOOK_COUNT, candidateIdList.size());

        // 随机推荐4本小说，反复随机抽下标，抽到没抽过的就取那本书，抽够 4 本就停。
        List<BookInfoRespDto> respDtoList = new ArrayList<>();  // 最终要返回的结果（挑中的书）
        Set<Integer> recIdIndexSet = new HashSet<>();  // 记录已抽中的下标
        Random rand = SecureRandom.getInstanceStrong();  // 随机数生成器

        // 使用 Set 提高查找效率，同时修复bug防止无限循环
        while (respDtoList.size() < actualRecCount && recIdIndexSet.size() < candidateIdList.size()) {
            // 随机抽取一个下标
            int recIdIndex = rand.nextInt(candidateIdList.size());
            // 判断是否出现过，没有出现过则添加到结果列表中
            if (!recIdIndexSet.contains(recIdIndex)) {
                // 添加到已抽中下标集合中
                recIdIndexSet.add(recIdIndex);
                // 获取抽中的小说id
                Long recBookId = candidateIdList.get(recIdIndex);
                // 获取抽中的小说信息
                BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(recBookId);
                // 添加到结果列表中
                respDtoList.add(bookInfo);
            }
        }

        return RestResp.ok(respDtoList);
    }


}
