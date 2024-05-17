package cn.vusv.plugin.superdialog.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.vusv.plugin.superdialog.SuperDialogPlugin;
import cn.vusv.plugin.superdialog.config.LuaShareVariable;
import cn.vusv.plugin.superdialog.config.LuaToolsVariable;
import cn.vusv.plugin.superdialog.config.PlayerConfig;
import cn.vusv.plugin.superdialog.form.ScrollingTextDialogHelper;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

import static cn.vusv.plugin.superdialog.SuperDialogPlugin.playerGlobals;
import static cn.vusv.plugin.superdialog.config.PlayerConfig.PlayersMap;

public class SuperDialogCommand extends Command {
    public SuperDialogPlugin plugin = SuperDialogPlugin.getInstance();

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


        this.getCommandParameters().put("value", new CommandParameter[]{
                CommandParameter.newEnum("value", false, new String[]{"value"}),
                CommandParameter.newType("player", false, CommandParamType.TARGET),
                CommandParameter.newEnum("dataType", false, new String[]{"string", "int", "boolean"}),
                CommandParameter.newType("key", false, CommandParamType.STRING),
                CommandParameter.newType("data", true, CommandParamType.STRING),
        });
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args_) {
        List<String> args = Arrays.stream(args_).toList();
        try {
            switch (args.get(0)) {
                case "open":
                    if (commandSender.isPlayer()) {// 执行者只能是：非玩家的实体
                        return false;
                    }
                    Player player = Server.getInstance().getPlayer(args.get(1));
                    if (player == null) {
                        SuperDialogPlugin.getInstance().getLogger().info("玩家" + args.get(1) + "不在线");
                        return false;
                    }
                    String luaFileName = plugin.getDataFolder() + File.separator + "scenes" + File.separator + args.get(2) + ".lua";
                    openDialog(player, commandSender, luaFileName);
                    break;
                case "value":
                    var cfg = new LuaShareVariable(args.get(1));
                    var after = cfg.getStringValue(args.get(3));
                    if (args.size() == 4) {
                        cfg.removeValue(args.get(3));
                        plugin.getLogger().info("删除 " + args.get(1) + " 的 " + args.get(3) + " 参数：" + after);
                        return true;
                    }
                    switch (args.get(2)) {
                        case "string":
                            cfg.setStringValue(args.get(3), args.get(4));
                            break;
                        case "int":
                            cfg.setIntValue(args.get(3), Integer.parseInt(args.get(4)));
                            break;
                        case "boolean":
                            cfg.setBooleanValue(args.get(3), Boolean.parseBoolean(args.get(4)));
                            break;
                    }
                    plugin.getLogger().info("修改 " + args.get(1) + " 的 " + args.get(3) + " 参数：" + after + " to " + args.get(4));
                    return true;
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
        } catch (IndexOutOfBoundsException e) {
            plugin.getLogger().info("请输入正确的参数：" + s);
        }
        return false;
    }

    private void openDialog(Player player, CommandSender commandSender, String luaFileName) {
        if (!playerGlobals.containsKey(player.getName())) {
            // Initialize globals
            playerGlobals.put(player.getName(), JsePlatform.standardGlobals());
        }
        Globals globals = playerGlobals.get(player.getName());
        try {
            LuaValue chunk = globals.load(new FileReader(luaFileName), luaFileName);
            chunk.call();

            LuaValue defaultDialog = globals.get("main");
            LuaValue luaPlayer = CoerceJavaToLua.coerce(player);
            LuaValue luaEntity = CoerceJavaToLua.coerce(commandSender.asEntity());
            globals.set("player", luaPlayer);
            globals.set("entity", luaEntity);
            globals.set("share", CoerceJavaToLua.coerce(new LuaShareVariable(player.getName())));
            globals.set("tools", CoerceJavaToLua.coerce(LuaToolsVariable.class));

            LuaValue result = defaultDialog.call();
            ScrollingTextDialogHelper.createAndSendDialog(player, commandSender.asEntity(), result, globals);
        } catch (Exception e) {
            plugin.getLogger().error("Failed to open dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void reloadLuaScripts() {
        playerGlobals.clear();
        PlayersMap.clear();
        PlayerConfig.init();
        plugin.getLogger().info("Lua scripts reloaded");
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("SuperDialogCommand Help:");
        sender.sendMessage("/supernpc value <player> <typeEnum: string, int, boolean> <data> - Open a dialog for a player");
        sender.sendMessage("/supernpc open <player> <scenes> - Open a dialog for a player");
        sender.sendMessage("/supernpc change <player> <scenes> - Change the dialog for a player");
        sender.sendMessage("/supernpc reload - Reload Lua scripts");
        sender.sendMessage("/supernpc help - Show this help message");
    }
}
