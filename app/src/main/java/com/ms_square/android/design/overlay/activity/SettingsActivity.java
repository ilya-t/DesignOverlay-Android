package com.ms_square.android.design.overlay.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ms_square.android.design.overlay.R;
import com.ms_square.android.design.overlay.activity.base.BaseActivity;
import com.ms_square.android.design.overlay.app.AppEnvironment;
import com.ms_square.android.design.overlay.event.OverlayServiceEvent;
import com.ms_square.android.design.overlay.service.DesignOverlayService;

public class SettingsActivity extends BaseActivity {

    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 10001;

    Switch mGridSwitch;

    private boolean mStartOverlayAfterPermission;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mGridSwitch = (Switch) findViewById(R.id.grid_switch);
        mGridSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (DesignOverlayService.canDrawOverlays(SettingsActivity.this)) {
                        startService(DesignOverlayService.createIntent(SettingsActivity.this));
                    } else {
                        mStartOverlayAfterPermission = true;
                        mGridSwitch.setChecked(false);
                        requestOverlayPermission();
                    }
                } else {
                    stopService(DesignOverlayService.createIntent(SettingsActivity.this));
                }
            }
        });
    }

    @Override
    protected boolean shouldRegisterToEventBus() {
        return true;
    }

    public void onEventMainThread(OverlayServiceEvent event) {
        if (mGridSwitch != null) {
            mGridSwitch.setChecked(event.isRunning);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStartOverlayAfterPermission && DesignOverlayService.canDrawOverlays(this)) {
            mStartOverlayAfterPermission = false;
            mGridSwitch.setChecked(true);
            return;
        }
        mGridSwitch.setChecked(AppEnvironment.INSTANCE.isOverlayServiceRunning());
    }

    private void requestOverlayPermission() {
        Toast.makeText(this, R.string.toast_overlay_permission_required, Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
        }
    }
}