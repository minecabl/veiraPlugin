package kabl.veira.Gamble;

import kabl.veira.Veira;
import kabl.veira.core.InventoryProvider;
import kabl.veira.core.VeiraPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class GambleClickEvent implements Listener {

    @EventHandler
    public void onGambleChooserClicked(InventoryClickEvent e){
        // Chooser-Menu --------------------------------------------------
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
                case CHEST:
                    e.getView().close();
                    e.getWhoClicked().openInventory(InventoryProvider.getLootBoxGUI(Veira.session.getVeiraPlayer((Player) e.getWhoClicked())));
                    break;
                case BARRIER:
                    e.getView().close();
                    break;
                case PAPER:
                    e.getView().close();
                    e.getWhoClicked().openInventory(InventoryProvider.getShopGui(vp));
                    break;
                default:
                    break;
            }
        }

        // Slots -------------------------------------------------------
        if (e.getView().title().equals(Component.text("Eye of Kabl").color(NamedTextColor.YELLOW)) && e.getClickedInventory() == e.getView().getTopInventory()){
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

        // Shop -------------------------------------------

        if (e.getView().title().equals(Component.text("Shop").color(NamedTextColor.YELLOW)) && e.getClickedInventory() == e.getView().getTopInventory()) {
            e.setCancelled(true);
            if(e.getCurrentItem() == null){
                return;
            }

            VeiraPlayer vp = Veira.session.getVeiraPlayer((Player) e.getWhoClicked());

            switch (e.getCurrentItem().getType()) {
                case EMERALD_ORE:
                    if (vp.getDiamonds() > 0) {
                        vp.setDiamonds(vp.getDiamonds() - 1);
                        vp.addKablcoins(100);
                    } else {
                        e.getWhoClicked().sendMessage("Du hast nicht genügend Diamanten, zahle welche mit /deposit ein!");
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 15);
                    }
                    break;
                case EMERALD:
                    if (vp.getDiamonds() > 10) {
                        vp.setDiamonds(vp.getDiamonds() - 10);
                        vp.addKablcoins(1000);
                    } else {
                        e.getWhoClicked().sendMessage("Du hast nicht genügend Diamanten, zahle welche mit /deposit ein!");
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 15);
                    }
                    break;
                case EMERALD_BLOCK:
                    if (vp.getDiamonds() > 100) {
                        vp.setDiamonds(vp.getDiamonds() - 100);
                        vp.addKablcoins(10000);
                    } else {
                        e.getWhoClicked().sendMessage("Du hast nicht genügend Diamanten, zahle welche mit /deposit ein!");
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 10, 15);
                    }
                    break;
                case MAP:
                    if (vp.getKablcoins() > 1337) {
                        e.getWhoClicked().closeInventory();
                        vp.removeKablcoins(1337);
                        e.getWhoClicked().sendMessage("Bitte sende eine URL zu deinem Bild in den Chat:");
                        vp.sendingMessage();
                    } else {
                        e.getWhoClicked().sendMessage("Du hast nicht genügend Kabl-Coins dafür :(");
                    }
                    break;

                case DIAMOND_BLOCK:
                    if (vp.getKablcoins() > 1000) {
                        for(int i = 0; i < 36; i++){
                            if (e.getWhoClicked().getInventory().getItem(i) == null) {
                                vp.removeKablcoins(1000);
                                e.getWhoClicked().sendMessage("Ein Diamantblock wurde in dein Inventar gelegt");
                                e.getWhoClicked().getInventory().setItem(i, new ItemStack(Material.DIAMOND_BLOCK));
                                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 15);

                                e.getWhoClicked().closeInventory();
                                e.getWhoClicked().openInventory(InventoryProvider.getShopGui(vp));
                                return;
                            }
                        }
                        e.getWhoClicked().sendMessage("Du benötigst einen freien Platz im Inventar");
                    } else {
                        e.getWhoClicked().sendMessage("Du hast nicht genügend Kabl-Coins dafür :(");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
