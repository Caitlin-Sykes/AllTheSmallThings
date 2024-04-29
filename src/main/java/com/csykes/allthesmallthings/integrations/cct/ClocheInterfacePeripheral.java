package com.csykes.allthesmallthings.integrations.cct;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ClocheInterfacePeripheral implements IPeripheral {
    @Override
    public String getType() {
        return "";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}
