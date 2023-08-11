package kabl.veira.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import kabl.veira.Veira;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e){
        if(e.message().toString().toLowerCase().contains("nigger") || e.message().toString().toLowerCase().contains("nigga") ||e.message().toString().toLowerCase().contains("neger")){
            e.setCancelled(true);
            Veira.session.getVeiraPlayer(e.getPlayer()).saidNword();
        }
    }
}
