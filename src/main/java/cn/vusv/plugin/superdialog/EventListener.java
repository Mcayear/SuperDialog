package cn.vusv.plugin.superdialog;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    public static final boolean hasNInvShop = Server.getInstance().getPluginManager().getPlugin("NInvShop") != null;

    private final SuperDialogPlugin plugin;

    public EventListener(SuperDialogPlugin plugin) {
        this.plugin = plugin;
        if (!hasNInvShop) plugin.getLogger().warning("软依赖 NInvShop 未加载！");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false) //DON'T FORGET THE ANNOTATION @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // 副作用回收
        SuperDialogPlugin.playerGlobals.remove(event.getPlayer().getName());
    }
}
