package com.cyn.blog.utils;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author G0dc
 * @description:
 * @date 2022/8/2 14:41
 */
public class BeanCopy<T, S> {
    public List<T> BeanCopyList(List<S> sList) {

    }

    public T BeanCopyOne(S s) {
        BeanUtil.copyToList();
    }
}
