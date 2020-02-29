package krepo.seshcoders.hardcraft.players;

import krepo.seshcoders.hardcraft.utils.Randomize;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayersStats implements Listener {

    @EventHandler
    public void onCropsDrop(BlockBreakEvent breakEvent) {
        if (breakEvent.isCancelled()) return;
        Material cropMaterial = breakEvent.getBlock().getBlockData().getMaterial();
        if (cropMaterial != Material.POTATOES && cropMaterial != Material.CARROTS) return;
        ArrayList<ItemStack> stack = (ArrayList<ItemStack>) breakEvent.getBlock().getDrops();
        if (stack.size() == 1) return;
        breakEvent.setDropItems(false);
        Material dropMaterial = cropMaterial == Material.POTATOES ? Material.POTATO : Material.CARROT;
        if (Randomize.checkChance(35)){
            breakEvent.getBlock().getWorld().dropItemNaturally(breakEvent.getBlock().getLocation(), new ItemStack(dropMaterial, 1));
        }else {
            breakEvent.getBlock().getWorld().dropItemNaturally(breakEvent.getBlock().getLocation(), new ItemStack(dropMaterial, 2));
        }

    }
    @EventHandler
    public void onPlayerSleep(TimeSkipEvent skipEvent){
        if (skipEvent.getSkipReason() != TimeSkipEvent.SkipReason.NIGHT_SKIP) return;
        skipEvent.setCancelled(true);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent){
        if (Randomize.checkChance(45)){
            playerDeathEvent.getDrops().clear();
            playerDeathEvent.setDroppedExp(0);
            playerDeathEvent.setKeepInventory(true);
            playerDeathEvent.setKeepLevel(true);
            playerDeathEvent.getEntity().sendMessage(ChatColor.GREEN+"itemy nie wypadly :>");
        }else {
            playerDeathEvent.getEntity().sendMessage(ChatColor.RED+"itemy wypadly");
        }

    }

    @EventHandler
    public void onPlayerGotHit(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        switch (e.getCause()) {
            case POISON:
                e.setDamage(e.getDamage() * 1.2);
                break;
            case FALL:
                e.setDamage(e.getDamage() * 1.25);
                break;
            case FIRE:
                e.setDamage(e.getDamage() * 1.1);
                break;
            //boss mobs change
            case WITHER:
                e.setDamage(e.getDamage() * 1.8);
                break;
            case DRAGON_BREATH:
                e.setDamage(e.getDamage() * 2);
                break;
        }
    }


}
