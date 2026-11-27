package io.github.hrz666.novelstudy.service.impl;
import io.github.hrz666.novelstudy.core.common.resp.PageRespDto;
import io.github.hrz666.novelstudy.core.common.resp.RestResp;
import io.github.hrz666.novelstudy.dto.req.BookSearchReqDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;
import io.github.hrz666.novelstudy.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")

public class ESSearchServiceImpl implements SearchService {
    // TODO: Implement the searchBooks method
    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> searchBooks(BookSearchReqDto condition) {
        return null;
    }
}
