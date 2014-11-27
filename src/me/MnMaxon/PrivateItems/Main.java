package me.MnMaxon.PrivateItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static String dataFolder;
	public static Main plugin;

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = this.getDataFolder().getAbsolutePath();
		getServer().getPluginManager().registerEvents(new MainListener(), this);
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
			return false;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("UserItemUnlock") || cmd.getName().equalsIgnoreCase("uiUnlock")) {

			if (p.hasPermission("uilock.unlock")
					|| (Main.hasOwner(p.getItemInHand()) && p.getName().equals(Main.getOwnerName(p.getItemInHand())))) {
				ItemStack is = removeOwner(p.getItemInHand());
				p.setItemInHand(is);
				p.updateInventory();
				p.sendMessage(ChatColor.WHITE + "[" + ChatColor.BLUE + "UItemLock" + ChatColor.WHITE + "] "
						+ ChatColor.GREEN + "Owner removed!");
			}

		} else if (cmd.getName().equalsIgnoreCase("UserItemLock") || cmd.getName().equalsIgnoreCase("uiLock")) {

			if (p.hasPermission("uilock.lock")) {
				OfflinePlayer oP;
				if (args.length == 0)
					oP = p;
				else if (args.length == 1) {
					oP = Bukkit.getOfflinePlayer(args[0]);
					if (oP == null) {
						p.sendMessage(ChatColor.WHITE + "[" + ChatColor.BLUE + "UItemLock" + ChatColor.WHITE + "] "
								+ ChatColor.GOLD + args[0] + ChatColor.RED + " could not be found");
						return false;
					}
				} else {
					displayHelp(p);
					return false;
				}
				if (hasOwner(p.getItemInHand())) {
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.BLUE + "UItemLock" + ChatColor.WHITE + "] "
							+ ChatColor.GOLD + getOwnerName(p.getItemInHand()) + ChatColor.RED
							+ " already owns this item");
					return false;
				}
				ItemStack is = setOwner(p.getItemInHand(), oP.getName());
				if (is == null)
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.BLUE + "UItemLock" + ChatColor.WHITE + "] "
							+ ChatColor.RED + "Owner could not be set");
				else {
					p.setItemInHand(is);
					p.updateInventory();
					p.sendMessage(ChatColor.WHITE + "[" + ChatColor.BLUE + "UItemLock" + ChatColor.WHITE + "] "
							+ ChatColor.RED + "Owner was set to " + oP.getName());
				}
			}

		} else
			displayHelp(p);
		return false;
	}

	private void displayHelp(CommandSender s) {
		s.sendMessage(ChatColor.AQUA + "========= UItemLock =========");
		s.sendMessage(ChatColor.DARK_PURPLE + "/UiLock (Player)" + ChatColor.GOLD + " - " + ChatColor.DARK_AQUA
				+ "Locks an item");
		s.sendMessage(ChatColor.DARK_PURPLE + "/UiUnlock" + ChatColor.GOLD + " - " + ChatColor.DARK_AQUA
				+ "Unlocks an item");
	}

	public static String getOwnerName(ItemStack is) {
		if (is != null && is.hasItemMeta() && is.getItemMeta().hasLore())
			for (String message : is.getItemMeta().getLore())
				if (ChatColor.stripColor(message).contains("Owned by "))
					return ChatColor.stripColor(message).replace("Owned by ", "");
		return null;
	}

	public static boolean hasOwner(ItemStack is) {
		if (getOwnerName(is) == null)
			return false;
		else
			return true;
	}

	public static ItemStack setOwner(ItemStack is, String name) {
		if (is != null && !is.getType().equals(Material.AIR)) {
			ItemMeta im = is.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (is.getItemMeta().hasLore())
				lore = is.getItemMeta().getLore();
			lore.add(ChatColor.GRAY + "Owned by " + name);
			im.setLore(lore);
			is.setItemMeta(im);
			return is;
		}
		return null;
	}

	public static ItemStack removeOwner(ItemStack is) {
		if (is != null) {
			ItemMeta im = is.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (is.getItemMeta().hasLore())
				lore = is.getItemMeta().getLore();
			String removeMessage = null;
			for (String message : lore)
				if (ChatColor.stripColor(message).contains("Owned by "))
					removeMessage = message;
			if (removeMessage != null)
				lore.remove(removeMessage);
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}
}