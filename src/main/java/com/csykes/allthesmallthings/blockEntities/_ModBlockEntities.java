package com.csykes.allthesmallthings.blockEntities;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.blocks.Debarker;
import com.csykes.allthesmallthings.blocks._ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModBlockEntities {
    public static final RegistryObject<BlockEntityType<DebarkerBlockEntity>> DEBARKER_ENTITY_TYPE = AllTheSmallThings.BLOCK_ENTITIES.register("debarker_entity",
            () -> BlockEntityType.Builder.of(DebarkerBlockEntity::new, _ModBlocks.DEBARKER.get()).build(null));
    public static void register(IEventBus modEventBus) {
//        AllTheSmallThings.BLOCK_ENTITIES.getEntries().
    }
}
