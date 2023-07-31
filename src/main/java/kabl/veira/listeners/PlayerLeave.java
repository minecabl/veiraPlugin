package kabl.veira.listeners;

import com.google.gson.JsonObject;
import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) throws IOException {
        Player p = e.getPlayer();

        if(Veira.debug){
            Veira.log("Debug: PlayerLeaveEvent");
        }

        Path playerPath = Paths.get(Veira.pluginInstance.veiraPath + "\\" + p.getUniqueId() + ".json");

        JsonObject v = Veira.session.getVeiraPlayer(p).getPlayerAsJson();

        Files.write(playerPath, Collections.singleton(v.toString()));

    }
}
