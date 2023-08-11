package kabl.veira.Daily;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class DailyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player) {
            VeiraPlayer p = Veira.session.getVeiraPlayer((Player) sender);
            if(args.length == 0){
                if(Veira.debug){
                    if(!(p.getDaily() == null)){
                        sender.sendMessage("[DEBUG]: QuestID: " + p.getDaily().getId());
                        sender.sendMessage("[DEBUG]: test: " + p.getDaily().getTitle());
                    } else {
                        sender.sendMessage("[DEBUG]: Noch keine Quest zugewiesen");
                    }
                }

                sender.sendMessage(Component.text("=== Tägliche Herrausforderung ===").color(NamedTextColor.GOLD));

                if(p.getDaily() == null){
                    try {
                        DailyQuest.givePlayerNewQuest(p);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage("Fehler bei zuweisen einer Daily-Quest @Kabl @Kabl @Kabl >:C");
                        return false;
                    }
                    sender.sendMessage(Component.text("Dir wurde soeben eine neue Herrausforderung zugewiesen:"));
                }

                sender.sendMessage(Component.text(p.getDaily().getTitle()).color(NamedTextColor.LIGHT_PURPLE));
                sender.sendMessage(Component.text(p.getDaily().getDescription()).color(NamedTextColor.GRAY));

                return true;
            }

            if(args[1].equals("complete")){
                if(Veira.session.getActiveQuest(((Player) sender).getUniqueId()).completeQuest()){
                    p.completedQuest();
                } else {
                    sender.sendMessage("Du hast noch nicht alle Bedingungen erfüllt:");
                    sender.sendMessage(Veira.session.getActiveQuest(((Player) sender).getUniqueId()).getRequirements());
                }
            }

            if(args[1].equals("cancel")){

            }

        }
        return false;
    }
}
