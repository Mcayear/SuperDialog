package cn.vusv.plugin.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.vusv.plugin.SuperDialogPlugin;
import cn.vusv.plugin.form.ScrollingTextDialogHelper;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileReader;

public class SuperDialogCommand extends Command {
    public SuperDialogPlugin plugin = SuperDialogPlugin.getInstance();
    private Globals globals;

    public SuperDialogCommand() {
        super("superdialog", "超级NPC对话框", "/superdialog open <玩家名> [场景ID]");

        this.getCommandParameters().clear();

        this.getCommandParameters().put("help", new CommandParameter[]{
                CommandParameter.newEnum("help", false, new String[]{"help"})
        });

        this.getCommandParameters().put("reload", new CommandParameter[]{
                CommandParameter.newEnum("reload", false, new String[]{"reload"})
        });

        this.getCommandParameters().put("open", new CommandParameter[]{
                CommandParameter.newEnum("open", false, new String[]{"open"}),
                CommandParameter.newType("player", true, CommandParamType.TARGET),
                CommandParameter.newType("sceneId", false, CommandParamType.STRING)
        });

        this.getCommandParameters().put("change", new CommandParameter[]{
                CommandParameter.newEnum("change", false, new String[]{"change"}),
                CommandParameter.newType("sceneId", false, CommandParamType.STRING),
                CommandParameter.newType("player", true, CommandParamType.TARGET)
        });

        // Initialize globals
        this.globals = JsePlatform.standardGlobals();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (args.length == 3) {
            switch (args[0]) {
                case "open":
                    Player player = Server.getInstance().getPlayer(args[1]);
                    if (player == null) {
                        SuperDialogPlugin.getInstance().getLogger().info("玩家" + args[1] + "不在线");
                        return false;
                    }
                    String luaFileName = plugin.getDataFolder() + File.separator + "scenes" + File.separator + args[2] + ".lua";
                    openDialog(player, commandSender, luaFileName);
                    break;
                case "change":
                    // TODO: Implement change dialog functionality
                    return true;
                case "reload":
                    reloadLuaScripts();
                    return true;
                case "help":
                    showHelp(commandSender);
                    return true;
            }
        }
        return false;
    }

    private void openDialog(Player player, CommandSender commandSender, String luaFileName) {
        try {
            LuaValue chunk = globals.load(new FileReader(luaFileName), luaFileName);
            chunk.call();

            LuaValue defaultDialog = globals.get("main");
            LuaValue luaPlayer = CoerceJavaToLua.coerce(player);
            LuaValue luaEntity = CoerceJavaToLua.coerce(commandSender.asEntity());
            globals.set("player", luaPlayer);
            globals.set("entity", luaEntity);

            LuaValue result = defaultDialog.call();
            ScrollingTextDialogHelper.createAndSendDialog(player, commandSender.asEntity(), result, globals);
        } catch (Exception e) {
            plugin.getLogger().error("Failed to open dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void reloadLuaScripts() {
        this.globals = JsePlatform.standardGlobals();
        plugin.getLogger().info("Lua scripts reloaded");
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("SuperDialogCommand Help:");
        sender.sendMessage("/supernpc open <player> <dialog> - Open a dialog for a player");
        sender.sendMessage("/supernpc change <player> <dialog> - Change the dialog for a player");
        sender.sendMessage("/supernpc reload - Reload Lua scripts");
        sender.sendMessage("/supernpc help - Show this help message");
    }
}
