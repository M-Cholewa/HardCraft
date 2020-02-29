package krepo.seshcoders.hardcraft.mobs;

import krepo.seshcoders.hardcraft.utils.Randomize;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MobsStats implements Listener {

    private float multiplier = 1;
    private boolean huntModeOn = false;
    private JavaPlugin hardCraft;
    private List<ItemStack> dropList;

    public MobsStats(JavaPlugin hardCraft) {
        this.hardCraft = hardCraft;
        dropList = new ArrayList<>();
    }

    @EventHandler
    public void monsterSpawnedEvent(CreatureSpawnEvent mobSpawn) {
        //if its not a monster then just skip
        if (!(mobSpawn.getEntity() instanceof Monster)) return;
        if (huntModeOn)
            mobSpawn.getEntity().setCustomName(ChatColor.RED + "HORDA");

        switch (mobSpawn.getEntity().getType()) {
            case CREEPER:
                Creeper creeper = (Creeper) mobSpawn.getEntity();
                creeper.setExplosionRadius(15);
                setMobMovementSpeed(creeper, 1.2f);
                if (huntModeOn && Randomize.checkChance(20))
                creeper.setPowered(true);
                break;

            case ZOMBIE:
                Zombie zombie = (Zombie) mobSpawn.getEntity();
                zombie.setCanPickupItems(true);
                setMobDamage(zombie, 1.2f);
                setMobHealth(zombie, 1.1f);
                if (zombie.isBaby()) break;
                setMobHealth(zombie, 1.25f);
                setMobMovementSpeed(zombie, 1.35f);
                break;

            case SKELETON:
                Skeleton skeleton = (Skeleton) mobSpawn.getEntity();
                skeleton.setCanPickupItems(true);
                setMobMovementSpeed(skeleton, 1.2f);
                break;

            case SPIDER:
                Spider spider = (Spider) mobSpawn.getEntity();
                setMobDamage(spider, 1.3f);
                setMobMovementSpeed(spider, 1.1f);
                break;

            case BLAZE:
                Blaze blaze = (Blaze) mobSpawn.getEntity();
                setMobDamage(blaze, 1.3f);
                setMobHealth(blaze, 1.3f);
                setMobMovementSpeed(blaze, 1.2f);
                break;

            case ENDERMAN:
                Enderman enderman = (Enderman) mobSpawn.getEntity();
                setMobDamage(enderman, 1.25f);
                setMobHealth(enderman, 1.25f);
                break;

            case ENDER_DRAGON:
                EnderDragon dragon = (EnderDragon) mobSpawn.getEntity();
                dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                        .setBaseValue(dragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 1.5 * multiplier);

                dragon.getAttribute(Attribute.GENERIC_ARMOR)
                        .setBaseValue(dragon.getAttribute(Attribute.GENERIC_ARMOR).getValue() * 1.5 * multiplier);

                dragon.getAttribute(Attribute.GENERIC_FLYING_SPEED)
                        .setBaseValue(dragon.getAttribute(Attribute.GENERIC_FLYING_SPEED).getValue() * 1.5 * multiplier);

                dragon.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                        .setBaseValue(dragon.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * 1.7 * multiplier);

                dragon.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                        .setBaseValue(dragon.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue() * 2.3 * multiplier);
                dragon.setHealth(1000);
                break;

            case WITHER:
                Wither wither = (Wither) mobSpawn.getEntity();
                wither.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                        .setBaseValue(wither.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 1.5 * multiplier);
                setMobMovementSpeed(wither, 1.5f);
                setMobDamage(wither, 1.9f);
                break;
        }
    }

    @EventHandler
    public void mobLootModifier(EntityDeathEvent deathEvent) {
        switch (deathEvent.getEntityType()) {
            case ZOMBIE:
                dropList.clear();
                if (deathEvent.getDrops().size() == 0) return;
                dropList.addAll(deathEvent.getDrops()) ;
                deathEvent.getDrops().clear();
                World world = deathEvent.getEntity().getWorld();

                for (ItemStack stack : dropList) {
                    if (stack.getType() == Material.ROTTEN_FLESH) {
                        if(Randomize.checkChance(55)){
                            try {
                                world.dropItemNaturally(deathEvent.getEntity().getLocation(), new ItemStack(Material.ROTTEN_FLESH, 1));
                            }catch (Exception e){
                                System.out.println();
                            }
                        }
                        continue;
                    }
                    world.dropItemNaturally(deathEvent.getEntity().getLocation(), stack);
                }
                break;
        }
    }

    @EventHandler
    public void zombieTargetEvent(EntityTargetEvent targetEvent) {
        if (!(
                (targetEvent.getEntity() instanceof Zombie || targetEvent.getEntity() instanceof Villager)
                        && !targetEvent.isCancelled())) return;
        if (targetEvent.getReason() == EntityTargetEvent.TargetReason.FORGOT_TARGET) {
            Zombie zombie = (Zombie) targetEvent.getEntity();
            for (Entity e : zombie.getNearbyEntities(40, 20, 40)) {
                if (e instanceof Player || e instanceof Villager) {
                    zombie.setTarget((LivingEntity) e);
                    for (Block block : zombie.getLineOfSight(null, 5)) {
                        block.breakNaturally();
                        double x = block.getX();
                        double y = block.getY();
                        double z = block.getZ();
                        Location location = new Location(hardCraft.getServer().getWorld("World"), x, y - 1, z);
                        location.getBlock().breakNaturally();
                    }
                }
            }
        }
    }

    @EventHandler
    public void skeletonDamageModifier(EntityDamageByEntityEvent entityEvent) {
        if (!(entityEvent.getDamager() instanceof Projectile
                && entityEvent.getEntity() instanceof Player)) return;
        Projectile arrow = (Projectile) entityEvent.getDamager();
        if (!(arrow.getShooter() instanceof Skeleton)) return;
        entityEvent.setDamage(entityEvent.getDamage() * 1.2 * multiplier);
    }

    public void setHuntModeOn(boolean huntModeOn) {
        multiplier = huntModeOn ? 1.2f : 1;
        this.huntModeOn = huntModeOn;
    }

    private void setMobDamage(Monster monster, float damageMultiplier) {
        monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
                .setBaseValue(monster.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue() * damageMultiplier * multiplier);
    }

    private void setMobMovementSpeed(Monster monster, float speedMultiplier) {
        monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)
                .setBaseValue(monster.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue() * speedMultiplier * multiplier);
    }

    private void setMobHealth(Monster monster, float healthMultiplier) {
        double maxHp = monster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * healthMultiplier * multiplier;
        monster.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                .setBaseValue(maxHp);
        monster.setHealth(maxHp);
    }
}

