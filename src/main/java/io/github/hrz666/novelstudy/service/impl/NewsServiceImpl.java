package io.github.hrz666.novelstudy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.core.constant.DatabaseConsts;
import io.github.hrz666.novelstudy.dao.entity.NewsContent;
import io.github.hrz666.novelstudy.dao.entity.NewsInfo;
import io.github.hrz666.novelstudy.dao.mapper.NewsContentMapper;
import io.github.hrz666.novelstudy.dao.mapper.NewsInfoMapper;
import io.github.hrz666.novelstudy.dto.resp.NewsInfoRespDto;
import io.github.hrz666.novelstudy.manager.cache.NewsCacheManager;
import io.github.hrz666.novelstudy.service.NewsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsInfoMapper newsInfoMapper;
    private final NewsCacheManager newsCacheManager;
    private final NewsContentMapper newsContentMapper;


    /**
     * 新闻信息查询接口
     */
    @Override
    public RestResp<NewsInfoRespDto> getNews(Long id) {
        //1.先根据前端传来的id查询相应的新闻信息
        NewsInfo newsInfo = newsInfoMapper.selectById(id);

        //2.根据新闻id查询相应的新闻内容
        QueryWrapper<NewsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.NewsContentTable.COLUMN_NEWS_ID, id)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        NewsContent newsContent = newsContentMapper.selectOne(queryWrapper);

        //3.将新闻信息和新闻内容封装到NewsInfoRespDto中
        return RestResp.ok(NewsInfoRespDto.builder()
                .title(newsInfo.getTitle())
                .sourceName(newsInfo.getSourceName())
                .updateTime(newsInfo.getUpdateTime())
                .content(newsContent.getContent())
                .build());
    }

    /**
     * 最新新闻列表查询接口
     */
    @Override
    public RestResp<List<NewsInfoRespDto>> listLatestNews() {
        return RestResp.ok(newsCacheManager.listLatestNews());
    }
}
