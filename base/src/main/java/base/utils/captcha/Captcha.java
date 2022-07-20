package base.utils.captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by cl on 2017/6/19.
 * 图片验证码
 */
public class Captcha {

    /* 验证码 */
    protected String code;

    /* 验证码图片 */
    protected BufferedImage image;

    /* 图片宽度，默认：60 */
    protected Integer width = 60;
    /* 图片高度，默认：20 */
    protected Integer height = 20;
    /* 字体，默认：Fixedsys*/
    protected String fontType = "Fixedsys";

    public Captcha(String code) {
        this(code, null, null, null);
    }

    public Captcha(String code, int width, int height) {
        this(code, width, height, null);
    }

    public Captcha(String code, Integer width, Integer height, String fontType) {
        this.code = code;
        this.width = null != width ? width : this.width;
        this.height = null != height ? height : this.height;
        this.fontType = null != fontType ? fontType : this.fontType;
        // 创建图片
        this.createImage();
    }

    /**
     * 获取验证码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取验证码图片字节数组
     */
    public byte[] getBytes(String formatName) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeTo(bos, formatName);
        return bos.toByteArray();
    }

    /**
     * 通过流输出验证码图片
     */
    public void writeTo(OutputStream out, String formatName) {
        try {
            ImageIO.write(image, formatName, out);
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 创建验证码图片
     */
    protected void createImage() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Random random = new Random();

        // 获取绘图对象(画笔)
        Graphics g = image.getGraphics();

        // 绘制矩形背景
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        // 绘制矩形边框
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);

        // 设置字体
        int fontSize = (int) (height * 0.8);
        Font font = new Font(fontType, Font.PLAIN, fontSize);
        g.setFont(font);

        // 绘制干扰线
        for (int i = 0; i < 50; i++) {
            // 设置干扰线颜色
            g.setColor(new Color(200 + random.nextInt(55),
                    200 + random.nextInt(55),
                    200 + random.nextInt(55)));
            int x1 = 2 + random.nextInt(width - 4);
            int y1 = 2 + random.nextInt(height - 4);
            int x2 = 2 + random.nextInt(width - 4);
            int y2 = 2 + random.nextInt(height - 4);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制验证码
        int len = code.length();
        int fontX = width / (len + 1);
        int refX = (width - fontX * len) / 2;
        int fontY = fontSize - 2;
        for (int i = 0; i < len; i++) {
            // 设置字体颜色
            g.setColor(new Color(20 + random.nextInt(110),
                    20 + random.nextInt(110),
                    20 + random.nextInt(110)));
            g.drawString(String.valueOf(code.charAt(i)), refX + i * fontX, fontY);
        }

        g.dispose();
    }

}
