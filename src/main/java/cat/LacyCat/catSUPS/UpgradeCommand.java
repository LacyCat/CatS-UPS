package cat.LacyCat.catSUPS;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UpgradeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender instanceof org.bukkit.entity.Player) {
            ((org.bukkit.entity.Player) commandSender).openInventory(UpgradeInventory.createUpgradeGUI());
            return true;
        }
        else {
            return false;
        }
    }
}
