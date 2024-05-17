package cn.vusv.plugin.superdialog.config;

import cn.nukkit.utils.Config;

public class LuaShareVarable {
    public String playerName;
    public Config cfg;

    public LuaShareVarable(String playerName) {
        this.playerName = playerName;
        this.cfg = PlayerConfig.create(playerName);;
    }

    public void removeValue(String key) {
        cfg.remove(key);
        cfg.save();
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return cfg.getBoolean(key, defaultValue);
    }

    public boolean getBooleanValue(String key) {
        return cfg.getBoolean(key);
    }

    public void setBooleanValue(String key, boolean value) {
        cfg.set(key, value);
        cfg.save();
    }

    public String getStringValue(String key, String defaultValue) {
        return cfg.getString(key, defaultValue);
    }

    public String getStringValue(String key) {
        return cfg.getString(key);
    }

    public void setStringValue(String key, String value) {
        cfg.set(key, value);
        cfg.save();
    }

    public int getIntValue(String key, int defaultValue) {
        return cfg.getInt(key, defaultValue);
    }

    public int getIntValue(String key) {
        return cfg.getInt(key);
    }

    public void setIntValue(String key, int value) {
        cfg.set(key, value);
        cfg.save();
    }
}
