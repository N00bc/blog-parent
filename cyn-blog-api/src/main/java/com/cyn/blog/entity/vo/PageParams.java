package com.cyn.blog.entity.vo;

import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:33
 */
@Data
public class PageParams {
    private int page = 1;
    private int pageSize = 10;
}
