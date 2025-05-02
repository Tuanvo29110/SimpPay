package org.simpmc.simppay.config.types.menu.data;

import de.exlll.configlib.Configuration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Configuration
@Builder
public class DisplayItem {
    private Material material;
    private String name;
    private List<String> lores;
    private int customModelData;
    private int amount;
    private RoleType role;

    public org.bukkit.inventory.ItemStack getItemStack() {
        org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(material, amount);
        org.bukkit.inventory.meta.ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            if (name != null && !name.isEmpty()) {
                meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', name));
            }
            if (lores != null && !lores.isEmpty()) {
                meta.setLore(lores.stream()
                        .map(lore -> org.bukkit.ChatColor.translateAlternateColorCodes('&', lore))
                        .toList());
            }
            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}