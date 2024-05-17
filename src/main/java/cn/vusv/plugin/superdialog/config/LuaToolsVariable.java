package cn.vusv.plugin.superdialog.config;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.vusv.ninvshop.ExamineNeed;
import cn.vusv.plugin.superdialog.EventListener;

public class LuaToolsVariable {
    public static boolean examineNeed(String[] needArray, Player player, String reason) {
        if (!EventListener.hasNInvShop) return false;
        return ExamineNeed.examineNeed(needArray, player, reason);
    }

    public static void executeCommand(String[] command, Player player) {
        for (String cmd : command) {
            cmd = cmd.replace("@p", player.getName());
            if (cmd.endsWith("&con")) {
                cmd = cmd.substring(0, cmd.length() - 4);
                player.getServer().dispatchCommand(Server.getInstance().getConsoleSender(), cmd);
            } else if (cmd.endsWith("&player") || cmd.endsWith("&op")) {
                player.getServer().dispatchCommand(player, cmd);
            }
//            else if (cmd.endsWith("&self")){
//                player.getServer().dispatchCommand(sender, cmd);
//            }
        }
    }
}
