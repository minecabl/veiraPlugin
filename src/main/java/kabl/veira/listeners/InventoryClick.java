package kabl.veira.listeners;

import kabl.veira.Veira;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class InventoryClick implements Listener {
    @EventHandler
    public void onInventoryClicked(InventoryClickEvent e){

        // Chooser-Menu --------------------------------------------------
        if (e.getView().title().equals(Component.text("Täglicher Einlogbonus").color(NamedTextColor.YELLOW)) && e.getClickedInventory() == e.getView().getTopInventory()){
            e.setCancelled(true);
            if(e.getCurrentItem() == null){
                return;
            }

            VeiraPlayer vp = Veira.session.getVeiraPlayer(((Player) e.getWhoClicked()).getPlayer());

            String wasdasteht = ((TextComponent)e.getCurrentItem().getItemMeta().displayName()).content();

            if (wasdasteht.equals("")) {
                return;
            }

            if (wasdasteht.split(" ")[1].equals(""+(vp.getLoginStreak()))) {
                e.getWhoClicked().closeInventory();
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 10);
                vp.giveLoginReward();
            } else {
                e.getWhoClicked().sendMessage(Component.text("Du kannst nur die Belohnung für den aktuellen Tag einsammeln").color(NamedTextColor.RED));
            }
        }

    }
}
