package kabl.veira.commands;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

public class Playtime implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            Comparator<VeiraPlayer> byPlaytime = Comparator.comparingLong(VeiraPlayer::getPlaytime);

            sender.sendMessage(Component.text("=== Spieler nach Spielzeit ===").color(NamedTextColor.GOLD));
            int i = 1;
            for(VeiraPlayer player: Veira.session.getTopList(byPlaytime)){
                sender.sendMessage(Component.text(i + ": " + player.getName() + " - " + String.format("%.2f", player.getPlaytime() / 3600.0) + " Stunden").color(NamedTextColor.GOLD));
                i++;
            }
        } else {
            Player p = Veira.pluginInstance.getServer().getPlayer(args[0]);
            if(p != null){
                sender.sendMessage(Component.text(p.getName() + " hat " + String.format("%.2f", Veira.session.getVeiraPlayer(p).getPlaytime() / 3600.0) + " Stunden gespielt"));
            } else {
                Comparator<VeiraPlayer> byPlaytime = Comparator.comparingLong(VeiraPlayer::getPlaytime);
                for(VeiraPlayer player: Veira.session.getTopList(byPlaytime)){
                    if(Objects.equals(player.getName(), args[0])){
                        sender.sendMessage(Component.text(player.getName() + " hat " + String.format("%.2f", player.getPlaytime() / 3600.0) + " Stunden gespielt"));
                        return true;
                    }
                }
                sender.sendMessage("Spieler nicht gefunden");
                return false;
            }
        }
        return true;
    }
}

