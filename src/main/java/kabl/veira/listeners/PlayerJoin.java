package kabl.veira.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Player p = e.getPlayer();
        Gson gson = new Gson();

        if(Veira.debug){
            Veira.log("[Debug]: Player join Event");
        }

        Path playerPath = Path.of(Veira.pluginInstance.veiraPath + "\\" + p.getUniqueId() + ".json");

        if(Files.exists(playerPath)){
            JsonObject result = gson.fromJson(Files.readString(playerPath), JsonObject.class);
            if(result == null){
                p.sendMessage("Irgendwas ist schief gelaufen, bitte logge dich neu ein oder melde dich bei Kabl");
            } else {
                VeiraPlayer v = new VeiraPlayer(p, result);
                Veira.session.addPlayer(p, v);
            }
        } else {
            // New Player has joined
            Bukkit.getServer().sendMessage(Component.text("Willkommen auf dem Server " + p.getName() + "!"));
            Files.createFile(playerPath);
            VeiraPlayer v = new VeiraPlayer(p);
            Veira.session.addPlayer(p, v);
        }
    }

}
