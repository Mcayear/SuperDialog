package cn.vusv.plugin.superdialog;

import cn.nukkit.lang.PluginI18n;
import cn.nukkit.lang.PluginI18nManager;
import cn.nukkit.plugin.PluginBase;
import cn.vusv.plugin.superdialog.command.SuperDialogCommand;
import cn.vusv.plugin.superdialog.config.PlayerConfig;
import lombok.Getter;
import org.luaj.vm2.Globals;

import java.util.HashMap;

public class SuperDialogPlugin extends PluginBase {
    @Getter
    public static SuperDialogPlugin instance;
    @Getter
    public static PluginI18n i18n;
    @Getter
    public static HashMap<String, Globals> playerGlobals = new HashMap<>();

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
        //Register the EventListener
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    public void init() {
        this.saveResource("config.yml", false);
        this.saveResource("scenes/default.lua", "scenes/default.lua", false);
        PlayerConfig.init();
        //PluginConfig.init();
    }
}
