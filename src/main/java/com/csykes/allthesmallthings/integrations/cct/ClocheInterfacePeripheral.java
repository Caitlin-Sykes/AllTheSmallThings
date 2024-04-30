package com.csykes.allthesmallthings.integrations.cct;

import com.csykes.allthesmallthings.util.ItemStackUtils;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Set;

public class ClocheInterfacePeripheral implements IDynamicPeripheral {
    private final Level level;
    private final BlockPos blockPos;
    private final BlockEntity blockEntity;

    public ClocheInterfacePeripheral(Level level, BlockPos blockPos, BlockEntity blockEntity) {
        this.level = level;
        this.blockPos = blockPos;
        this.blockEntity = blockEntity;
    }

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
        return new String[]{"what_is_above_me"};
    }

    @Override
    public MethodResult callMethod(IComputerAccess iComputerAccess, ILuaContext iLuaContext, int methodIndex, IArguments arguments) throws LuaException {
        if (level.isClientSide()) {
            return MethodResult.of(null, "Running on client side, no BlockEntity available");
        }
        if (methodIndex == 0) {
            if (blockEntity != null && (blockEntity.getClass().getSimpleName().equals("ClocheBlockEntity"))) {
                try {
                    Field privateField = blockEntity.getClass().getDeclaredField("inventory");  // Replace 'privateFieldName' with the actual field name
                    privateField.setAccessible(true);  // Make the field accessible
                    NonNullList<ItemStack> value =(NonNullList<ItemStack>) privateField.get(blockEntity);  // Get the value of the field
                    return ItemStackUtils.convertToMethodResult(value);
                    // Optionally, modify the value
                    // privateField.set(entity, newValue);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    // Handle the error gracefully
                }
            }
        }
        return null;
    }
}
