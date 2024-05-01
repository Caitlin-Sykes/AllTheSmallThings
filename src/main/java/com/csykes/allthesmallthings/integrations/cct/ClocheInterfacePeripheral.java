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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
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
        return new String[]{"getItemDetail", "getItemLimit", "getEnergy", "getEnergyCapacity", "tanks", "getLocation"};
    }

    @Override
    public MethodResult callMethod(IComputerAccess iComputerAccess, ILuaContext iLuaContext, int methodIndex, IArguments arguments) throws LuaException {
        if (level.isClientSide()) {
            return MethodResult.of(null, "Running on client side, no BlockEntity available");
        }
        if (blockEntity != null && (blockEntity.getClass().getSimpleName().equals("ClocheBlockEntity"))) {
            try {
                Field inventoryField = blockEntity.getClass().getDeclaredField("inventory");
                Field tankField = blockEntity.getClass().getDeclaredField("tank");
                Field energyField = blockEntity.getClass().getDeclaredField("energyStorage");
                inventoryField.setAccessible(true);  // Make the field accessible
                energyField.setAccessible(true);  // Make the field accessible
                tankField.setAccessible(true);  // Make the field accessible
                NonNullList<ItemStack> inventory = (NonNullList<ItemStack>) inventoryField.get(blockEntity);  // Get the value of the field
                EnergyStorage energy = (EnergyStorage) energyField.get(blockEntity);  // Get the value of the field
                FluidTank tank = (FluidTank) tankField.get(blockEntity);  // Get the value of the field
                ItemStack item;
                switch (methodIndex) {
                    case 0:
                        item = inventory.get(arguments.getInt(0)-1);
                        if (!item.isEmpty()) {
                            Map<String, Object> itemDetails = new HashMap<>();
                            itemDetails.put("item", item.getItem().getDescriptionId());
                            itemDetails.put("count", item.getCount());
                            itemDetails.put("maxStackSize", item.getMaxStackSize());

                            // Add more item properties as needed
                            if (item.hasTag()) {
                                itemDetails.put("tag", item.getTag().toString());
                            }
                            return MethodResult.of(itemDetails);
                        }
                        return null;
                    case 1:
                        item = inventory.get(arguments.getInt(0)-1);
                        if (!item.isEmpty()) {
                            return MethodResult.of(item.getMaxStackSize());
                        }
                        return null;
                    case 2:
                        return MethodResult.of(energy.getEnergyStored());
                    case 3:
                        return MethodResult.of(energy.getMaxEnergyStored());
                    case 4:
                        return MethodResult.of(Map.of(
                                "capacity", tank.getCapacity(),
                                "fluid", tank.getFluid().getFluid().getBucket().getDescriptionId(),
                                "amount", tank.getFluid().getAmount()
                        ));
                    case 5:
                        return MethodResult.of(Map.of(
                                "x", blockPos.getX(),
                                "y", blockPos.getY(),
                                "z", blockPos.getZ()
                        ));
                }

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                // Handle the error gracefully
            }
        }
        return null;
    }
}
