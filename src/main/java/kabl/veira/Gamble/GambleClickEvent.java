package kabl.veira.Gamble;

import kabl.veira.Veira;
import kabl.veira.core.InventoryProvider;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GambleClickEvent implements Listener {

    @EventHandler
    public void onGambleChooserClicked(InventoryClickEvent e){
        // Chooser-Menu
        if (e.getView().title().equals(Component.text("Wähle ein Spiel").color(NamedTextColor.LIGHT_PURPLE)) && e.getClickedInventory() == e.getView().getTopInventory()){
            e.setCancelled(true);
            if(e.getCurrentItem() == null){
                return;
            }
            VeiraPlayer vp = Veira.session.getVeiraPlayer(((Player) e.getWhoClicked()).getPlayer());
            switch (e.getCurrentItem().getType()){
                case CHISELED_SANDSTONE:
                    e.getView().close();
                    e.getWhoClicked().openInventory(InventoryProvider.getGambleSlotsGUI((Player) e.getWhoClicked()));
                    vp.setSlotsGame(new SlotsGame((Player) e.getWhoClicked()));
                    break;
                case BARRIER:
                    e.getView().close();
                    break;
                case PAPER:
                    if (vp.getDiamonds() > 0) {
                        vp.setDiamonds(vp.getDiamonds() - 1);
                        vp.addKablcoins(100);
                        e.getWhoClicked().sendMessage("+100 Kablcoins");
                    } else {
                        e.getWhoClicked().sendMessage("Du hast nicht genügend Diamanten, zahle welche mit /deposit ein!");
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 15);
                    }
                    break;
                default:
                    break;
            }
        }

        // Slots
        if(e.getView().title().equals(Component.text("Eye of Kabl").color(NamedTextColor.YELLOW)) && e.getClickedInventory() == e.getView().getTopInventory()){
            e.setCancelled(true);
            if(e.getCurrentItem() == null){
                return;
            }

            VeiraPlayer vp = Veira.session.getVeiraPlayer((Player) e.getWhoClicked());
            SlotsGame game = vp.getSlotsGame();

            if (game.isSpinning()){
                return;
            }

            switch (e.getCurrentItem().getType()){
                case BARRIER:
                    e.getView().close();
                    e.getWhoClicked().openInventory(InventoryProvider.getGambleChooseGUI((Player) e.getWhoClicked()));
                    break;
                case DIAMOND_ORE:
                    game.clickRows();
                    break;
                case DIAMOND:
                    game.clickPrice();
                    break;
                case LIME_SHULKER_BOX:
                    game.spin();
                    break;
                default:
                    break;
            }
        }

    }
}
