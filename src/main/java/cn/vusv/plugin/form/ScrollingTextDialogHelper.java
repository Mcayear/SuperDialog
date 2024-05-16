package cn.vusv.plugin.form;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.element.ElementDialogButton;
import cn.nukkit.form.response.FormResponseDialog;
import cn.nukkit.form.window.FormWindowDialog;
import cn.nukkit.form.window.ScrollingTextDialog;
import cn.nukkit.network.protocol.NPCDialoguePacket;
import cn.vusv.plugin.SuperDialogPlugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.concurrent.atomic.AtomicInteger;

public class ScrollingTextDialogHelper {

    public static void createAndSendDialog(Player player, Entity entity, LuaValue result, Globals globals) {
        LuaTable resultTable = result.checktable();
        String title = resultTable.get(1).tojstring();
        String description = resultTable.get(2).tojstring();
        LuaTable optionsTable = resultTable.get(3).checktable();
        LuaTable actionsTable = resultTable.get(4).checktable();

        FormWindowDialog dialog = new FormWindowDialog(title, description, entity);

        for (int i = 1; i <= optionsTable.length(); i++) {
            String text = optionsTable.get(i).tojstring();
            dialog.addButton(new ElementDialogButton(text, text, null, ElementDialogButton.Mode.BUTTON_MODE));
        }

        dialog.addHandler((player_, response) -> {
            if (!response.getRequestType().name().equals("EXECUTE_ACTION")) return;
            int index = dialog.getButtons().stream()
                    .map(ElementDialogButton::getText)
                    .toList()
                    .indexOf(response.getClickedButton().getText());

            String actionName = actionsTable.get(index + 1).tojstring();
            LuaValue action = globals.get(actionName);
            close(player, response);

            Server.getInstance().getScheduler().scheduleDelayedTask(SuperDialogPlugin.getInstance(), () -> {
                if (!action.isnil()) {
                    globals.set("player", CoerceJavaToLua.coerce(player));
                    globals.set("entity", CoerceJavaToLua.coerce(entity));
                    LuaValue actionResult = action.call();
                    if (!actionName.equals("close") && !actionResult.isnil()) {

                        createAndSendDialog(player, entity, actionResult, globals);
                    } else {
                        LuaValue closeAction = globals.get("close");
                        if (!closeAction.isnil()) {
                            LuaValue closeActionResult = closeAction.call();
                        }
                    }
                }
            }, 7);
        });

        //ScrollingTextDialog form = new ScrollingTextDialog(player, dialog, 1);
        dialog.send(player);
    }
    public static void close(Player player, FormResponseDialog response) {
        NPCDialoguePacket closeWindowPacket = new NPCDialoguePacket();
        closeWindowPacket.setUniqueEntityId(response.getEntityRuntimeId());
        closeWindowPacket.setAction(NPCDialoguePacket.Action.CLOSE);
        closeWindowPacket.setSceneName(response.getSceneName());
        player.dataPacket(closeWindowPacket);
    }
}
