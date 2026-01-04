package org.sc.smartcommunitybackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sc.smartcommunitybackend.common.Result;
import org.sc.smartcommunitybackend.dto.request.*;
import org.sc.smartcommunitybackend.dto.response.FileUploadResponse;
import org.sc.smartcommunitybackend.dto.response.UserLoginResponse;
import org.sc.smartcommunitybackend.dto.response.UserProfileResponse;
import org.sc.smartcommunitybackend.dto.response.UserRegisterResponse;
import org.sc.smartcommunitybackend.service.SysUserService;
import org.sc.smartcommunitybackend.service.VerifyCodeService;
import org.sc.smartcommunitybackend.util.FileUploadUtil;
import org.sc.smartcommunitybackend.util.JwtUtil;
import org.sc.smartcommunitybackend.util.UserContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户注册、登录等相关接口")
public class UserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VerifyCodeService verifyCodeService;

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

    @PostMapping("/login")
    @Operation(summary = "用户登录（手机号+密码）", description = "使用手机号和密码进行登录，返回用户信息和JWT令牌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "600", description = "业务异常（如用户不存在、密码错误、账号被冻结）")
    })
    public Result<UserLoginResponse> login(
            @Parameter(description = "用户登录信息", required = true)
            @RequestBody @Valid UserLoginRequest request) {
        UserLoginResponse response = sysUserService.login(request);
        return success("登录成功", response);
    }

    @PostMapping("/login-by-email")
    @Operation(summary = "邮箱验证码登录", description = "使用邮箱和验证码进行登录，返回用户信息和JWT令牌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "600", description = "业务异常（如验证码错误、邮箱未注册、账号被冻结）")
    })
    public Result<UserLoginResponse> loginByEmail(
            @Parameter(description = "邮箱验证码登录信息", required = true)
            @RequestBody @Valid EmailLoginRequest request) {
        UserLoginResponse response = sysUserService.loginByEmail(request);
        return success("登录成功", response);
    }

    @PostMapping("/avatar/upload")
    @Operation(summary = "上传头像", description = "用户上传头像图片，返回头像URL。需要在请求头中携带JWT令牌")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "上传成功"),
            @ApiResponse(responseCode = "400", description = "参数错误或文件格式不支持"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常")
    })
    public Result<FileUploadResponse> uploadAvatar(
            @Parameter(description = "头像图片文件（支持jpg、png、gif等格式，大小不超过10MB）", required = true)
            @RequestParam("file") MultipartFile file) {
        
        // 1. 从上下文获取当前登录用户ID（已通过拦截器验证）
        Long userId = UserContextUtil.getCurrentUserId();
        
        // 2. 上传文件到服务器
        String avatarUrl = fileUploadUtil.uploadFile(file, "avatar");
        
        // 3. 更新用户头像URL
        boolean updated = sysUserService.updateAvatar(userId, avatarUrl);
        if (!updated) {
            return error("头像更新失败");
        }
        
        // 4. 构建响应
        FileUploadResponse response = FileUploadResponse.builder()
                .url(avatarUrl)
                .originalFilename(file.getOriginalFilename())
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();
        
        return success("头像上传成功", response);
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）")
    })
    public Result<UserLoginResponse> getCurrentUserInfo() {
        // 从上下文获取当前登录用户ID
        Long userId = UserContextUtil.getCurrentUserId();
        
        // 查询用户信息
        var user = sysUserService.getById(userId);
        if (user == null) {
            return error("用户不存在");
        }
        
        // 构建响应
        UserLoginResponse response = UserLoginResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .age(user.getAge())
                .userType(user.getUserType())
                .status(user.getStatus())
                .build();
        
        return success("获取成功", response);
    }

    @PostMapping("/send-verify-code")
    @Operation(summary = "发送邮箱验证码", description = "发送验证码到指定邮箱，用于忘记密码等场景（60秒内只能发送一次）")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "发送成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "600", description = "业务异常（如发送过于频繁）")
    })
    public Result<Void> sendVerifyCode(
            @Parameter(description = "发送验证码请求", required = true)
            @RequestBody @Valid SendVerifyCodeRequest request) {
        verifyCodeService.sendEmailVerifyCode(request.getEmail());
        return success();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "忘记密码", description = "通过邮箱和验证码重置密码")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "重置成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "600", description = "业务异常（如验证码错误、邮箱未注册）")
    })
    public Result<Void> forgotPassword(
            @Parameter(description = "忘记密码请求", required = true)
            @RequestBody @Valid ForgotPasswordRequest request) {
        sysUserService.forgotPassword(request);
        return success();
    }

    @PostMapping("/change-password")
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码（需要验证旧密码）")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "修改成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常（如旧密码错误）")
    })
    public Result<Void> changePassword(
            @Parameter(description = "修改密码请求", required = true)
            @RequestBody @Valid ChangePasswordRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        sysUserService.changePassword(userId, request);
        return success();
    }

    @GetMapping("/profile")
    @Operation(summary = "获取个人资料", description = "获取当前登录用户的个人资料")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "获取成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）")
    })
    public Result<UserProfileResponse> getProfile() {
        Long userId = UserContextUtil.getCurrentUserId();
        UserProfileResponse profile = sysUserService.getProfile(userId);
        return success("获取成功", profile);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新个人资料", description = "更新当前登录用户的个人资料（只更新非空字段）")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "400", description = "参数错误"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）"),
            @ApiResponse(responseCode = "600", description = "业务异常")
    })
    public Result<UserProfileResponse> updateProfile(
            @Parameter(description = "更新个人资料请求", required = true)
            @RequestBody @Valid UpdateProfileRequest request) {
        Long userId = UserContextUtil.getCurrentUserId();
        UserProfileResponse profile = sysUserService.updateProfile(userId, request);
        return success("更新成功", profile);
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "用户退出登录，清除token（客户端需删除本地token）")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "退出成功"),
            @ApiResponse(responseCode = "401", description = "未授权（token无效或过期）")
    })
    public Result<Void> logout() {
        Long userId = UserContextUtil.getCurrentUserId();
        String token = UserContextUtil.getCurrentToken();
        
        // 调用退出登录服务
        sysUserService.logout(userId, token);
        
        return success("退出登录成功", null);
    }
}

