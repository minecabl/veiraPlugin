package kabl.veira.listeners;

import com.loohp.imageframe.ImageFrame;
import com.loohp.imageframe.objectholders.ImageMap;
import io.papermc.paper.event.player.AsyncChatEvent;
import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e){
        VeiraPlayer vp = Veira.session.getVeiraPlayer(e.getPlayer());

        if(e.message().toString().toLowerCase().contains("nigger") || e.message().toString().toLowerCase().contains("nigga") ||e.message().toString().toLowerCase().contains("neger")){
            e.setCancelled(true);
            vp.saidNword();
        }

        if (vp.isSendingMessage()) {
            if (this.isValidURL( ((TextComponent)e.message()).content()) ) {
                vp.noLongerSendingMessage();
                Collection<ImageMap> test = ImageFrame.imageMapManager.getMaps();
                int cur = 0;
                for (ImageMap element: test) {
                    if (element.getImageIndex() > cur) {
                        cur = element.getImageIndex();
                    }
                }

                int finalCur = cur;

                Bukkit.getScheduler().scheduleSyncDelayedTask(Veira.pluginInstance, new Runnable() {
                    @Override
                    public void run() {
                        Veira.pluginInstance.getServer().dispatchCommand(Bukkit.getConsoleSender(), "imageframe create " + LocalDateTime.now() +" "+ ((TextComponent)e.message()).content() +" 1 1");
                        e.getPlayer().sendMessage(Component.text("Dein Bild wird in 10 Sekunden in dein Inventar gelegt, bitte mache dort Platz oder das Item wird gelöscht!").color(NamedTextColor.RED));
                    }
                }, 0);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Veira.pluginInstance, new Runnable() {
                    @Override
                    public void run() {
                        ImageMap test = ImageFrame.imageMapManager.getFromMapId(finalCur + 1);
                        if (test == null) {
                            e.getPlayer().sendMessage("Etwas ist schief gelaufen, prüfe bitte deinen Link (imgur funktioniert am besten)");
                            vp.addKablcoins(1337);
                        } else {
                            List<ItemStack> item = test.getMaps(test.getName());
                            for(int i = 0; i < 36; i++){
                                if (e.getPlayer().getInventory().getItem(i) == null) {
                                    e.getPlayer().getInventory().setItem(i, item.get(0));
                                    return;
                                }
                            }
                        }
                    }
                }, 200);
            } else {
                e.getPlayer().sendMessage("Link ist nicht valide, versuche es erneut");
            }

        }
    }

    boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
