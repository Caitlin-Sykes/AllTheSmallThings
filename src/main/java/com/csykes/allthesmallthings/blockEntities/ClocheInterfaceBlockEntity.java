package com.csykes.allthesmallthings.blockEntities;

import com.csykes.allthesmallthings.menus.DebarkerMenu;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;

public class ClocheInterfaceBlockEntity implements IPeripheralProvider {
    public ClocheInterfaceBlockEntity(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    public LazyOptional<IPeripheral> getPeripheral(Level level, BlockPos blockPos, Direction direction) {
        return null;
    }

//region Capabilities


//endregion
}
