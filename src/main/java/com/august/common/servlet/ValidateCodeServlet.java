package com.august.common.servlet;

import com.august.common.utils.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Description;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * PROJECT_NAME: core
 * PACKAGE_NAME: com.august.common.servlet
 * Author: August
 * Update: August(2016/4/16)
 * Description:生成随机验证码
 */
@Description("生成随机验证码")
public class ValidateCodeServlet extends HttpServlet {
    //声明验证码标识
    public static final String VALIDATE_CODE = "validateCode";

    private int w = 70;
    private int h = 26;

    /**
     * 创建无参构造器
     */
    public ValidateCodeServlet() {
        super();
    }

    public void destroy() {
        super.destroy();
    }

    /**
     * 校验验证码信息
     *
     * @param request      请求信息
     * @param validateCode 验证码信息
     * @return
     */
    public static boolean validate(HttpServletRequest request, String validateCode) {
        //获取请求信息中的验证码信息
        String code = (String) request.getSession().getAttribute(VALIDATE_CODE);
        //进行验证码的比较
        return validateCode.toUpperCase().equals(code);
    }

    /**
     * 发送GET请求方式
     *
     * @param request  请求信息
     * @param response 响应结果
     * @throws ServletException 可能出现的服务器异常
     * @throws IOException      输入输出异常
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求信息中的验证码信息（AJAX验证，成功返回true）
        String validateCode = request.getParameter(VALIDATE_CODE);
        if (StringUtils.isNotBlank(validateCode))
            response.getOutputStream().print(validate(request, validateCode) ? true : false);
        else
            super.doGet(request, response);
    }

    /**
     * 发送post请求
     *
     * @param request  请求信息
     * @param response 相应结果
     * @throws ServletException
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        createImage(request, response);
    }

    /**
     * 创建图片
     *
     * @param request  请求信息
     * @param response 响应结果
     */
    private void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置响应头以及日期头信息和响应类型
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        //获取长宽信息
        String width = request.getParameter("width");
        String height = request.getParameter("heigth");
        if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
            w = NumberUtils.toInt(width);
            h = NumberUtils.toInt(height);
        }

        //创建图片
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics graphics = image.getGraphics();

        //生成背景
        createBackground(graphics);

        //生成字符
        String string = createCharacter(graphics);
        //将验证码字符放入会话信息中
        request.getSession().setAttribute(VALIDATE_CODE, string);
        //关闭画笔
        graphics.dispose();
        //创建写出流
        OutputStream outputStream = response.getOutputStream();
        //将图片信息以JPEG的格式写出到画布
        ImageIO.write(image, "JPEG", outputStream);
        outputStream.close();
    }

    /**
     * 创建随机字符
     *
     * @param graphics 画笔
     * @return 随机字符串
     */
    private String createCharacter(Graphics graphics) {
        //定义验证码范围
        char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
                'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};
        String[] fontTypes = {"Arial", "Arial Black", "AvantGarde Bk BT", "Calibri"};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        //准备生成验证码
        for (int i = 0; i < 4; i++) {
            String string = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            graphics.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
            graphics.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)], Font.BOLD, 26));
            graphics.drawString(string, 15 * i + 5, 19 + random.nextInt(8));
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
     * 通过画笔创建背景图片
     *
     * @param graphics 画笔
     */
    private void createBackground(Graphics graphics) {
        //填充背景颜色
        graphics.setColor(getRandColor(220, 250));
        //设置画布填充位置
        graphics.fillRect(0, 0, w, h);

        //添加干扰线条
        for (int i = 0; i < 8; i++) {
            graphics.setColor(getRandColor(40, 150));
            Random random = new Random();
            //设置干扰线条位置（起止点）
            int x = random.nextInt(w);
            int y = random.nextInt(h);
            int x1 = random.nextInt(w);
            int y1 = random.nextInt(h);
            //绘制出干扰线条
            graphics.drawLine(x, y, x1, y1);
        }

    }

    /**
     * 创建随机颜色
     *
     * @param frontColor      前景色
     * @param backGroundColor 背景色
     * @return
     */
    private Color getRandColor(int frontColor, int backGroundColor) {
        int f = frontColor;
        int b = backGroundColor;
        Random random = new Random();
        if (f > 255) f = 255;
        if (b > 255) b = 255;
        return new Color(f + random.nextInt(b - f), f + random.nextInt(b - f), f + random.nextInt(b - f));
    }
}

























