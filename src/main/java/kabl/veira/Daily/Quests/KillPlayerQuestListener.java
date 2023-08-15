package kabl.veira.Daily.Quests;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.io.IOException;

public class KillPlayerQuestListener implements Listener {
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) throws IOException {
        VeiraPlayer killer = Veira.session.getVeiraPlayer(event.getPlayer().getKiller());

        if (killer.getDaily().getId() == 1) {
            if (killer.getDaily().getRequirements().toString().contains(event.getPlayer().getName())) {
                killer.completedQuest();
            }
        }
    }
}
