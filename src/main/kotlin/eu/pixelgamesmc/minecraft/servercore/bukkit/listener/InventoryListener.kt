package eu.pixelgamesmc.minecraft.servercore.bukkit.listener

import eu.pixelgamesmc.minecraft.servercore.bukkit.inventory.ClickableInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryListener: Listener {

    @EventHandler
    fun inventoryClick(event: InventoryClickEvent) {
        val whoClicked = event.whoClicked

        if (whoClicked is Player) {
            event.clickedInventory?.let { inventory ->
                val inventoryHolder = inventory.holder
                if (inventoryHolder is ClickableInventory) {
                    val isPickupItem = inventoryHolder.playerClick(
                        whoClicked,
                        event.slot,
                        event.click,
                        event.action,
                        event.isShiftClick,
                        event.isRightClick,
                        event.isLeftClick,
                        event.currentItem
                    )
                    event.isCancelled = isPickupItem
                }
            }
        }
    }
}