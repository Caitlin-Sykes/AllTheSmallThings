package com.csykes.allthesmallthings.blocks;

import com.csykes.allthesmallthings.blockEntities.DebarkerBlockEntity;
import com.csykes.allthesmallthings.blockEntities._ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClocheInterface extends Block {
    public ClocheInterface() {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
        );
    }

//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level p_153212_, @NotNull BlockState p_153213_, @NotNull BlockEntityType<T> blockEntity) {
//        return blockEntity == _ModBlockEntities.DEBARKER_ENTITY_TYPE.get() ? DebarkerBlockEntity::tick : null;
//    }

//    @Override
//    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
//        return new DebarkerBlockEntity(pos, state);
//    }
}

