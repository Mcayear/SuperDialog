package cn.vusv.plugin.superdialog.config;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.vusv.ninvshop.ExamineNeed;

public class LuaToolsVarable {
    public static boolean examineNeed(String[] needArray, Player player, String reason) {
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
