package com.csykes.allthesmallthings.items;

import com.csykes.allthesmallthings.ui.CreativeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class BlockItemStackable extends BlockItem {
    public static final int MAXIMUM_STACK = 64; // maximum stack size
    public BlockItemStackable(Block bl) {
        super(bl, new Properties().stacksTo(MAXIMUM_STACK).tab(CreativeTab.MY_CUSTOM_TAB));
    }
}
