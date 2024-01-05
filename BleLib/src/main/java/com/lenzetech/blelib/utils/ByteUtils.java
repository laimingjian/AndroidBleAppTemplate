package com.lenzetech.blelib.utils;

/**
 * author:created by mj
 * Date:2022/8/24 17:46
 * Description:字节相关的数据转换类
 */
public class ByteUtils {
    private static String hexIndexString = "0123456789ABCDEF";

    //16进制字符串转字节数组
    public static byte[] hexStr2Bytes(String hexStr) {
        if (hexStr == null || hexStr.equals(""))
            return null;
        //全部转化为大写字母
        hexStr = hexStr.toUpperCase();
        //字节数组长度
        int length = hexStr.length() / 2;
        //转化为字符数组
        byte[] value = new byte[length];
        char[] hexChar = hexStr.toCharArray();
        for (int i = 0; i < length; i++) {
            int p = i * 2;
            //左移4位得到字节高位再和下一个字符或得到位
            value[i] = (byte) (hexIndexString.indexOf(hexChar[p]) << 4 | hexIndexString.indexOf(hexChar[p + 1]));
        }
        return value;
    }

    //字节转十六进制字符串
    public static String byte2HexString(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1)
            hex = "0" + hex;
        return hex.toUpperCase();
    }

    //字节数组转16进制字符串有分隔符方便看
    public static String bytes2HexStringLog(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        int i = 0;
        for (byte b : bytes) {
            hex.append(i++);
            hex.append(":");
            hex.append(byte2HexString(b));
            hex.append(" ");
        }
        return hex.toString();
    }

    //字节数组转16进制字符串
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        int i = 0;
        for (byte b : bytes) {
            hex.append(byte2HexString(b));
        }
        return hex.toString();
    }

    //int转16进制字符串(长度固定为2前面补0，不需要补零可使用Integer.toHexString())
    public static String int2HexString(int n) {
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return String.valueOf(hexIndexString.charAt(d1)) + hexIndexString.charAt(d2);
    }

    //int转字节数组
    public static byte[] intToBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) ((i >> 24) & 0xFF);

        result[1] = (byte) ((i >> 16) & 0xFF);

        result[2] = (byte) ((i >> 8) & 0xFF);

        result[3] = (byte) (i & 0xFF);

        return result;

    }
    //计算累加和
    public static byte calChecksum(byte[] byteArray) {
        int sum = 0;
        for (byte b : byteArray) {
            sum += b;
        }
        return (byte) (sum & 0xFF);
    }
    public static String hexStringToDecimalString(String hexString) {
        // 将十六进制字符串转换为字节数组
        byte[] byteArray = hexStringToByteArray(hexString);

        // 将字节数组转换为"0.1.7.28"形式的字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append((b & 0xFF)).append(".");
        }

        // 移除最后一个多余的"."
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    // 将十六进制字符串解码为字节数组
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] byteArray = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }
}
