package eu.pixelgamesmc.minecraft.servercore.inventory

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

@Suppress("LeakingThis")
abstract class ClickableInventory: InventoryHolder {

    private val inventory: Inventory
    private val parent: ClickableInventory?

    constructor(
        size: Int,
        title: Component = Component.empty(),
        parent: ClickableInventory? = null
    ) {
        this.inventory = Bukkit.createInventory(this, size, title)
        this.parent = parent
    }

    constructor(
        type: InventoryType,
        title: Component = Component.empty(),
        parent: ClickableInventory? = null
    ) {
        this.inventory = Bukkit.createInventory(this, type, title)
        this.parent = parent
    }

    abstract fun playerClick(
        player: Player,
        slot: Int,
        clickType: ClickType,
        action: InventoryAction,
        isShiftClick: Boolean,
        isRightClick: Boolean,
        isLeftClick: Boolean,
        currentItem: ItemStack?
    ): Boolean

    fun getItem(slot: Int): ItemStack? {
        return inventory.getItem(slot)
    }

    fun setItem(slot: Int, itemStack: ItemStack) {
        inventory.setItem(slot, itemStack)
    }

    fun fillInventory(material: Material, force: Boolean = false) {
        fillInventory(ItemStack(material), force)
    }

    fun fillInventory(itemStack: ItemStack, force: Boolean = false) {
        for (slot in 0 until inventory.size) {
            if (!force) {
                if (inventory.getItem(slot) != null) {
                    continue
                }
            }
            inventory.setItem(slot, itemStack)
        }
    }

    fun clearInventory() {
        for (slot in 0 until inventory.size) {
            inventory.setItem(slot, null)
        }
    }

    override fun getInventory(): Inventory {
        return inventory
    }

    fun openInventory(player: Player) {
        player.openInventory(inventory)
    }
}