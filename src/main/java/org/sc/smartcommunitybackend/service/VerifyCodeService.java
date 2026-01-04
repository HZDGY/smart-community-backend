package org.sc.smartcommunitybackend.service;

/**
 * 验证码服务接口
 */
public interface VerifyCodeService {

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱地址
     * @return 是否发送成功
     */
    boolean sendEmailVerifyCode(String email);

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱地址
     * @param code 验证码
     * @return 是否验证成功
     */
    boolean verifyEmailCode(String email, String code);

    /**
     * 生成6位数字验证码
     *
     * @return 验证码
     */
    String generateCode();
}

