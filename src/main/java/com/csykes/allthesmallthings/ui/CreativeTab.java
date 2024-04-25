package com.csykes.allthesmallthings.ui;

import com.csykes.allthesmallthings.items._ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CreativeTab extends CreativeModeTab {
    public static final CreativeModeTab MY_CUSTOM_TAB = new CreativeTab();

    public CreativeTab() {
        super("allthesmallthingstab");
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(_ModItems.SAWBLADE.get());
    }
}
