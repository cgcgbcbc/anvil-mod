package com.cgcgbcbc.mc.mod.anvil;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

/**
 * Created by guangchen on 6/8/16 18:45.
 */
public class AnvilHandler {

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        FMLLog.getLogger().debug("cost: " + event.getCost());
        event.setCost(1);
        ItemStack itemstack = event.getLeft();
//        int i = 0;
//        int k = 0;
        ItemStack itemstack1 = itemstack.copy();
        ItemStack itemstack2 = event.getRight();
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
        boolean flag;

        flag = itemstack2.getItem() == Items.enchanted_book && !Items.enchanted_book.getEnchantments(itemstack2).hasNoTags();

        if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
            int j2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
            if (j2 <= 0)
            {
                event.setOutput(null);
                event.setCost(0);
                return;
            }

            int k2;

            for (k2 = 0; j2 > 0 && k2 < itemstack2.stackSize; ++k2) {
                int l2 = itemstack1.getItemDamage() - j2;
                itemstack1.setItemDamage(l2);
//                ++i;
                j2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
            }

            event.setMaterialCost(k2);
        } else {
            if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                event.setOutput(null);
                event.setCost(0);
                return;
            }

            if (itemstack1.isItemStackDamageable() && !flag) {
                int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
                int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                int k1 = l + j1;
                int l1 = itemstack1.getMaxDamage() - k1;

                if (l1 < 0)
                {
                    l1 = 0;
                }

                if (l1 < itemstack1.getMetadata())
                {
                    itemstack1.setItemDamage(l1);
//                    i += 2;
                }
            }

            Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);

            for (Enchantment enchantment1 : map1.keySet()) {
                if (enchantment1 != null) {
                    int i3 = map.containsKey(enchantment1) ? map.get(enchantment1) : 0;
                    int j3 = map1.get(enchantment1);
                    j3 = i3 == j3 ? j3 + 1 : Math.max(j3, i3);
                    boolean flag1 = enchantment1.canApply(itemstack);

//                    if (this.thePlayer.capabilities.isCreativeMode || itemstack.getItem() == Items.enchanted_book)
//                    {
//                        flag1 = true;
//                    }
                    for (Enchantment enchantment : map.keySet()) {
                        if (enchantment != enchantment1 && !(enchantment1.canApplyTogether(enchantment) && enchantment.canApplyTogether(enchantment1))) {  //Forge BugFix: Let Both enchantments veto being together
                            flag1 = false;
//                            ++i;
                        }
                    }
                    if (flag1) {
                        if (j3 > enchantment1.getMaxLevel()) {
                            j3 = enchantment1.getMaxLevel();
                        }
                        map.put(enchantment1, j3);
//                        int k3 = 0;
//                        switch (enchantment1.getWeight()) {
//                            case COMMON:
//                                k3 = 1;
//                                break;
//                            case UNCOMMON:
//                                k3 = 2;
//                                break;
//                            case RARE:
//                                k3 = 4;
//                                break;
//                            case VERY_RARE:
//                                k3 = 8;
//                        }
//                        if (flag) {
//                            k3 = Math.max(1, k3 / 2);
//                        }

//                        i += k3 * j3;
                    }
                }
            }
        }

        if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2)) itemstack1 = null;

        // Change name won't work
//        if (StringUtils.isBlank(this.repairedItemName)) {
//            if (itemstack.hasDisplayName()) {
//                k = 1;
//                i += k;
//                itemstack1.clearCustomName();
//            }
//        } else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
//            k = 1;
//            i += k;
//            itemstack1.setStackDisplayName(this.repairedItemName);
//        }

//        if (i <= 0) {
//            itemstack1 = null;
//        }

        if (itemstack1 != null) {
            itemstack1.setRepairCost(0);
            EnchantmentHelper.setEnchantments(map, itemstack1);
        }
        event.setOutput(itemstack1);

    }

    @SubscribeEvent
    public void onAnvilRepair(AnvilRepairEvent event) {
        if (!event.getLeft().isItemEnchanted()) {
            event.getEntityPlayer().addExperienceLevel(1);
        }
    }
}
