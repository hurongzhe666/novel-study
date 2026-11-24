package io.github.hrz666.novelstudy.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.hrz666.novelstudy.core.constant.CacheConsts;
import io.github.hrz666.novelstudy.core.constant.DatabaseConsts;
import io.github.hrz666.novelstudy.dao.entity.BookCategory;
import io.github.hrz666.novelstudy.dao.mapper.BookCategoryMapper;
import io.github.hrz666.novelstudy.dto.resp.BookCategoryRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 小说分类 缓存管理类
 *
 * @author hrz666
 * @date 2026/11/18
 */
@Component
@RequiredArgsConstructor
public class BookCategoryCacheManager {

    private final BookCategoryMapper bookCategoryMapper;

    /**
     * 根据作品方向查询小说分类列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.BOOK_CATEGORY_LIST_CACHE_NAME)
    public List<BookCategoryRespDto> listCategory(Integer workDirection) {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCategoryTable.COLUMN_WORK_DIRECTION, workDirection);
        return bookCategoryMapper.selectList(queryWrapper).stream().map(v ->
            BookCategoryRespDto.builder()
                .id(v.getId())
                .name(v.getName())
                .build()).toList();
    }

}
