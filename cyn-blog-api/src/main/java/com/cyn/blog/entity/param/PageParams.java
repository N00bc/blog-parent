package com.cyn.blog.entity.param;

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
    private Long categoryId;
    private Long tagId;
    private String year;
    private String month;

    public String getMonth() {
        if (month != null && month.length() == 1) {
            return "0" + month;
        }
        return month;
    }
}
