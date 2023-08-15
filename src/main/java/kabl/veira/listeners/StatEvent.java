package kabl.veira.listeners;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;


public class StatEvent implements Listener {
    @EventHandler
    public void onPlayerKillMob(EntityDeathEvent e){
        if(e.getEntity().getKiller() != null){
            int exp, max, min;
            Component msg = Component.text("[Stimmen] ").color(TextColor.color(0, 200, 0));

            switch(e.getEntity().getName().toUpperCase()){
                case "CREEPER":
                case "ENDERMITE":
                case "PILLAGER":
                case "STRAY":
                    max = 200; min = 150; break;
                case "ZOMBIE":
                case "DROWNED":
                case "ZOMBIE_VILLAGER":
                case "HUSK":
                case "SPIDER":
                    max = 100; min = 50; break;
                case "SKELETON": max = 150; min = 100; break;
                case "WITHER_SKELETON":
                case "PHANTOM":
                case "SHULKER":
                case "WITCH":
                    max = 300; min = 250; break;
                case "BLAZE": max = 250; min = 200; break;
                case "ELDER_GUARDIAN": max = 1500; min = 1000; break;
                case "ENDERMAN":
                case "GUADRIAN":
                case "HOGLIN":
                case "ZOGLIN":
                    max = 400; min = 300; break;
                case "ENDER_DRAGON": max = 20000; min = 17500; break;
                case "EVOKER": max = 1000; min = 500; break;
                case "GHAST":
                case "RAVAGER":
                case "VINDICATOR":
                    max = 750; min = 500; break;
                case "MAGMA_CUBE":
                case "SILVERFISH":
                case "SLIME":
                    max = 50; min = 30; break;
                case "PIGLIN_BRUTE": max = 500; min = 400; break;
                case "WARDEN": max = 5000; min = 4000; break;

                case "PIG":
                case "COW":
                case "CHICKEN":
                case "SQUID":
                case "HORSE":
                case "SHEEP":
                case "CAT":
                case "DONKEY":
                case "RABBIT":
                case "WOLF":
                case "PANDA":
                case "BEE":
                    Veira.session.getVeiraPlayer(e.getEntity().getKiller()).incrementAnimalsKilled();
                    return;
                default: return;
            }

            if (Veira.session.getVeiraPlayer(e.getEntity().getKiller()).isSchizo()) {
                exp = (int)Math.floor(Math.random()*(max-min+1)+min);
                msg = msg.append(Component.text("+"+exp).color(TextColor.color(255, 255, 255)));
                e.getEntity().getKiller().sendMessage(msg);
            }

            Veira.session.getVeiraPlayer(e.getEntity().getKiller()).incrementMonstersKilled();
        }
    }

    @EventHandler
    public void onPlayerDigBlock(BlockBreakEvent e){
        if(e.getPlayer() == null) return;

        Veira.session.getVeiraPlayer(e.getPlayer()).incrementBlocksMined();
    }

    @EventHandler
    public void onPlayerPlaceAnything(BlockPlaceEvent e){
        if(e.getPlayer() != null){
            Veira.session.getVeiraPlayer(e.getPlayer()).incrementBlocksPlaced();
        }
    }
}
