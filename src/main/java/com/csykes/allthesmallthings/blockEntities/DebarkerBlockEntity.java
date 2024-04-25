package com.csykes.allthesmallthings.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;

public class DebarkerBlockEntity extends BlockEntity implements IEnergyStorage, ICapabilityProvider {
    private int energy;
    private static final int MAX_ENERGY = 10000;

    public DebarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.DEBARKER_ENTITY_TYPE.get(), blockPos, blockState);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = energy;
        if (!simulate) {
            energy += maxReceive;
            energy = Math.min(energy, MAX_ENERGY);
        }
        return Math.abs(energyReceived-energy);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return MAX_ENERGY;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
//        energyStorage.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
//        energyStorage.revive();
    }
}
