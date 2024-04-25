package com.csykes.allthesmallthings.blocks;

import com.csykes.allthesmallthings.AllTheSmallThings;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModBlocks {
    public static final RegistryObject<Block> DEBARKER = AllTheSmallThings.BLOCKS.register("debarker", Debarker::new);
    public static void register(IEventBus modEventBus) {
        // This method can be empty. It's used to ensure that the class is loaded and the static initializers are run.
    }
}
