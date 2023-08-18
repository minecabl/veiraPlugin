package kabl.veira.commands;

import kabl.veira.Veira;
import kabl.veira.core.InventoryProvider;
import kabl.veira.core.VeiraPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class Reward implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            VeiraPlayer vp = Veira.session.getVeiraPlayer((Player) sender);
            Inventory dailyInv = InventoryProvider.getRewardGUI(vp);

            ((Player) sender).openInventory(dailyInv);

            return true;
        }
        return false;
    }
}
