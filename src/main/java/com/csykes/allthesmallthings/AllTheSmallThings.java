package com.csykes.allthesmallthings;

import com.csykes.allthesmallthings.blockEntities.DynamicPlankBlockEntity;
import com.csykes.allthesmallthings.blockEntities._ModBlockEntities;
import com.csykes.allthesmallthings.blocks._ModBlocks;
import com.csykes.allthesmallthings.integrations.cct.ClocheInterfacePeripheralProvider;
import com.csykes.allthesmallthings.items._ModItems;
import com.csykes.allthesmallthings.menus.DebarkerScreen;
import com.csykes.allthesmallthings.menus._ModMenus;
import com.csykes.allthesmallthings.renderer.DynamicPlankBlockEntityRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import dan200.computercraft.api.ForgeComputerCraftAPI;

@Mod(AllTheSmallThings.MOD_ID)
public class AllTheSmallThings {
    public static final String MOD_ID = "allthesmallthings";

    public static IEventBus MOD_EVENT_BUS;
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);



    /**
     * Constructor to initialise the event bus and events
     */
    public AllTheSmallThings() {
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCK_ENTITIES.register(MOD_EVENT_BUS);
        BLOCKS.register(MOD_EVENT_BUS);
        ITEMS.register(MOD_EVENT_BUS);
        MENUS.register(MOD_EVENT_BUS);
        registerCommonEvents();
    }

    public static void registerCommonEvents() {
        _ModItems.register(MOD_EVENT_BUS);
        _ModMenus.register(MOD_EVENT_BUS);
        _ModBlockEntities.register(MOD_EVENT_BUS);
        _ModBlocks.register(MOD_EVENT_BUS);
        ForgeComputerCraftAPI.registerPeripheralProvider(new ClocheInterfacePeripheralProvider());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(_ModMenus.DEBARKER.get(), DebarkerScreen::new);
            ItemBlockRenderTypes.setRenderLayer(_ModBlocks.SIGNAGE_DISPLAY_BASIC.get(), RenderType.translucent());
        }
        
        @SubscribeEvent()
        public static void RegisterRenderers(EntityRenderersEvent.RegisterRenderers evnt) {
            evnt.registerBlockEntityRenderer(_ModBlockEntities.DYNAMIC_PLANK_ENTITY_TYPE.get(), DynamicPlankBlockEntityRenderer::new);

        }
    }
}