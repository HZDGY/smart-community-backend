package org.sc.smartcommunitybackend.constant;

/**
 * Redis键名常量类
 * 统一管理系统中所有Redis键名，避免硬编码和重复定义
 */
public class RedisKeyConstant {

    // ============================ 用户相关 ============================
    /**
     * 用户信息缓存键
     * 格式：user:info:{userId}
     */
    public static final String USER_INFO = "user:info:%s";

    /**
     * 用户登录令牌键
     * 格式：user:token:{token}
     */
    public static final String USER_TOKEN = "user:token:%s";

    /**
     * 用户登录次数限制键
     * 格式：user:login:limit:{username}
     */
    public static final String USER_LOGIN_LIMIT = "user:login:limit:%s";

    // ============================ 商品相关 ============================
    /**
     * 商品大类缓存键
     */
    public static final String PRODUCT = "product";
    /**
     * 商品信息缓存键
     * 格式：product:info:{productId}
     */
    public static final String PRODUCT_INFO = "product:info:%s";

    /**
     * 商品分类缓存键
     * 格式：product:category:{categoryId}
     */
    public static final String PRODUCT_CATEGORY = "product:category:%s";

    /**
     * 商品列表缓存键
     * 格式：product:list:{page}:{size}
     */
    public static final String PRODUCT_LIST = "product:list:%s:%s";

    /**
     * 商品库存缓存键
     * 格式：product:stock:{productId}
     */
    public static final String PRODUCT_STOCK = "product:stock:%s";

    // ============================ 促销相关 ============================
    /**
     * 促销活动缓存键
     * 格式：promotion:info:{promotionId}
     */
    public static final String PROMOTION_INFO = "promotion:info:%s";

    /**
     * 促销商品列表缓存键
     * 格式：promotion:product:list:{type}
     */
    public static final String PROMOTION_PRODUCT_LIST = "promotion:product:list:%s";

    // ============================ 门店相关 ============================
    /**
     * 门店信息缓存键
     * 格式：store:info:{storeId}
     */
    public static final String STORE_INFO = "store:info:%s";

    /**
     * 门店列表缓存键
     * 格式：store:list:{areaId}
     */
    public static final String STORE_LIST = "store:list:%s";

    // ============================ 验证码相关 ============================
    /**
     * 短信验证码键
     * 格式：sms:code:{phone}
     */
    public static final String SMS_CODE = "sms:code:%s";

    /**
     * 邮箱验证码键
     * 格式：email:code:{email}
     */
    public static final String EMAIL_CODE = "email:code:%s";

    /**
     * 验证码发送限制键
     * 格式：code:send:limit:{target}
     */
    public static final String CODE_SEND_LIMIT = "code:send:limit:%s";

    // ============================ 社区服务相关 ============================
    /**
     * 报事维修列表缓存键
     * 格式：repair:list:{userId}:{status}
     */
    public static final String REPAIR_LIST = "repair:list:%s:%s";

    /**
     * 投诉建议列表缓存键
     * 格式：complaint:list:{userId}:{status}
     */
    public static final String COMPLAINT_LIST = "complaint:list:%s:%s";

    /**
     * 访客登记列表缓存键
     * 格式：visitor:list:{userId}
     */
    public static final String VISITOR_LIST = "visitor:list:%s";

    // ============================ 统计相关 ============================
    /**
     * 今日访问量统计键
     */
    public static final String STATISTICS_DAILY_VISITS = "statistics:daily:visits";

    /**
     * 商品销量统计键
     * 格式：statistics:product:sales:{productId}
     */
    public static final String STATISTICS_PRODUCT_SALES = "statistics:product:sales:%s";

    // ============================ 分布式锁 ============================
    /**
     * 商品库存更新锁
     * 格式：lock:product:stock:{productId}
     */
    public static final String LOCK_PRODUCT_STOCK = "lock:product:stock:%s";

    /**
     * 订单创建锁
     * 格式：lock:order:create:{userId}
     */
    public static final String LOCK_ORDER_CREATE = "lock:order:create:%s";

    // ============================ 过期时间相关 ============================
    /**
     * 用户信息缓存过期时间（秒）
     */
    public static final Integer USER_INFO_EXPIRE_SECONDS = 3600 * 24; // 24小时

    /**
     * 登录令牌过期时间（秒）
     */
    public static final Integer USER_TOKEN_EXPIRE_SECONDS = 3600 * 24 * 7; // 7天

    /**
     * 验证码过期时间（秒）
     */
    public static final Integer CODE_EXPIRE_SECONDS = 300; // 5分钟

    /**
     * 商品信息缓存过期时间（秒）
     */
    public static final Integer PRODUCT_INFO_EXPIRE_SECONDS = 3600; // 1小时

    /**
     * 缓存过期时间 - 默认（秒）
     */
    public static final Integer DEFAULT_EXPIRE_SECONDS = 3600; // 1小时

    /**
     * 缓存过期时间 - 短期（秒）
     */
    public static final Integer SHORT_EXPIRE_SECONDS = 600; // 10分钟

    /**
     * 缓存过期时间 - 长期（秒）
     */
    public static final Integer LONG_EXPIRE_SECONDS = 3600 * 24 * 30; // 30天
}