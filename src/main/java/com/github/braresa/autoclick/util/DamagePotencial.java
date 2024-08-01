package com.github.braresa.autoclick.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DamagePotencial {
    public static double getDamagePotencial(Player player) {
        double baseDamage = 1.0;
        Map<Material, Double> swords = new HashMap<Material, Double>();

        swords.put(Material.WOOD_SWORD, 4.0);
        swords.put(Material.GOLD_SWORD, 4.0);
        swords.put(Material.STONE_SWORD, 5.0);
        swords.put(Material.IRON_SWORD, 6.0);
        swords.put(Material.DIAMOND_SWORD, 7.0);

        ItemStack heldItem = player.getInventory().getItemInHand();

        for(Map.Entry<Material, Double> entryMap : swords.entrySet()) {
            Material material = entryMap.getKey();
            Double damage = entryMap.getValue();
            if(heldItem.getType() == material) {
                baseDamage = damage;
                if(heldItem.containsEnchantment(Enchantment.DAMAGE_ALL)) {
                    int sharpnessLevel = heldItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                    baseDamage += sharpnessLevel * 1.25;
                }
                break;
            }
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            if(effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
             int strengthLevel = effect.getAmplifier();
             baseDamage += (strengthLevel + 1) * 1.3;
            } else if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
                int weaknessLevel = effect.getAmplifier();
                baseDamage -= (weaknessLevel + 1) * 4.0;
            }
        }

        return baseDamage;
    }
}
