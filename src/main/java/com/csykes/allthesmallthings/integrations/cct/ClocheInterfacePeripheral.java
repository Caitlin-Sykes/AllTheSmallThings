package com.csykes.allthesmallthings.integrations.cct;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ClocheInterfacePeripheral implements IDynamicPeripheral {
    @Override
    public String getType() {
        return "Cloche_Interface";
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"say_hi", "to_cait"};
    }

    @Override
    public MethodResult callMethod(IComputerAccess iComputerAccess, ILuaContext iLuaContext, int i, IArguments iArguments) throws LuaException {
        return null;
    }
}
