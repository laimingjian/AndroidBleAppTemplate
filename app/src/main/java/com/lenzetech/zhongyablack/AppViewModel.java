package com.lenzetech.zhongyablack;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class AppViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> workMode = new MutableLiveData<>(0);
    private MutableLiveData<Integer> flowMode = new MutableLiveData<>(0);
    private MutableLiveData<Integer> power = new MutableLiveData<>(0);
    private MutableLiveData<Integer> brightness = new MutableLiveData<>(0);
    private MutableLiveData<Integer> brightness1 = new MutableLiveData<>(0);
    private MutableLiveData<Integer> brightness2 = new MutableLiveData<>(0);
    private MutableLiveData<Integer> soundSensitivity = new MutableLiveData<>(0);
    private MutableLiveData<Integer> flowSpeed = new MutableLiveData<>(0);
    private MutableLiveData<Integer> flowDir = new MutableLiveData<>(0);
    private MutableLiveData<Integer> rgbSpeed = new MutableLiveData<>(0);
    private MutableLiveData<String> hardVersion = new MutableLiveData<>("");
    private MutableLiveData<String> carType = new MutableLiveData<>("");
    private MutableLiveData<String> softVersion = new MutableLiveData<>("");
    private MutableLiveData<Boolean> welcomePower = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> doorOpenWarn = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> radarLink = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> overSpeedLink = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> steerLink = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> airConLink = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> blindSpotDetect = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> halfNight = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> briPart = new MutableLiveData<>(false);
    private MutableLiveData<Integer> welcomeDir = new MutableLiveData<>(0);
    private MutableLiveData<Integer> welcomeColor = new MutableLiveData<>(0);
    private MutableLiveData<Integer> rhythmSource = new MutableLiveData<>(0);
    private MutableLiveData<Integer> flowEffect = new MutableLiveData<>(0);
    private MutableLiveData<Boolean[]> dialState = new MutableLiveData<>(new Boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false});
    private MutableLiveData<Integer> mdLampNum = new MutableLiveData<>(30);
    private MutableLiveData<Boolean> mdDir = new MutableLiveData<>(false);
    private MutableLiveData<Integer> cpLampNum = new MutableLiveData<>(30);
    private MutableLiveData<Boolean> cpDir = new MutableLiveData<>(false);
    private MutableLiveData<Integer> fdLampNum = new MutableLiveData<>(30);
    private MutableLiveData<Boolean> lfdDir = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> rfdDir = new MutableLiveData<>(false);
    private MutableLiveData<Integer> rdLampNum = new MutableLiveData<>(30);
    private MutableLiveData<Boolean> lrdDir = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> rrdDir = new MutableLiveData<>(false);
    private MutableLiveData<Integer> lightSn = new MutableLiveData<>(0);

    Application application;

    public AppViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public MutableLiveData<Integer> getMdLampNum() {
        return mdLampNum;
    }

    public MutableLiveData<Integer> getCpLampNum() {
        return cpLampNum;
    }

    public MutableLiveData<Integer> getFdLampNum() {
        return fdLampNum;
    }

    public MutableLiveData<Integer> getRdLampNum() {
        return rdLampNum;
    }

    public MutableLiveData<Integer> getLightSn() {
        return lightSn;
    }

    public MutableLiveData<Integer> getWorkMode() {
        return workMode;
    }

    public MutableLiveData<Integer> getFlowMode() {
        return flowMode;
    }

    public MutableLiveData<Integer> getPower() {
        return power;
    }

    public MutableLiveData<Integer> getBrightness() {
        return brightness;
    }

    public MutableLiveData<Integer> getBrightness1() {
        return brightness1;
    }

    public MutableLiveData<Integer> getBrightness2() {
        return brightness2;
    }

    public MutableLiveData<Integer> getSoundSensitivity() {
        return soundSensitivity;
    }

    public MutableLiveData<Integer> getFlowSpeed() {
        return flowSpeed;
    }

    public MutableLiveData<Integer> getFlowDir() {
        return flowDir;
    }

    public MutableLiveData<Integer> getRgbSpeed() {
        return rgbSpeed;
    }

    public MutableLiveData<String> getHardVersion() {
        return hardVersion;
    }

    public MutableLiveData<String> getCarType() {
        return carType;
    }

    public MutableLiveData<String> getSoftVersion() {
        return softVersion;
    }

    public MutableLiveData<Boolean> getWelcomePower() {
        return welcomePower;
    }

    public MutableLiveData<Boolean> getDoorOpenWarn() {
        return doorOpenWarn;
    }

    public MutableLiveData<Boolean> getRadarLink() {
        return radarLink;
    }

    public MutableLiveData<Boolean> getOverSpeedLink() {
        return overSpeedLink;
    }

    public MutableLiveData<Boolean> getSteerLink() {
        return steerLink;
    }

    public MutableLiveData<Boolean> getAirConLink() {
        return airConLink;
    }

    public MutableLiveData<Boolean> getBlindSpotDetect() {
        return blindSpotDetect;
    }

    public MutableLiveData<Boolean> getHalfNight() {
        return halfNight;
    }

    public MutableLiveData<Boolean> getBriPart() {
        return briPart;
    }

    public MutableLiveData<Integer> getWelcomeDir() {
        return welcomeDir;
    }

    public MutableLiveData<Integer> getWelcomeColor() {
        return welcomeColor;
    }

    public MutableLiveData<Integer> getRhythmSource() {
        return rhythmSource;
    }

    public MutableLiveData<Integer> getFlowEffect() {
        return flowEffect;
    }

    public MutableLiveData<Boolean[]> getDialState() {
        return dialState;
    }

    public MutableLiveData<Boolean> getMdDir() {
        return mdDir;
    }

    public MutableLiveData<Boolean> getCpDir() {
        return cpDir;
    }

    public MutableLiveData<Boolean> getLfdDir() {
        return lfdDir;
    }

    public MutableLiveData<Boolean> getLrdDir() {
        return lrdDir;
    }

    public MutableLiveData<Boolean> getRfdDir() {
        return rfdDir;
    }

    public MutableLiveData<Boolean> getRrdDir() {
        return rrdDir;
    }

}
