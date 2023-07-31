package kabl.veira.Daily;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DailyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            VeiraPlayer p = Veira.session.getVeiraPlayer((Player) sender);

            if(Veira.debug){
                sender.sendMessage("QuestID: " + p.getDailyID());
            }


            return true;
        }
        return false;
    }
}
