package me.MnMaxon.PrivateItems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MainListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (Main.hasOwner(e.getItem()) && !e.getPlayer().getName().equals(Main.getOwnerName(e.getItem()))) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getCursor() != null && Main.hasOwner(e.getCursor())
				&& !e.getWhoClicked().getName().equals(Main.getOwnerName(e.getCursor()))
				&& e.getSlotType().equals(SlotType.ARMOR))
			e.setCancelled(true);
		else if (e.isShiftClick()
				&& Main.hasOwner(e.getCurrentItem())
				&& !e.getWhoClicked().getName().equals(Main.getOwnerName(e.getCurrentItem()))
				&& (e.getInventory().getType().equals(InventoryType.PLAYER) || e.getInventory().getType()
						.equals(InventoryType.CRAFTING))) {
			String name = e.getCurrentItem().getType().name().toLowerCase();
			if (name.contains("boots") || name.contains("helm") || name.contains("legging") || name.contains("boot")
					|| name.contains("cap") || name.contains("tunic") || name.contains("chestpl"))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getOldCursor() != null
				&& Main.hasOwner(e.getOldCursor())
				&& !e.getWhoClicked().getName().equals(Main.getOwnerName(e.getOldCursor()))
				&& (e.getInventory().getType().equals(InventoryType.PLAYER) || e.getInventory().getType()
						.equals(InventoryType.CRAFTING))
				&& (e.getInventorySlots().contains(36) || e.getInventorySlots().contains(37)
						|| e.getInventorySlots().contains(38) || e.getInventorySlots().contains(39)))
			e.setCancelled(true);
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			ItemStack is = ((Player) e.getDamager()).getItemInHand();
			if (Main.hasOwner(is) && !((Player) e.getDamager()).getName().equals(Main.getOwnerName(is)))
				e.setCancelled(true);
		}
	}
}
