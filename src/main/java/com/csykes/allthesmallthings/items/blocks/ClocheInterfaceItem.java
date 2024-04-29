package com.csykes.allthesmallthings.items.blocks;

import com.csykes.allthesmallthings.blocks._ModBlocks;
import com.csykes.allthesmallthings.ui.CreativeTab;
import net.minecraft.world.item.BlockItem;

public class ClocheInterfaceItem extends BlockItem {
    public static final int MAXIMUM_STACK = 64; // maximum stack size
    public ClocheInterfaceItem() {
        super(_ModBlocks.CLOCHE_INTERFACE.get(), new Properties().stacksTo(MAXIMUM_STACK).tab(CreativeTab.MY_CUSTOM_TAB));

    }
}
