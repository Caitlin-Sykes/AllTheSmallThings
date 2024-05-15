package com.csykes.allthesmallthings.blocks;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.blocks.chipper.Mason;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModBlocks {
    public static final RegistryObject<Block> DEBARKER = AllTheSmallThings.BLOCKS.register("debarker", Debarker::new);
    public static final RegistryObject<Block> CLOCHE_INTERFACE = AllTheSmallThings.BLOCKS.register("cloche_interface", ClocheInterface::new);
    public static final RegistryObject<Block> SIGNAGE_DISPLAY_BASIC = AllTheSmallThings.BLOCKS.register("signage_display", SignageDisplay::new);

//    Chippers
    public static final RegistryObject<Block> MASON_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_mason", Mason::new);
//    public static final RegistryObject<Block> ALCHEMY_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_alchemy", Mason::new);
//    public static final RegistryObject<Block> BOTANY_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_botany", Mason::new);
//    public static final RegistryObject<Block> CARPENTRY_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_carpentry", Mason::new);
//    public static final RegistryObject<Block> GLASS_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_glass", Mason::new);
//    public static final RegistryObject<Block> LOOM_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_loom", Mason::new);
//    public static final RegistryObject<Block> TINKER_CHIPPER = AllTheSmallThings.BLOCKS.register("chipper_tinker", Mason::new);
    @SuppressWarnings("EmptyMethod")
    public static void register(IEventBus modEventBus) {
        // This method can be empty. It's used to ensure that the class is loaded and the static initializers are run.
    }
}
