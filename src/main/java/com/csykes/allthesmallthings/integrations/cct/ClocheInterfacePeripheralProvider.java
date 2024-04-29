package com.csykes.allthesmallthings.integrations.cct;

import com.csykes.allthesmallthings.blocks._ModBlocks;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class ClocheInterfacePeripheralProvider implements IPeripheralProvider {
    @Override
    public LazyOptional<IPeripheral> getPeripheral(Level level, BlockPos blockPos, Direction direction) {
        if (level.getBlockState(blockPos).getBlock() == _ModBlocks.CLOCHE_INTERFACE.get()) {
            return LazyOptional.of(ClocheInterfacePeripheral::new);
        }
        return LazyOptional.empty();
    }

//region Capabilities


//endregion
}
