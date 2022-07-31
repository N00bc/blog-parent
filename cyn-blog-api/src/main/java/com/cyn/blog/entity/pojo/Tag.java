package com.cyn.blog.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:28
 */
@Data
public class Tag {

    private Long id;

    private String avatar;

    @TableField("tag_name")
    private String tagName;
}