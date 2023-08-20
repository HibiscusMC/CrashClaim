package net.crashcraft.crashclaim.listeners;

import com.destroystokyo.paper.event.entity.ThrownEggHatchEvent;
import io.papermc.paper.event.player.PlayerOpenSignEvent;
import net.crashcraft.crashclaim.data.ClaimDataManager;
import net.crashcraft.crashclaim.localization.Localization;
import net.crashcraft.crashclaim.permissions.PermissionHelper;
import net.crashcraft.crashclaim.permissions.PermissionRoute;
import net.crashcraft.crashclaim.visualize.VisualizationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PaperListener implements Listener {
    private final PermissionHelper helper;
    private final VisualizationManager visuals;

    public PaperListener(ClaimDataManager manager, VisualizationManager visuals){
        this.visuals = visuals;
        this.helper = PermissionHelper.getPermissionHelper();
    }

    @EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onThrownEggHatchEvent(ThrownEggHatchEvent e){
        if (!e.isHatching()){
            return;
        }

        if (e.getEgg().getShooter() instanceof Player player) {
            if (!helper.hasPermission(player.getUniqueId(), e.getEgg().getLocation(), PermissionRoute.ENTITIES)){
                e.setHatching(false);
                visuals.sendAlert(player, Localization.ALERT__NO_PERMISSIONS__ENTITIES.getMessage(player));
            }
        } else {
            if (!helper.hasPermission(e.getEgg().getLocation(), PermissionRoute.ENTITIES)){
                e.setHatching(false);
            }
        }
    }

    @EventHandler
    public void onPlayerSignOpen(PlayerOpenSignEvent event) {
        Player player = event.getPlayer();
        if (!helper.hasPermission(player.getUniqueId(), event.getSign().getLocation(), PermissionRoute.INTERACTIONS)){
            event.setCancelled(true);
            visuals.sendAlert(player, Localization.ALERT__NO_PERMISSIONS__INTERACTION.getMessage(player));
        }
    }
}
