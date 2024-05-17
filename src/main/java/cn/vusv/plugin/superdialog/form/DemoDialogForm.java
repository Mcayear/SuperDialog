package cn.vusv.plugin.superdialog.form;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.window.FormWindowDialog;

public class DemoDialogForm {
    public static void open(Player player, Entity entity) {
        FormWindowDialog form = new FormWindowDialog("Title", "Description", entity);
        // Add options
        form.addButton("Option 1");
        form.addButton("Option 2");
        // Set submission action
        form.addHandler((player_, response) -> {
            String buttonText = response.getClickedButton().getName(); // Get the clicked button text
            // Handle user-submitted data
        });
        // Show the dialog to the player
        form.send(player);
    }
}