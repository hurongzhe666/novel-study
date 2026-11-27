package io.github.hrz666.novelstudy.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.hrz666.novelstudy.core.annotation.ValidateSortOrder;
import io.github.hrz666.novelstudy.dao.entity.BookInfo;
import io.github.hrz666.novelstudy.dto.req.BookSearchReqDto;
import io.github.hrz666.novelstudy.dto.resp.BookInfoRespDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 小说信息 Mapper 接口
 * </p>
 *
 * @author hrz666
 * @date 2026/11/18
 */
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    /**
     * 增加小说点击量
     *
     * @param bookId 小说ID
     */
    void addVisitCount(@Param("bookId") Long bookId);

    /**
     * 小说搜索，根据条件搜索小说
     * @param page mybatis-plus 分页对象
     * @param condition 搜索条件
     * @return 返回结果
     * */

    // @ValidateSortOrder白名单注解
    List<BookInfo> searchBooks(IPage<BookInfoRespDto> page,
                               @ValidateSortOrder @Param("condition") BookSearchReqDto condition);


}
