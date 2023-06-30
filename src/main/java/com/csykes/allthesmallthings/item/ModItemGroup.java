package com.csykes.allthesmallthings.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup ALLTHESMALLTHINGS = new ItemGroup("AllTheSmallThingsTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.SAWBLADE_ITEM.get());
        }
    };

}
