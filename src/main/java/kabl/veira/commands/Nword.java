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

public class Nword implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            Comparator<VeiraPlayer> byNword = Comparator.comparingLong(VeiraPlayer::getNwordcount);
            sender.sendMessage(Component.text("=== Spieler nach N-Wörtern ===").color(NamedTextColor.GOLD));
            int i = 1;
            for(VeiraPlayer player: Veira.session.getTopList(byNword)){
                sender.sendMessage(Component.text(i + ": " + player.getName() + " - " + player.getNwordcount()).color(NamedTextColor.GOLD));
                i++;
            }
        } else {
            Player p = Veira.pluginInstance.getServer().getPlayer(args[0]);
            if(p != null){
                sender.sendMessage(Component.text(p.getName() + " hat " + Veira.session.getVeiraPlayer(p).getNwordcount() + " mal das N-Wort gesagt"));
            } else {
                Comparator<VeiraPlayer> byNword = Comparator.comparingLong(VeiraPlayer::getNwordcount);
                for(VeiraPlayer player: Veira.session.getTopList(byNword)){
                    if(Objects.equals(player.getName(), args[0])){
                        sender.sendMessage(Component.text(player.getName() + " hat " + player.getNwordcount() + " mal das N-Wort gesagt"));
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
