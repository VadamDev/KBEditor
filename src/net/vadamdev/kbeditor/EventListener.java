package net.vadamdev.kbeditor;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;
import net.vadamdev.kbeditor.utils.KBInfo;
import net.vadamdev.viaapi.tools.packet.Reflection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class EventListener implements Listener {
    private float meleeHorizontal, meleeVertical, speedHorizontal, speedVertical;

    public EventListener() {
        KBInfo kbInfo = Main.instance.getKbInfo();

        meleeHorizontal = kbInfo.getMeleeHorizontal();
        meleeVertical = kbInfo.getMeleeVertical();

        speedHorizontal = kbInfo.getSpeedHorizontal();
        speedVertical = kbInfo.getSpeedVertical();
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        EntityDamageEvent lastDamage = player.getLastDamageCause();

        if(lastDamage == null || !(lastDamage instanceof EntityDamageByEntityEvent)) return;

        if(((EntityDamageByEntityEvent) lastDamage).getDamager() instanceof Player) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if(isUsable(event)) {
            Player victim = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            double sprintMultiplier = damager.isSprinting() ? 0.8D : 0.5D;
            double kbMultiplier = (damager.getItemInHand() == null) ? 0 : damager.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) * 0.2;
            double yMultiplier = victim.isOnGround() ? 1 : 0.5D;

            Vector kb = victim.getLocation().toVector().subtract(damager.getLocation().toVector()).normalize();

            if(!damager.hasPotionEffect(PotionEffectType.SPEED)) {
                kb.setX((kb.getX() * sprintMultiplier + kbMultiplier) * meleeHorizontal);
                kb.setY(0.35D * yMultiplier * meleeVertical);
                kb.setZ((kb.getZ() * sprintMultiplier + kbMultiplier) * meleeHorizontal);
            }else {
                kb.setX((kb.getX() * sprintMultiplier + kbMultiplier) * speedHorizontal);
                kb.setY(0.35D * yMultiplier * speedVertical);
                kb.setZ((kb.getZ() * sprintMultiplier + kbMultiplier) * speedHorizontal);
            }

            Reflection.sendPacket(victim, new PacketPlayOutEntityVelocity(victim.getEntityId(), kb.getX(), kb.getY(), kb.getZ()));
        }
    }

    private boolean isUsable(EntityDamageByEntityEvent event) {
        return !event.isCancelled() && event.getEntity() instanceof Player && event.getDamager() instanceof Player /*&& ((Player) event.getEntity()).getNoDamageTicks() > (((Player) event.getEntity()).getMaximumNoDamageTicks() / 2)*/;
    }
}
