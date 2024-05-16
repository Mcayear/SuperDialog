package cn.vusv.plugin;

import cn.nukkit.lang.PluginI18n;
import cn.nukkit.lang.PluginI18nManager;
import cn.nukkit.plugin.PluginBase;
import cn.vusv.plugin.command.SuperDialogCommand;
import lombok.Getter;

public class SuperDialogPlugin extends PluginBase {
    @Getter
    public static SuperDialogPlugin instance;
    @Getter
    public static PluginI18n i18n;

    @Override
    public void onLoad() {
        //save Plugin Instance
        instance = this;
        //register the plugin i18n
        i18n = PluginI18nManager.register(this);
    }

    @Override
    public void onEnable() {
        //this.getServer().getPluginManager().registerEvents(new Events(), this);
        this.getServer().getCommandMap().register("SuperDialog", new SuperDialogCommand());
        init();
    }

    public void init() {
        this.saveResource("config.yml", false);
        this.saveResource("scenes/default.lua", "scenes/default.lua", false);
        //PluginConfig.init();
    }
}
