package com.lenzetech.zhongyablack.ble;

/**
 * author:created by mj
 * Date:2022/12/22 17:09
 * Description:
 */
public interface ICommandManager {

    void setConnectState(String address);

    void brightnessSet(String address, int zone, int brightness);

    void brightnessZone(String address, int zone);

    void lightPower(String address, int value);

    void colorSet(String address, int zone, int value);

    void modeSet(String address, int value);

    void fullColorSpeed(String address, int value);

    void soundSensitivity(String address, int value);

    void welcomePower(String address, int value);

    void welcomeMode(String address, int value);

    void welcomeColor(String address, int value);

    void doorOpenWarn(String address, int value);

    void radarLinkage(String address, int value);

    void overSpeedLinkage(String address, int value);

    void steerLinkage(String address, int value);

    void airConditionLinkage(String address, int value);

    void blindSpotDetection(String address, int value);

    void doorColor(String address, int value);

    void turnColor(String address, int value);

    void blindSpotColor(String address, int value);

    void flowModeSet(String address, int value);

    void flowModeEffect(String address, int value);

    void flowModeSpeed(String address, int value);

    void flowModeDirection(String address, int value);

    void nightBrightnessHalve(String address, int value);

    void restoreFactory(String address);

    void factorySetting(String address, int value);

    void setDial(String address, boolean dial1, boolean dial2, boolean dial3, boolean dial4, boolean dial5, boolean dial6, boolean dial7, boolean dial8, boolean dial9, boolean dial10, boolean dial11, boolean dial12, boolean dial13, boolean dial14, boolean dial15, boolean dial16);

    void flowLightSet(String address, int position, int number);

    void flowLightInstallDir(String address, int position, int number);

    void lightSnImport(String address, int value);

    void micSource(String address, int value);

    void squareControlLearn(String address, int value);

    void startFlash(String address);

}
