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
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class DebarkerTile extends TileEntity implements IEnergyStorage, ITickableTileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private static final String REGEX = ":(.+)_";
    
    public static final int MAX_ENERGY_STORED = 1000; // Max energy machine can store
    public int ENERGY_STORED = 1000; // Current energy stored
    public static int ENERGY_COST = 100; // Energy cost for each run of the machine
    private static int TICKS_CONVERSION = 20; // How long it waits per cycle of ticks
    private static int TICKS_COUNTER = 0; // current tick counter

    public static final int MAX_WIDTH_CHARGEBAR = 70; //max width of the charge bar

    /**
     * Constructor
     * @param tileEntityTypeIn
     */
    public DebarkerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    /**
     * Constructor
     */
    public DebarkerTile() {
        this(TileEntities.DEBARKER_TILE.get());
    }

    /**
     * Capability
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * Creates new instances of handlers
     * @return ItemStackHandler
     */
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {

            /**
             * A function to determine if the log is valid
             * Slot 0: Has to be a log, and has a stripped variant slot
             * Slot 1: Has to be empty, or has to be a stripped log,
             * @return false otherwise
             */
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

            /**
             * Limit number of items in machine
             * @param return a stack
             */
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }
        };

    }

    /**
     * A function to convert the logs into their stripped variant
     */
    public void convertLogToStripped() {

        //If energy stored is over or = to the energy cost
        if (ENERGY_STORED >= ENERGY_COST) {
            try {

                //Gets stack of items in slot 0
                ItemStack inputStack = itemHandler.getStackInSlot(0);

                // If the input stack is not empty and the item is valid
                if (!inputStack.isEmpty() && itemHandler.isItemValid(0, inputStack)) {
                    //Gets stripped variant of log as ItemStack. Returns empty if cannot find it
                    ItemStack strippedStack = getStrippedVersion(inputStack);
    
                    //If stripped stack is not empty, gets stripped logs as new item stack, and gets one of it
                    if (!strippedStack.isEmpty()) {
                        ItemStack strippedLogs = new ItemStack(strippedStack.getItem(), 1);

                        //Takes one item out of slot 0, and adds one item to slot 1
                        itemHandler.extractItem(0, 1, false);
                        itemHandler.insertItem(1, strippedLogs, false);

                        //Deducts cost of machine process
                        ENERGY_STORED -= ENERGY_COST;
                        //Saves
                        markDirty();

                    }
                }
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the stripped variant of a log
     * @param stack log to check
     * @return stripped log version or null
     */
    private ItemStack getStrippedVersion(ItemStack stack) {
        Block blockToConvert = getBlockFromItem(stack);

        // Checks to see if an item is a log and converts
        if (isLog(stack)) {
            try {
                return createItemStack(("minecraft:stripped_" + getTypeOfLog(blockToConvert.toString()) + "_log"));
            }

            catch (Exception e) {
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

            catch (Exception e) {
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
     * 
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

    /**
     * Reads block state
     */
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        itemHandler.deserializeNBT(compound.getCompound("itemHandler"));
        super.read(state, compound);
    }

    /**
     * Saves block state
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("itemHandler", itemHandler.serializeNBT());
        return super.write(compound);
    }

    /**
     * Checks if a given itemStack is a log
     * 
     * @param stack to check
     * @return true if the itemStack is a log and does not contain "stripped" in the
     *         name, false otherwise
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

    /**
     * A function to add energy
     * 
     * @param maxReceive - max energy to receive
     * @param simulate
     * @return received energy
     */
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), maxReceive);

        if (!simulate) {
            ENERGY_STORED += energyReceived;
            markDirty();
        }

        return energyReceived;
    }

    /**
     * A function to extract energy
     * 
     * @param maxExtract - maximum amount of energy to extract
     * @param simulate
     * @return energy extracted
     */
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {

        if (!canExtract()) {
            return 0;
        }

        int energyExtracted = Math.min(ENERGY_STORED, maxExtract);

        if (!simulate) {
            ENERGY_STORED -= energyExtracted;
            markDirty(); 
        }

        return energyExtracted;
    }

    /**
     * A function to get the current energy stored
     * 
     * @return current energy_stored as int
     */
    @Override
    public int getEnergyStored() {
        return ENERGY_STORED;
    }

    /**
     * A function to get the max energy stored
     * 
     * @return max energy as int
     */
    @Override
    public int getMaxEnergyStored() {
        return MAX_ENERGY_STORED;
    }

    /**
     * A function to extract energy
     * 
     * @return true if energy is over 0, false otherwise
     */
    @Override
    public boolean canExtract() {
        // For example, you might check if the tile entity is active or has enough
        // energy stored
        return ENERGY_STORED > 0;
    }

    /**
     * A function to determine whether it can receive energy
     * 
     * @return false if energy is full, true otherwise
     */
    @Override
    public boolean canReceive() {
        return !isFull();
    }

    /**
     * A function to determine whether the energy stored is full or not
     * 
     * @return true if full, false otherwise
     */
    private boolean isFull() {
        if (ENERGY_STORED < MAX_ENERGY_STORED) {
            return true;
        }
        return false;
    }

    /**
     * Every minecraft tick, pings this function
     */
    @Override
    public void tick() {

        //If world not null and is not remote
        if (world != null && !world.isRemote) {
            // Only perform actions on the server side
            if (ENERGY_STORED >= ENERGY_COST && canExtract()) {
                TICKS_COUNTER++;

                if (TICKS_COUNTER >= TICKS_CONVERSION) {
                    convertLogToStripped();
                    TICKS_CONVERSION = 0;
                }
            }
        }
    }

}
