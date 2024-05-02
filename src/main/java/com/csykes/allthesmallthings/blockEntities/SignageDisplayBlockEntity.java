package com.csykes.allthesmallthings.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SignageDisplayBlockEntity extends BlockEntity {
    private Direction direction;

    public SignageDisplayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.SIGNAGE_DISPLAY_ENTITY_TYPE.get(), blockPos, blockState);
    }


    public Direction getDirection() {
        return direction;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Direction")) {
            direction = Direction.byName(tag.getString("Direction"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Direction", direction.getName());
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        this.setChanged();
    }
}
