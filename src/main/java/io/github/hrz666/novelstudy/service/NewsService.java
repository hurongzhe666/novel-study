package io.github.hrz666.novelstudy.service;


import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.resp.NewsInfoRespDto;

import java.util.List;

public interface NewsService {

    /**
     * 最新新闻列表查询接口
     */
    RestResp<List<NewsInfoRespDto>> listLatestNews();

    /**
     * 新闻信息查询接口
     */
    RestResp<NewsInfoRespDto> getNews(Long id);
}
