package com.august.modules.system.shiro;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/11)
 * Description:设置用户名和密码（包含验证码)令牌类
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    //声明验证码参数
    private String captcha;
    //声明是否手机登录
    private boolean mobileLogin;

    /**
     * 创建无参数构造器
     */
    public UsernamePasswordToken() {
        super();
    }

    /**
     * 创建有参数构造器,初始化相应参数信息
     *
     * @param username    用户名
     * @param password    密码
     * @param rememberMe  记住我
     * @param host        远程主机 IP
     * @param captcha     验证码
     * @param mobileLogin 是否手机登录
     */
    public UsernamePasswordToken(String username, String password, boolean rememberMe, String host, String captcha, boolean mobileLogin) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = mobileLogin;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public void setMobileLogin(boolean mobileLogin) {
        this.mobileLogin = mobileLogin;
    }
}
