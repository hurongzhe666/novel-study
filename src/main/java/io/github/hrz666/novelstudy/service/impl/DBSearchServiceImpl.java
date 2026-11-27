package io.github.hrz666.novelstudy.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.hrz666.novelstudy.core.common.resp.PageRespDto;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dao.entity.BookInfo;
import io.github.hrz666.novelstudy.dao.mapper.BookInfoMapper;
import io.github.hrz666.novelstudy.dto.req.BookSearchReqDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;
import io.github.hrz666.novelstudy.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "false")

public class DBSearchServiceImpl implements SearchService {

    private final BookInfoMapper bookInfoMapper;


    /**
     * 根据条件搜索书籍
     *
     * @param condition 搜索条件
     * @return 书籍列表
     */
    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition) {
        Page<BookInfoRespDto> page = new Page<>();
        page.setCurrent(condition.getPageNum());
        page.setSize(condition.getPageSize());

        //自己写的xml方法，这里MybatisConfig配置了Mapper扫描，扫描到Ipage方法，传page是为了让拦截器拦截，然后
        //后台自动将page信息写成分页sql加入总sql语句，而xml里面不会去写分页sql，不使用page数据
        List<BookInfo> bookInfos = bookInfoMapper.searchBooks(page, condition);
        return RestResp.ok(
                //of是PageRespDto的静态方法，用于将分页数据封装成PageRespDto
                PageRespDto.of(condition.getPageNum(), condition.getPageSize(), page.getTotal(),
                        //(long pageNum, long pageSize, long total, List<T> list)
                        bookInfos.stream().map(v -> BookInfoRespDto.builder()
                                .id(v.getId())
                                .bookName(v.getBookName())
                                .categoryId(v.getCategoryId())
                                .categoryName(v.getCategoryName())
                                .authorId(v.getAuthorId())
                                .authorName(v.getAuthorName())
                                .wordCount(v.getWordCount())
                                .lastChapterName(v.getLastChapterName())
                                .build()).toList()));
    }

}
