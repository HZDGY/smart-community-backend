package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.UserRegisterRequest;
import org.sc.smartcommunitybackend.dto.response.UserRegisterResponse;
import org.sc.smartcommunitybackend.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户注册、登录等相关接口")
public class UserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "任何具有中国公民资格的人员都可以通过此接口进行注册")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "注册成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "600", description = "业务异常（如手机号已注册）")
    })
    public Result<UserRegisterResponse> register(
            @Parameter(description = "用户注册信息", required = true)
            @RequestBody @Valid UserRegisterRequest request) {
        UserRegisterResponse response = sysUserService.register(request);
        return success("注册成功", response);
    }
}

