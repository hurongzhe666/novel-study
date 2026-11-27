package io.github.hrz666.novelstudy.controller.front;

import io.github.hrz666.novelstudy.core.common.resp.PageRespDto;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.core.constant.ApiRouterConsts;
import io.github.hrz666.novelstudy.dto.req.BookSearchReqDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;
import io.github.hrz666.novelstudy.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_SEARCH_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SearchController", description = "前台门户-搜索模块")
public class SearchController {

    private final SearchService searchService;

    /**
     * 小说搜索接口
     */
    @Operation(summary = "小说搜索接口")
    @GetMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(
            @ParameterObject BookSearchReqDto condition) {
        return searchService.searchBooks(condition);
    }
}
