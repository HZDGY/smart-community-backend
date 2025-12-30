package org.sc.smartcommunitybackend.controller;

import org.sc.smartcommunitybackend.common.Result;

/**
 * 基础控制器类
 */
public class BaseController {

    /**
     * 返回成功结果
     */
    protected <T> Result<T> success() {
        return Result.success();
    }

    /**
     * 返回成功结果（带数据）
     */
    protected <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /**
     * 返回成功结果（带消息和数据）
     */
    protected <T> Result<T> success(String message, T data) {
        return Result.success(message, data);
    }

    /**
     * 返回失败结果
     */
    protected <T> Result<T> error(String message) {
        return Result.error(message);
    }
}

