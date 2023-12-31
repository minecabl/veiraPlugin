package kabl.veira.Daily;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDateTime;


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

                if(p.getDaily() == null) {
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
                sender.sendMessage(Component.text("Beschreibung: " + p.getDaily().getDescription()).color(NamedTextColor.GRAY));
                sender.sendMessage(Component.text("Abschluss: " + p.getDaily().getRequirements()).color(NamedTextColor.GRAY));
                sender.sendMessage(Component.text("Belohnung: " + p.getDaily().getReward()).color(NamedTextColor.GRAY));

                sender.sendMessage(p.getDaily().dailyComplete ? Component.text("Status: Abgeschlossen").color(NamedTextColor.GREEN) : Component.text("Status: Offen").color(NamedTextColor.RED));
                return true;
            } else {
                if(args[0].equals("complete") && Veira.session.getActiveQuest(((Player) sender).getUniqueId()) != null){
                    if(Veira.session.getActiveQuest(((Player) sender).getUniqueId()).completeQuest()){
                        try {
                            Veira.pluginInstance.getServer().broadcast(Component.text(sender.getName() + " hat soeben seine Quest " + Veira.session.getActiveQuest(((Player) sender).getUniqueId()).getTitle() + " abgeschlossen!").color(NamedTextColor.LIGHT_PURPLE));
                            p.completedQuest();
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sender.sendMessage("Du hast noch nicht alle Bedingungen erfüllt:");
                        sender.sendMessage(Veira.session.getActiveQuest(((Player) sender).getUniqueId()).getRequirements());
                        return true;
                    }
                }

                if(args[0].equals("reroll") && Veira.session.getActiveQuest(((Player) sender).getUniqueId()) != null){
                    Veira.session.getActiveQuest(((Player) sender).getUniqueId()).reroll();
                    ((Player) sender).playSound(((Player) sender).getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 10, 10);
                    return true;
                }

                if (args[0].equals("cancel") && sender.isOp()) {
                    if (args.length == 3) {
                        Player target = Veira.pluginInstance.getServer().getPlayer(args[1]);
                        if (target != null) {
                            Veira.session.getVeiraPlayer(target).resetDaily();
                            target.sendMessage(Component.text("Deine Daily-Quest wurde gerade zurückgesetzt").color(NamedTextColor.RED));
                        } else {
                            sender.sendMessage("Spieler nicht gefunden");
                        }
                    } else {
                        Veira.session.cancelAllQuests(null);
                    }
                }
            }
        }
        return false;
    }
}
