package kabl.veira.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import kabl.veira.Veira;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e){
        if(e.message().toString().toLowerCase().contains("nigger") || e.message().toString().toLowerCase().contains("nigga") ||e.message().toString().toLowerCase().contains("neger")){
            Veira.session.getVeiraPlayer(e.getPlayer()).saidNword();
        }
    }
}
