package kabl.veira.commands;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Withdraw implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof ConsoleCommandSender){
            return false;
        }

        Player p = (Player) sender;
        VeiraPlayer v = Veira.session.getVeiraPlayer(p);

        try {
            if(args.length == 0){
                v.withdrawDiamond(9999);
            } else {
                v.withdrawDiamond(Long.parseLong(args[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
