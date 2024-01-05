package com.lenzetech.zhongyablack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.lenzetech.zhongyablack.ble.BleManager;
import com.lenzetech.zhongyablack.ble.BleViewModel;
import com.lenzetech.zhongyablack.databinding.ActivityMainBinding;
import com.lenzetech.zhongyablack.ui.adapter.FragmentAdapter;
import com.lenzetech.zhongyablack.ui.dialog.TipsDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final List<Fragment> fragmentList = new ArrayList<>();
    private BleViewModel bleViewModel;
    private AppViewModel appViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        BleManager.getInstance().initBle(this);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        fragmentList.add(new FullColorFragment());
        fragmentList.add(new SymphonyFragment());
        fragmentList.add(new BluetoothFragment());
        fragmentList.add(new BrightnessFragment());
        fragmentList.add(new SettingFragment());

        bleViewModel = BleManager.getInstance().getBleViewModel();
        appViewModel = BleManager.getInstance().getAppViewModel();

        binding.vp.setUserInputEnabled(false);
        binding.vp.setAdapter(fragmentAdapter);
        binding.vp.setOffscreenPageLimit(1);
        binding.rgBnv.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_full_color) {
                binding.vp.setCurrentItem(0);
            } else if (i == R.id.rb_symphony) {
                binding.vp.setCurrentItem(1);
            } else if (i == R.id.rb_bl) {
                binding.vp.setCurrentItem(2);
            } else if (i == R.id.rb_brightness) {
                binding.vp.setCurrentItem(3);
            } else if (i == R.id.rb_setting) {
                binding.vp.setCurrentItem(4);
            }
        });

        binding.rgBnvText.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_full_color_text) {
                binding.vp.setCurrentItem(0);
            } else if (i == R.id.rb_symphony_text) {
                binding.vp.setCurrentItem(1);
            } else if (i == R.id.rb_bl_text) {
                binding.vp.setCurrentItem(2);
            } else if (i == R.id.rb_brightness_text) {
                binding.vp.setCurrentItem(3);
            } else if (i == R.id.rb_setting_text) {
                binding.vp.setCurrentItem(4);
            }
        });

        binding.vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.rbFullColor.setChecked(true);
                        binding.rbFullColorText.setChecked(true);
                        break;
                    case 1:
                        binding.rbSymphony.setChecked(true);
                        binding.rbSymphonyText.setChecked(true);
                        break;
                    case 2:
                        binding.rbBl.setChecked(true);
                        binding.rbBlText.setChecked(true);
                        break;
                    case 3:
                        binding.rbBrightness.setChecked(true);
                        binding.rbBrightnessText.setChecked(true);
                        break;
                    case 4:
                        binding.rbSetting.setChecked(true);
                        binding.rbSettingText.setChecked(true);
                        break;
                }
            }
        });

        bleViewModel.startScan(this);
        bleViewModel.getNotifyAddress().observe(this, s -> {
            if (bleViewModel.getNotifyMap().containsKey(s)) {
                byte[] data = bleViewModel.getNotifyMap().get(s);
                if (data != null && data.length > 0) {
                    String str = new String(data);
                    switch (str) {
                        case "[2E00]":
                        case "[1900]":
                            TipsDialog.show(MainActivity.this, false, 1);
                            break;
                        case "[2E01]":
                            TipsDialog.show(MainActivity.this, false, 2);
                            break;
                        case "[2E02]":
                            TipsDialog.show(MainActivity.this, false, 3);
                            break;
                    }
                }
                bleViewModel.getNotifyMap().remove(s);
            }
        });
    }

}