package com.cyn.blog.entity.pojo;

import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/29 6:59
 */
@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}
