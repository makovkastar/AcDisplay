/*
 * Copyright (C) 2013 AChep@xda <artemchep@gmail.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.achep.activedisplay.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.achep.activedisplay.Config;
import com.achep.activedisplay.Keys;
import com.achep.activedisplay.R;

/**
 * Created by Artem on 09.02.14.
 */
public class InterfaceFragment extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener,
        Config.OnConfigChangedListener {

    private CheckBoxPreference mShowWallpaper;

    private boolean mBroadcasting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.interface_settings);

        mShowWallpaper = (CheckBoxPreference) findPreference(
                Keys.Settings.INTERFACE_IS_WALLPAPER_SHOWN);
        mShowWallpaper.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Config config = Config.getInstance(getActivity());
        config.addOnConfigChangedListener(this);

        updateShowWallpaperPreference(config);
    }

    @Override
    public void onPause() {
        super.onPause();
        Config config = Config.getInstance(getActivity());
        config.removeOnConfigChangedListener(this);
    }

    private void updateShowWallpaperPreference(Config config) {
        mBroadcasting = true;
        mShowWallpaper.setChecked(config.isWallpaperShown());
        mBroadcasting = false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mBroadcasting) {
            return true;
        }

        Config config = Config.getInstance(getActivity());
        if (preference == mShowWallpaper) {
            config.setWallpaperShown(getActivity(), (Boolean) newValue, this);
        } else
            return false;
        return true;
    }

    @Override
    public void onConfigChanged(Config config, String key, Object value) {
        switch (key) {
            case Config.KEY_INTERFACE_WALLPAPER_SHOWN:
                updateShowWallpaperPreference(config);
                break;
        }
    }
}