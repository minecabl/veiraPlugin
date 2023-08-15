package kabl.veira.commands;

import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.getWorld().getName().equals("world")) {
                p.sendMessage(Component.text("Bleibe 5 Sekunden still stehen!").color(NamedTextColor.GOLD));

                final boolean[] moved = {false};
                Location initial = p.getLocation();

                for (int i = 0; i <= 5; i++) {
                    int finalI = i;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Veira.pluginInstance, new Runnable() {
                        @Override
                        public void run() {
                            if (!moved[0]) {
                                if (finalI == 5) {
                                    p.teleport(p.getWorld().getSpawnLocation());
                                    p.sendMessage(Component.text("Du wurdest teleportiert").color(NamedTextColor.GREEN));
                                } else {
                                    if (p.getLocation().getX() != initial.getX() || p.getLocation().getY() != initial.getY() || p.getLocation().getZ() != initial.getZ()) {
                                        p.sendMessage("Du hast dich bewegt!");
                                        moved[0] = true;
                                    } else {
                                        p.sendMessage(5 - finalI + "...");
                                    }
                                }
                            }
                        }
                    }, i * 20);
                }
            }
        }
        return true;
    }
}
