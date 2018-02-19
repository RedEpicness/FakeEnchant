package me.redepicness.template;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.List;

public class main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(p.isSneaking()
                            && p.getInventory().getChestplate() != null
                            && p.getInventory().getChestplate().getType().equals(Material.IRON_CHESTPLATE)) {
                        List<String> lore = p.getInventory().getChestplate().getItemMeta().getLore();
                        Location launch = p.getLocation().clone();
                        launch.setPitch(Float.valueOf(lore.get(0).split(" ")[1])*-1);
                        p.setVelocity(p.getVelocity().add(launch.getDirection().multiply(Double.valueOf(lore.get(1).split(" ")[1])/10)));
                    }
                }
            }
        }, 2, 2);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equals("fe")) return false;
        ItemStack enchant = ((Player) sender).getItemInHand();
        enchant = addGlow(enchant);
        ItemMeta meta = enchant.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + args[0] + " " + args[1]);
        meta.setLore(lore);
        enchant.setItemMeta(meta);
        ((Player) sender).getInventory().addItem(enchant);
        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        e.getPlayer().setFallDistance(((float) (NumberConversions.square(e.getPlayer().getVelocity().getY()) * 9f)-2));
    }

    public static ItemStack addGlow(ItemStack item){
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

}
