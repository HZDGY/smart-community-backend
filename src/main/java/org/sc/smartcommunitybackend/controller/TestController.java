package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sc.smartcommunitybackend.common.Result;
import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器（Swagger示例）
 */
@RestController
@RequestMapping("/test")
@Tag(name = "测试接口", description = "用于测试和演示Swagger文档的接口")
public class TestController extends BaseController {

    @GetMapping("/hello")
    @Operation(summary = "Hello接口", description = "返回Hello World消息")
    public Result<String> hello() {
        return success("Hello World!");
    }

    @GetMapping("/echo/{message}")
    @Operation(summary = "回显接口", description = "返回传入的消息")
    public Result<String> echo(
            @Parameter(description = "要回显的消息", required = true)
            @PathVariable String message) {
        return success("Echo: " + message);
    }

    @PostMapping("/info")
    @Operation(summary = "信息接口", description = "接收并返回信息")
    public Result<String> info(
            @Parameter(description = "信息内容", required = true)
            @RequestParam String info) {
        return success("Received: " + info);
    }
}

