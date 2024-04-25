package com.csykes.allthesmallthings;

import com.csykes.allthesmallthings.blockEntities._ModBlockEntities;
import com.csykes.allthesmallthings.blocks._ModBlocks;
import com.csykes.allthesmallthings.items._ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(AllTheSmallThings.MOD_ID)
public class AllTheSmallThings {
    public static final String MOD_ID = "allthesmallthings";

    public static IEventBus MOD_EVENT_BUS;
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);



    /**
     * Constructor to initialise the event bus and events
     * @deprecated
     */
    @Deprecated
    public AllTheSmallThings() {
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCK_ENTITIES.register(MOD_EVENT_BUS);
        BLOCKS.register(MOD_EVENT_BUS);
        ITEMS.register(MOD_EVENT_BUS);
        registerCommonEvents();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> AllTheSmallThings::registerClientOnlyEvents);
    }

    public static void registerCommonEvents() {
        _ModItems.register(MOD_EVENT_BUS);
        _ModBlockEntities.register(MOD_EVENT_BUS);
        _ModBlocks.register(MOD_EVENT_BUS);
    }

    public static void registerClientOnlyEvents() {

    }

}