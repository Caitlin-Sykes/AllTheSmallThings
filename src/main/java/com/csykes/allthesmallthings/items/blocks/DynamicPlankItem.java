package com.csykes.allthesmallthings.items.blocks;

import com.csykes.allthesmallthings.blocks._ModBlocks;
import com.csykes.allthesmallthings.ui.CreativeTab;
import net.minecraft.world.item.BlockItem;

public class DynamicPlankItem extends BlockItem {
    public static final int MAXIMUM_STACK = 64; // maximum stack size
    public DynamicPlankItem() {
        super(_ModBlocks.DYNAMIC_PLANK.get(), new Properties().stacksTo(MAXIMUM_STACK).tab(CreativeTab.MY_CUSTOM_TAB));

    }

}
