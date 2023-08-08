package com.csykes.allthesmallthings.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class DebarkerTile extends TileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private static final String REGEX = ":(.+)_";

    public DebarkerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DebarkerTile() {
        this(TileEntities.DEBARKER_TILE.get());
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                if (slot == 0 && isItemValid(0, this.getStackInSlot(slot)) && this.getStackInSlot(0).getCount() != 0) {
                    // convertLogToStripped();
                }

                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                 switch (slot) {
                    case 0:
                        return (isLog(stack) && hasStrippedLog(stack));
                    case 1:
                        return stack.isEmpty() || isStrippedLog(stack);
                    default:
                        return false;

                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };
    }

    public void convertLogToStripped() {
        try {
            ItemStack inputStack = itemHandler.getStackInSlot(0);
        if (!inputStack.isEmpty() && itemHandler.isItemValid(0, inputStack)) {
            ItemStack strippedStack = getStrippedVersion(inputStack);
            if (!strippedStack.isEmpty()) {
                int inputCount = Math.min(inputStack.getCount(), 64); // Limit to 20 logs
                ItemStack strippedLogs = new ItemStack(strippedStack.getItem(), inputCount);

                itemHandler.extractItem(0, inputCount, false);
                itemHandler.insertItem(1, strippedLogs, false);
                markDirty();
            }
        }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ItemStack getStrippedVersion(ItemStack stack) {
        Block blockToConvert = getBlockFromItem(stack);

        // Checks to see if an item is a log and converts
        if (isLog(stack)) {
            try {
                return createItemStack(("minecraft:stripped_" + getTypeOfLog(blockToConvert.toString()) + "_log"));
            }

            catch (Exception e){
                return null;
            }
        }
        return null;
    }

    /**
     * A function to get the stripped version of a block
     * 
     * @param stack
     * @return ItemStack with the name of string, null if not found
     */
    private ItemStack getStrippedLog(ItemStack stack) {
        Block blockToConvert = getBlockFromItem(stack);

        // Checks to see if an item is a log and converts
        if (isLog(stack)) {
            try {
                return createItemStack(("minecraft:stripped_" + getTypeOfLog(blockToConvert.toString()) + "_log"));
            }

            catch (Exception e){
                return null;
            }
        }
        return null;
    }

    /**
     * A function to get the block from an item
     * 
     * @param stack - item stack to convert
     * @return Block
     */
    private static Block getBlockFromItem(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem());
    }

    /**
     * A function to confirm whether it has a stripped log variant
     * @param stack - itemStack to check
     * @return true if it has a stripped log variant, false otherwise
     */
    private boolean hasStrippedLog(ItemStack stack) {
        if (getStrippedLog(stack) != null) {
            return true;
        }
        return false;
    }


    /**
     * Gets the type of log using a regex pattern
     * 
     * @param block
     * @return type of log as String or null if not found
     */
    private String getTypeOfLog(String block) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matched = pattern.matcher(block);
        if (matched.find()) {
            return matched.group(1);
        }
        return null;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        itemHandler.deserializeNBT(compound.getCompound("itemHandler"));
        super.read(state, compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("itemHandler", itemHandler.serializeNBT());
        return super.write(compound);
    }

    /**
     * Checks if a given itemStack is a log
     * 
     * @param stack to check
     * @return true if the itemStack is a log and does not contain "stripped" in the name, false otherwise
     */
    public boolean isLog(ItemStack stack) {
        return BlockTags.LOGS.contains(getBlockFromItem(stack)) && !stack.toString().contains("stripped");
    }

    /**
     * A function to create a new item stack based on a string name
     * 
     * @param itemType itemType = name of item formed by @getStrippedLog
     * @return ItemStack as found item, empty if not found
     */
    private ItemStack createItemStack(String itemType) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemType));
        if (item != null) {
            return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

    /**
     * Function to check if stripped
     * 
     * @param stack - item stack to check
     * @return true if contains "stripped"
     */
    public boolean isStrippedLog(ItemStack stack) {
        return stack.toString().toLowerCase().contains("stripped");
    }
}
