package com.lenzetech.zhongyablack.utils;

import android.content.Context;

public class TimeUtils {
    /**
     * 判断时间差是否超过指定毫秒数
     *
     * @param lastTimestamp 上一次操作的时间戳
     * @param millis        时间间隔（毫秒）
     * @return 如果时间差超过指定毫秒数，则返回true；否则返回false。
     */
    public static boolean isInterval(long lastTimestamp, int millis) {
        long currentTimestamp = System.currentTimeMillis();
        long timeGap = (currentTimestamp - lastTimestamp); // 时间差（以秒为单位）

        return timeGap >= millis;
    }

    public static String repeatDisplay(Context context, String repeat) {
        String displayS = "";
        if (repeat.contains("1")) {
//            displayS += context.getString(R.string.mon);
        }
        if (repeat.contains("2")) {
//            displayS += context.getString(R.string.tue);
        }
        if (repeat.contains("3")) {
//            displayS += context.getString(R.string.wed);
        }
        if (repeat.contains("4")) {
//            displayS += context.getString(R.string.thu);
        }
        if (repeat.contains("5")) {
//            displayS += context.getString(R.string.fri);
        }
        if (repeat.contains("6")) {
//            displayS += context.getString(R.string.sat);
        }
        if (repeat.contains("7")) {
//            displayS += context.getString(R.string.sun);
        }
        return displayS;
    }
    public static byte weekStr2Byte(String weekStr) {
        byte value = 0;
        if (weekStr.contains("1"))
            value = 1;
        if (weekStr.contains("2"))
            value |= (1 << 1);
        if (weekStr.contains("3"))
            value |= (1 << 2);
        if (weekStr.contains("4"))
            value |= (1 << 3);
        if (weekStr.contains("5"))
            value |= (1 << 4);
        if (weekStr.contains("6"))
            value |= (1 << 5);
        if (weekStr.contains("7"))
            value |= (1 << 6);
        return value;
    }
    public static String byte2RepeatDayStr(byte b) {
        String repeatDay = "";
        if ((b & 0x01) == 0x01)
            repeatDay += "1";
        if ((b & 0x02) == 0x02)
            repeatDay += "2";
        if ((b & 0x04) == 0x04)
            repeatDay += "3";
        if ((b & 0x08) == 0x08)
            repeatDay += "4";
        if ((b & 0x10) == 0x10)
            repeatDay += "5";
        if ((b & 0x20) == 0x20)
            repeatDay += "6";
        if ((b & 0x40) == 0x40)
            repeatDay += "7";
        return repeatDay;
    }
}
