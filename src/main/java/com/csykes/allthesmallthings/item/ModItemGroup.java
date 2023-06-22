package com.csykes.allthesmallthings.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ModItemGroup {

    public static final ItemGroup ALLTHESMALLTHINGS = new ItemGroup("AllTheSmallThingsTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.ENDER_EYE);
        }
    };

}
