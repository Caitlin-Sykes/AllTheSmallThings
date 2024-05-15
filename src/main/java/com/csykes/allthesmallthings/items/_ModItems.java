package com.csykes.allthesmallthings.items;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.blocks._ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModItems {
    public static final RegistryObject<Item> SAWBLADE = AllTheSmallThings.ITEMS.register("sawblade", Sawblade::new);
    public static final RegistryObject<Item> DEBARKER = AllTheSmallThings.ITEMS.register("debarker", () -> new BlockItemStackable(_ModBlocks.DEBARKER.get()));
    public static final RegistryObject<Item> CLOCHE_INTERFACE = AllTheSmallThings.ITEMS.register("cloche_interface", () -> new BlockItemStackable(_ModBlocks.CLOCHE_INTERFACE.get()));
    public static final RegistryObject<Item> SIGNAGE_DISPLAY_BASIC = AllTheSmallThings.ITEMS.register("signage_display", () -> new BlockItemStackable(_ModBlocks.SIGNAGE_DISPLAY_BASIC.get()));
    public static final RegistryObject<Item> MASON_CHIPPER = AllTheSmallThings.ITEMS.register("chipper_mason", () -> new BlockItemStackable(_ModBlocks.MASON_CHIPPER.get()));

    @SuppressWarnings({"EmptyMethod", "NeverUsed"})
    public static void register(IEventBus modEventBus) {
        // This method can be empty. It's used to ensure that the class is loaded and the static initializers are run.
    }

}
