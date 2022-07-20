package base.utils.captcha;

import java.util.Random;

/**
 * Created by cl on 2017/6/19.
 * 验证码工具类
 */
public class CaptchaUtils {

    /* 数字 */
    public final static int NUM_WORD = 0;
    /* 大写字母 */
    public final static int UPPER_WORD = 1;
    /* 小写字母 */
    public final static int LOWER_WORD = 2;
    /* 混合大小写字母、数字 */
    public final static int MIX_WORD = 9;

    private CaptchaUtils() {
    }

    /**
     * 生成验证码字符串（生成指定位数）
     */
    public static String generateCode(int len) {
        return generateCode(MIX_WORD, len);
    }

    /**
     * 生成验证码字符串（生成指定位数，大写字母、小写字母、数字）
     */
    public static String generateCode(int wordType, int len) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        while (sb.length() < len) {
            int type = wordType != MIX_WORD ? wordType : random.nextInt(2);
            char ch = ' ';

            switch (type) {
                // 数字
                case NUM_WORD:
                    ch = (char) (random.nextInt(9) + 48);
                    break;
                // 大写字母(A-Z: 65-90)
                case UPPER_WORD:
                    ch = (char) (random.nextInt(25) + 65);
                    break;
                // 小写字母(a-z: 97-122)
                case LOWER_WORD:
                    ch = (char) (random.nextInt(25) + 97);
                    break;
            }

            sb.append(ch);
        }

        return sb.toString();
    }

    /**
     * 生成验证码（生成指定位数）
     */
    public static Captcha generateCaptcha(int len) {
        return generateCaptcha(MIX_WORD, len);
    }

    /**
     * 生成验证码（生成指定位数，大写字母、小写字母、数字）
     */
    public static Captcha generateCaptcha(int wordType, int len) {
        String code = generateCode(wordType, len);
        return new Captcha(code);
    }

    /**
     * 生成验证码（生成指定位数，图片尺寸）
     */
    public static Captcha generateCaptcha(int len, int width, int height) {
        return generateCaptcha(MIX_WORD, len, width, height);
    }

    /**
     * 生成验证码（生成指定位数，大写字母、小写字母、数字，图片尺寸）
     */
    public static Captcha generateCaptcha(int wordType, int len, int width, int height) {
        String code = generateCode(wordType, len);
        return new Captcha(code, width, height);
    }

    /**
     * 生成验证码（生成指定位数，大写字母、小写字母、数字，图片尺寸，字体）
     */
    public static Captcha generateCaptcha(int wordType, int len, int width, int height, String fontType) {
        String code = generateCode(wordType, len);
        return new Captcha(code, width, height, fontType);
    }

}
