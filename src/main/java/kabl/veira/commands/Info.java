package kabl.veira.commands;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Info implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player infoPlayer;
        if(args.length == 0){
            if(sender instanceof ConsoleCommandSender){
                sender.sendMessage("Bitte Namen angeben, die Konsole hat keine Stats");
                return false;
            }
            infoPlayer = (Player) sender;
        } else {
            infoPlayer = Veira.pluginInstance.getServer().getPlayer(args[0]);
        }

        if(infoPlayer == null){
            sender.sendMessage("Spieler ist gerade Offline, noch unsupportet");
            return false;
        }

        VeiraPlayer veiraPlayer = Veira.session.getVeiraPlayer(infoPlayer);

        sender.sendMessage(Component.text("=== Statistiken für " + infoPlayer.getName() + " ===").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("Spielzeit: " + veiraPlayer.getPlaytime() / 3600.0 + " Stunden"));
        sender.sendMessage(Component.text("Diamanten: " + veiraPlayer.getDiamonds()));
        sender.sendMessage(Component.text("Kills: " + infoPlayer.getStatistic(Statistic.PLAYER_KILLS)));
        sender.sendMessage(Component.text("Tode: " + infoPlayer.getStatistic(Statistic.DEATHS)));
        sender.sendMessage(Component.text("K/D: " + String.format("%.2f", infoPlayer.getStatistic(Statistic.PLAYER_KILLS) * 1.0 / infoPlayer.getStatistic(Statistic.DEATHS))));

        if(Veira.debug){
            sender.sendMessage("Quest: " + veiraPlayer.getDaily().getId());
            sender.sendMessage("QuestTag: " + veiraPlayer.getDailyDate());
            sender.sendMessage("Nword: " + veiraPlayer.getNwordcount());
        }


        return true;
    }
}
