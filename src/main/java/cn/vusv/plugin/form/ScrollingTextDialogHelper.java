package cn.vusv.plugin.form;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.element.ElementDialogButton;
import cn.nukkit.form.window.FormWindowDialog;
import cn.nukkit.form.window.ScrollingTextDialog;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class ScrollingTextDialogHelper {

    public static void createAndSendDialog(Player player, Entity entity, LuaValue result, Globals globals) {
        LuaTable resultTable = result.checktable();
        String title = resultTable.get(1).tojstring();
        String description = resultTable.get(2).tojstring();
        LuaTable optionsTable = resultTable.get(3).checktable();
        LuaTable actionsTable = resultTable.get(4).checktable();

        FormWindowDialog dialog = new FormWindowDialog(title, description, entity);

        for (int i = 1; i <= optionsTable.length(); i++) {
            dialog.addButton(optionsTable.get(i).tojstring());
        }

        dialog.addHandler((player_, response) -> {
            int index = dialog.getButtons().stream()
                    .map(ElementDialogButton::getText)
                    .toList()
                    .indexOf(response.getClickedButton().getText());

            String actionName = actionsTable.get(index + 1).tojstring();
            LuaValue action = globals.get(actionName);
            if (!action.isnil()) {

                globals.set("player", CoerceJavaToLua.coerce(player));
                globals.set("entity", CoerceJavaToLua.coerce(entity));
                LuaValue actionResult = action.call();
                if (!actionName.equals("close") && !actionResult.isnil()) {
                    createAndSendDialog(player, entity, actionResult, globals);
                }
            }
        });

        ScrollingTextDialog form = new ScrollingTextDialog(player, dialog, 1);
        form.send(player);
    }
}
