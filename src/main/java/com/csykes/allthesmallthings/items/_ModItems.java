package com.csykes.allthesmallthings.items;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.items.blocks.DebarkerItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModItems {
    public static final RegistryObject<Item> SAWBLADE = AllTheSmallThings.ITEMS.register("sawblade", Sawblade::new);
    public static final RegistryObject<Item> DEBARKER = AllTheSmallThings.ITEMS.register("debarker", DebarkerItem::new);
    public static void register(IEventBus modEventBus) {
        // This method can be empty. It's used to ensure that the class is loaded and the static initializers are run.
    }

}
