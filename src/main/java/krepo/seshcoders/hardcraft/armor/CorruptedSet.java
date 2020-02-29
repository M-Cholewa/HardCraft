package krepo.seshcoders.hardcraft.armor;

import krepo.seshcoders.hardcraft.utils.Randomize;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class CorruptedSet implements Listener {

    private String[] materials = {"SWORD", "PICKAXE", "HOE", "AXE", "SHOVEL"};


    @EventHandler
    public void onToolsSpawn(CraftItemEvent craftItemEvent) {
        ItemStack item = craftItemEvent.getCurrentItem();
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(item.getType().getMaxDurability() / 4);
        item.setItemMeta((ItemMeta) meta);
    }

    @EventHandler
    public void onItemDamaged(PlayerItemDamageEvent itemDamageEvent) {
        String itemName = itemDamageEvent.getItem().getType().toString();
        for (String material : materials) {
            if (itemName.contains(material)) {
                itemDamageEvent.setDamage(itemDamageEvent.getDamage() * 2);
                return;
            }
        }
        if (Randomize.checkChance(85))
        itemDamageEvent.setDamage(itemDamageEvent.getDamage() * 2);
    }
}
