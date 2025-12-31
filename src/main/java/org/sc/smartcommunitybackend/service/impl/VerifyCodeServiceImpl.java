package org.sc.smartcommunitybackend.service.impl;

import org.sc.smartcommunitybackend.exception.BusinessException;
import org.sc.smartcommunitybackend.service.VerifyCodeService;
import org.sc.smartcommunitybackend.util.EmailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码服务实现类
 * 使用内存存储验证码（生产环境应使用Redis）
 */
@Service
public class VerifyCodeServiceImpl implements VerifyCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerifyCodeServiceImpl.class);

    @Autowired
    private EmailUtil emailUtil;

    /**
     * 验证码存储（邮箱 -> 验证码信息）
     * 生产环境应使用Redis，并设置过期时间
     */
    private final Map<String, CodeInfo> codeStore = new ConcurrentHashMap<>();

    /**
     * 验证码有效期（毫秒）- 5分钟
     */
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 同一邮箱发送间隔（毫秒）- 60秒
     */
    private static final long SEND_INTERVAL = 60 * 1000;

    @Override
    public boolean sendEmailVerifyCode(String email) {
        // 1. 检查发送频率
        CodeInfo existingCode = codeStore.get(email);
        if (existingCode != null) {
            long timeSinceLastSend = System.currentTimeMillis() - existingCode.getSendTime();
            if (timeSinceLastSend < SEND_INTERVAL) {
                long remainingSeconds = (SEND_INTERVAL - timeSinceLastSend) / 1000;
                throw new BusinessException("发送过于频繁，请" + remainingSeconds + "秒后再试");
            }
        }

        // 2. 生成验证码
        String code = generateCode();

        // 3. 发送邮件
        boolean sent = emailUtil.sendVerifyCode(email, code);
        if (!sent) {
            throw new BusinessException("验证码发送失败");
        }

        // 4. 存储验证码
        codeStore.put(email, new CodeInfo(code, System.currentTimeMillis()));
        
        logger.info("验证码已发送到邮箱: {}", email);
        return true;
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        // 1. 获取存储的验证码
        CodeInfo codeInfo = codeStore.get(email);
        if (codeInfo == null) {
            logger.warn("验证码不存在或已过期: {}", email);
            return false;
        }

        // 2. 检查是否过期
        long currentTime = System.currentTimeMillis();
        if (currentTime - codeInfo.getSendTime() > CODE_EXPIRE_TIME) {
            codeStore.remove(email);
            logger.warn("验证码已过期: {}", email);
            return false;
        }

        // 3. 验证码比对
        if (code.equals(codeInfo.getCode())) {
            // 验证成功后删除验证码（一次性使用）
            codeStore.remove(email);
            logger.info("验证码验证成功: {}", email);
            return true;
        }

        logger.warn("验证码错误: {}", email);
        return false;
    }

    @Override
    public String generateCode() {
        // 生成6位随机数字验证码
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 验证码信息内部类
     */
    private static class CodeInfo {
        private final String code;
        private final long sendTime;

        public CodeInfo(String code, long sendTime) {
            this.code = code;
            this.sendTime = sendTime;
        }

        public String getCode() {
            return code;
        }

        public long getSendTime() {
            return sendTime;
        }
    }
}

