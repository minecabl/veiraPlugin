package kabl.veira.commands;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length != 2) {
            sender.sendMessage("Falsche Syntax: /pay <target> <amount>");
            return true;
        }

        String playerName = args[0];
        Player target = Bukkit.getServer().getPlayer(playerName);

        if (target == null) {
            sender.sendMessage("Spieler nicht gefunden");
        }

        long amount = Long.parseLong(args[1]);

        if (amount <= 0 || amount >= 999999) {
            sender.sendMessage("Bitte gib eine Menge zwischen 1 und 999999 an");
            return true;
        }

        if (sender instanceof ConsoleCommandSender) {
            try {
                Veira.session.getVeiraPlayer(target).depositDiamond(amount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            VeiraPlayer vSelf = Veira.session.getVeiraPlayer((Player) sender);
            VeiraPlayer vTarget = Veira.session.getVeiraPlayer(target);

            if (vSelf.getDiamonds() < amount) {
                sender.sendMessage("Du hast nicht genügend Diamanten dafür!");
                return true;
            } else {
                try {
                    vTarget.addDiamonds(amount);
                    vSelf.removeDiamonds(amount);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return true;
    }
}
