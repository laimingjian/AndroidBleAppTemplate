package com.lenzetech.zhongyablack.ble;

import android.graphics.Color;
import android.util.Log;

import com.lenzetech.blelib.utils.ByteUtils;

import java.util.Locale;

/**
 * author:created by mj
 * Date:2022/12/22 17:09
 * Description:
 */
public class CommandManager implements ICommandManager {
    private static final String TAG = "CommandManager";
    //单例对象
    private static volatile CommandManager commandManager;
    private final BleManager bleManager = BleManager.getInstance();
    private final byte HEAD = (byte) 0x5B;
    private final byte TAIL = (byte) 0x5D;

    //获取单例模式
    public static CommandManager getInstance() {
        //双重校验锁提高效率
        if (commandManager == null)
            synchronized (CommandManager.class) {
                if (commandManager == null)
                    commandManager = new CommandManager();
            }
        return commandManager;
    }

    @Override
    public void setConnectState(String address) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x30, (byte) 0x41, (byte) 0x30, (byte) 0x31
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void brightnessSet(String address, int zone, int brightness) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x30,
                (byte) (zone == 3 ? 0x31 : 0x32), 0x30,
                (byte) (zone == 3 ? 0x30 : 0x30 + zone), 0x30,
                (byte) (0x30 + brightness)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void brightnessZone(String address, int zone) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x31, (byte) 0x34, (byte) 0x30,
                (byte) (0x30 + zone)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void lightPower(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x30, (byte) 0x37, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void colorSet(String address, int zone, int value) {
        byte[] colorByte = Integer.toHexString(value).getBytes();
        String data = "[06" + Integer.toHexString(value).substring(2) + "]";
        switch (zone) {
            case 2:
                data = "[10" + Integer.toHexString(value).substring(2) + "]";
                break;
            case 3:
                data = "[09" + Integer.toHexString(value).substring(2) + "]";
                break;
        }
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void modeSet(String address, int value) {
//        byte[] data = new byte[]{HEAD,
//                (byte) 0x30, (byte) 0x34, (byte) (value < 16 ? 0x30 : 0x31),
//                0, TAIL};
//        if (value < 10) {
//            data[data.length - 2] = (byte) (0x30 + value);
//        }
        String data = "[04" + ByteUtils.byte2HexString((byte) value) + "]";
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void fullColorSpeed(String address, int value) {
        String data = "[3E" + ByteUtils.byte2HexString((byte) value) + "]";
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void soundSensitivity(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x30, (byte) 0x33, (byte) 0x30, (byte) 0x30, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void welcomePower(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x30, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void welcomeMode(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x39, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void welcomeColor(String address, int value) {
        String data = "[38" + ByteUtils.byte2HexString((byte) value) + "]";
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void doorOpenWarn(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x31, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void radarLinkage(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x32, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void overSpeedLinkage(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x33, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void steerLinkage(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x34, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void airConditionLinkage(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x33, (byte) 0x35, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void blindSpotDetection(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x34, (byte) 0x34, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void doorColor(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x34, (byte) 0x36, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void turnColor(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x34, (byte) 0x35, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void blindSpotColor(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x34, (byte) 0x37, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void flowModeSet(String address, int value) {
        String data = "[1E" + ByteUtils.byte2HexString((byte) value) + "]";
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void flowModeEffect(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x32, (byte) 0x39, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void flowModeSpeed(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x30, (byte) 0x42, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void flowModeDirection(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x30, (byte) 0x43, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void nightBrightnessHalve(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x31, (byte) 0x41, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void restoreFactory(String address) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x34, (byte) 0x32, (byte) 0x30, (byte) 0x30
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void factorySetting(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x32, (byte) 0x31, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void setDial(String address, boolean dial1, boolean dial2, boolean dial3, boolean dial4, boolean dial5, boolean dial6, boolean dial7, boolean dial8, boolean dial9, boolean dial10, boolean dial11, boolean dial12, boolean dial13, boolean dial14, boolean dial15, boolean dial16) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x32, (byte) 0x30,
                (byte) (dial1 ? 0x31 : 0x30), (byte) (dial2 ? 0x31 : 0x30), (byte) (dial3 ? 0x31 : 0x30), (byte) (dial4 ? 0x31 : 0x30), (byte) (dial5 ? 0x31 : 0x30), (byte) (dial6 ? 0x31 : 0x30), (byte) (dial7 ? 0x31 : 0x30), (byte) (dial8 ? 0x31 : 0x30), (byte) (dial9 ? 0x31 : 0x30), (byte) (dial10 ? 0x31 : 0x30), (byte) (dial11 ? 0x31 : 0x30), (byte) (dial12 ? 0x31 : 0x30), (byte) (dial13 ? 0x31 : 0x30), (byte) (dial14 ? 0x31 : 0x30), (byte) (dial15 ? 0x31 : 0x30), (byte) (dial16 ? 0x31 : 0x30)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void flowLightSet(String address, int position, int number) {
        String data = "[]";
        switch (position) {
            case 1:
                data = "[1C0" + String.format(Locale.US, "%03d", number) + "]";
                break;
            case 2:
                data = "[0F0" + String.format(Locale.US, "%03d", number) + "]";
                break;
            case 3:
                data = "[130" + String.format(Locale.US, "%03d", number) + "]";
                break;
            case 4:
                data = "[1D0" + String.format(Locale.US, "%03d", number) + "]";
                break;
        }
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void flowLightInstallDir(String address, int position, int value) {
        String data = "[]";
        switch (position) {
            case 1:
                data = "[2C0" + value + "]";
                break;
            case 2:
                data = "[2F0" + value + "]";
                break;
            case 3:
                data = "[480" + value + "]";
                break;
            case 4:
                data = "[490" + value + "]";
                break;
            case 5:
                data = "[4A0" + value + "]";
                break;
            case 6:
                data = "[4B0" + value + "]";
                break;
        }
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void lightSnImport(String address, int value) {
        String data = "[400" + String.format(Locale.US, "%03d", value) + "]";
        BleManager.getInstance().sendData2Ble(address, data.getBytes());
    }

    @Override
    public void micSource(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x31, (byte) 0x42, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void squareControlLearn(String address, int value) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x32, (byte) 0x32, (byte) 0x30,
                (byte) (0x30 + value)
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }

    @Override
    public void startFlash(String address) {
        byte[] data = new byte[]{HEAD,
                (byte) 0x34, (byte) 0x31, (byte) 0x30, (byte) 0x30
                , TAIL};
        BleManager.getInstance().sendData2Ble(address, data);
    }
}
