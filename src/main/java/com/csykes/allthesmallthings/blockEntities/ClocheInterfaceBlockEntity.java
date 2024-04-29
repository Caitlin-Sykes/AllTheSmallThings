package com.csykes.allthesmallthings.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClocheInterfaceBlockEntity extends BlockEntity {

    public ClocheInterfaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.CLOCHE_INTERFACE_ENTITY_TYPE.get(), blockPos, blockState);
    }
}
