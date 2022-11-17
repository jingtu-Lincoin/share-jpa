package net.ahzz.share.sharejpa.query;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 封装查询条件
 */
public class SpringQuery {
    /**
     * 查询条件
     */
    public Example example;
    /**
     * 排序
     */
    public Sort sort;
    /**
     * 分页
     */
    public Pageable pageable;
}
