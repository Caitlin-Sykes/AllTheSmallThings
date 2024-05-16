package com.csykes.allthesmallthings.items;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.items.blocks.ClocheInterfaceItem;
import com.csykes.allthesmallthings.items.blocks.SignageDisplayItem;
import com.csykes.allthesmallthings.items.blocks.DebarkerItem;
import com.csykes.allthesmallthings.items.blocks.DynamicPlankItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModItems {
    public static final RegistryObject<Item> SAWBLADE = AllTheSmallThings.ITEMS.register("sawblade", Sawblade::new);
    public static final RegistryObject<Item> DEBARKER = AllTheSmallThings.ITEMS.register("debarker", DebarkerItem::new);
    public static final RegistryObject<Item> CLOCHE_INTERFACE = AllTheSmallThings.ITEMS.register("cloche_interface", ClocheInterfaceItem::new);
    public static final RegistryObject<Item> SIGNAGE_DISPLAY_BASIC = AllTheSmallThings.ITEMS.register("signage_display", SignageDisplayItem::new);
    public static final RegistryObject<Item> DYNAMIC_PLANK = AllTheSmallThings.ITEMS.register("dynamic_plank", DynamicPlankItem::new);
    @SuppressWarnings({"EmptyMethod", "NeverUsed"})
    public static void register(IEventBus modEventBus) {
        // This method can be empty. It's used to ensure that the class is loaded and the static initializers are run.
    }

}
