package com.csykes.allthesmallthings.item;

import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.csykes.allthesmallthings.AllTheSmallThings;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AllTheSmallThings.MOD_ID);

    public static final RegistryObject<Item> SAWBLADE_ITEM = ITEMS.register("sawblade", SawbladeItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }        
}
