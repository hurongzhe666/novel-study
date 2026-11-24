package io.github.hrz666.novelstudy.service.impl;

import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.resp.HomeBookRespDto;
import io.github.hrz666.novelstudy.dto.resp.HomeFriendLinkRespDto;
import io.github.hrz666.novelstudy.manager.cache.FriendLinkCacheManager;
import io.github.hrz666.novelstudy.manager.cache.HomeBookCacheManager;
import io.github.hrz666.novelstudy.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final HomeBookCacheManager homeBookCacheManager;
    private final FriendLinkCacheManager friendLinkCacheManager;


    /**
     * 首页小说推荐查询接口
     */
    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {

        return RestResp.ok(homeBookCacheManager.listHomeBooks());
    }

    /**
     * 首页友情链接列表查询接口
     */
    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return RestResp.ok(friendLinkCacheManager.listFriendLinks());
    }
}
