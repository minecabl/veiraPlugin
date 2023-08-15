package kabl.veira.commands;

import kabl.veira.Veira;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Schizo implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Veira.session.getVeiraPlayer((Player) sender).schizoToggle();
            ((Player) sender).playSound(((Player) sender).getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 10, 1);
        }
        return true;
    }
}
