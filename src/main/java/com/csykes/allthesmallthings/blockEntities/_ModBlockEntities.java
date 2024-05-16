package com.csykes.allthesmallthings.blockEntities;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.blocks._ModBlocks;
import com.csykes.allthesmallthings.items.blocks.DynamicPlankItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModBlockEntities {
    public static final RegistryObject<BlockEntityType<DebarkerBlockEntity>> DEBARKER_ENTITY_TYPE = AllTheSmallThings.BLOCK_ENTITIES.register("debarker_entity",
            () -> BlockEntityType.Builder.of(DebarkerBlockEntity::new, _ModBlocks.DEBARKER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ClocheInterfaceBlockEntity>> CLOCHE_INTERFACE_ENTITY_TYPE = AllTheSmallThings.BLOCK_ENTITIES.register("cloche_interface_entity",
            () -> BlockEntityType.Builder.of(ClocheInterfaceBlockEntity::new, _ModBlocks.CLOCHE_INTERFACE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SignageDisplayBlockEntity>> SIGNAGE_DISPLAY_ENTITY_TYPE = AllTheSmallThings.BLOCK_ENTITIES.register("signage_entity",
            () -> BlockEntityType.Builder.of(SignageDisplayBlockEntity::new, _ModBlocks.SIGNAGE_DISPLAY_BASIC.get()).build(null));

    public static final RegistryObject<BlockEntityType<DynamicPlankBlockEntity>> DYNAMIC_PLANK_ENTITY_TYPE = AllTheSmallThings.BLOCK_ENTITIES.register("dynamic_plank_entity",
            () -> BlockEntityType.Builder.of(DynamicPlankBlockEntity::new, _ModBlocks.DYNAMIC_PLANK.get()).build(null));


    @SuppressWarnings("EmptyMethod")
    public static void register(IEventBus modEventBus) {
//        AllTheSmallThings.BLOCK_ENTITIES.getEntries().
    }
}
