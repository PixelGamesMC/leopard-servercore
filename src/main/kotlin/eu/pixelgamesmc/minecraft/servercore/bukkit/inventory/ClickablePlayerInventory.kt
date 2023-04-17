package eu.pixelgamesmc.minecraft.servercore.bukkit.inventory

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.ItemStack

abstract class ClickablePlayerInventory(
    protected val player: Player,
    size: Int,
    title: Component = Component.empty(),
    parent: ClickableInventory? = null
): ClickableInventory(size, title, parent) {

    override fun playerClick(
        player: Player,
        slot: Int,
        clickType: ClickType,
        action: InventoryAction,
        isShiftClick: Boolean,
        isRightClick: Boolean,
        isLeftClick: Boolean,
        currentItem: ItemStack?
    ): Boolean {
        return playerClick(slot, clickType, action, isShiftClick, isRightClick, isLeftClick, currentItem)
    }

    abstract fun playerClick(
        slot: Int,
        clickType: ClickType,
        action: InventoryAction,
        isShiftClick: Boolean,
        isRightClick: Boolean,
        isLeftClick: Boolean,
        currentItem: ItemStack?
    ): Boolean
}