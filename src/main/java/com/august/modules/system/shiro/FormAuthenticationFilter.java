package com.august.modules.system.shiro;

import com.august.common.utils.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common
 * Author: August
 * Update: August(2016/4/8)
 * Description:表单验证（包含验证码）过滤类
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
    //初始化定义默认的验证码参数
    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    //初始化定义默认的手机登录参数
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    //初始化定义默认的信息参数
    public static final String DEFAULT_MESSAGE_PARAM = "message";

    //初始化验证码参数信息
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    //初始化是否手机登录参数信息
    private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
    //初始化消息参数信息
    private String messageParam = DEFAULT_MESSAGE_PARAM;

    /**
     * 根据请求参数创建令牌
     *
     * @param request  请求参数
     * @param response 响应信息
     * @return 创建后的令牌信息
     */
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        //获取用户名
        String username = getUsername(request);
        //获取密码
        String password = getPassword(request);
        //如果密码为空，则设置为空字符串
        if (password == null) password = "";

        //获取是否记住我选项
        boolean rememberMe = isRememberMe(request);
        //从请求信息中获取主机信息
        String host = StringUtils.getRemoteAddress((HttpServletRequest) request);
        //获取请求信息中的验证码信息
        String captcha = getCaptcha(request);
        //是否是手机登录
        boolean mobile = isMobileLogin(request);
        //创建一个新的用户名密码令牌
        return new UsernamePasswordToken(username, password, rememberMe, host, captcha, mobile);
    }

    /**
     * 获取是否手机登录
     *
     * @param request 请求参数
     * @return 返回 验证结果
     */
    private boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public String getMobileLoginParam() {
        return mobileLoginParam;
    }

    public String getMessageParam() {
        return messageParam;
    }

    /**
     * 获取请求信息中的验证码信息
     *
     * @param request 请求信息
     * @return 返回验证码信息
     */
    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    /**
     * 重写登录成功后发起重定向
     *
     * @param request  请求参数信息
     * @param response 响应参数信息
     * @throws Exception 可能遇到的异常信息
     */
    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
    }

    /**
     * 重写登录失败后的调用事件
     *
     * @param token                   用户认证令牌信息
     * @param authenticationException 用户认证异常信息
     * @param request                 请求信息
     * @param response                响应信息
     * @return 判断结果
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException authenticationException, ServletRequest request, ServletResponse response) {
        //初始化类名
        String className = authenticationException.getClass().getName();
        //初始化消息信息
        String message = "";
        //如果是验证凭证错误异常，则直接设定消息内容为用户名或者密码错误
        if (IncorrectCredentialsException.class.getName().equals(className)) message = "用户名或密码错误，请重试。";
        else if (authenticationException.getMessage() != null && StringUtils.startsWith(authenticationException.getMessage(), "msg:"))
            //如果是身份认证异常，且内容不为空，同时以msg:开头，则消息内容替换掉msg:字段
            message = StringUtils.replace(authenticationException.getMessage(), "msg:", "");
        else {
            message = "对不起，系统出现点小问题，请稍后再试！";
            //将异常信息输出到控制台
            authenticationException.printStackTrace();
        }
        //设置请求信息中属性名和属性值
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }
}