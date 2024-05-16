package com.csykes.allthesmallthings.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DynamicPlankBlockEntity extends BlockEntity {

    public DynamicPlankBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.DYNAMIC_PLANK_ENTITY_TYPE.get(), blockPos, blockState);
    }
}
