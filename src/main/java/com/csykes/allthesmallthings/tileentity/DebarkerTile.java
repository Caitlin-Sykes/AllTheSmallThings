package com.csykes.allthesmallthings.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DebarkerTile extends TileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public DebarkerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DebarkerTile() {
        this(TileEntities.DEBARKER_TILE.get());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            /**
             * Function for contents changing
             * @param slot - slot to check
             */
            protected void onContentsChanged(int slot) {
                markDirty();

                if (slot != 1) {
                    stripLogs(slot);
                }
            }

            /**
             * checks if item is valid
             * @param slot - slot to check
             * @param stack - item stack to check
             */
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0:
                        return stack.getItem() == Items.OAK_LOG;
                    case 1:
                        return stack.isEmpty() || stack.getItem() == Items.STRIPPED_OAK_LOG;
                    default:
                        return false;
                }
            }

            /**
             * Limit of items in inventory slot
             * Slot: inventory slot.
             */
            @Override
            public int getSlotLimit(int slot) {
                return 64;
            }

            @Nonnull
            @Override
            /**
             * Inserts an item
             * @param Slot - slot to insert
             * @param ItemStack - the item stack to enter
             * @param simulate - whether its been changed or not
             */
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }

                else {
                    return super.insertItem(slot, stack, simulate);
                }
            }

            /**
             * overrides, method extracts items from a slot
             * @param slot - slot to copy
             * @param amount - amount to remove
             * @param simulate - changes
             */
            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {

                //If slot = 1 and the slot is not empty then makes a copy of the items in the stack and sets the count
                //if not simulate then gets the stack in the slot and shrinks
                if (slot == 1 && !itemHandler.getStackInSlot(slot).isEmpty()) {
                    ItemStack extractedStack = itemHandler.getStackInSlot(slot).copy();
                    extractedStack.setCount(Math.min(amount, extractedStack.getCount()));
                    if (!simulate) {
                        itemHandler.getStackInSlot(slot).shrink(extractedStack.getCount());
                        checkItemRemoved(slot, extractedStack.getCount());
                    }
                    return extractedStack;
                } 
                
                //If slot = 0 and the slot is not empty then makes a copy of the items in the stack and sets the count
                //if not simulate then gets the stack in the slot and shrinks
                else if (slot == 0 && !itemHandler.getStackInSlot(slot).isEmpty()) {
                    int extractedAmount = Math.min(amount, itemHandler.getStackInSlot(slot).getCount());
                    ItemStack extractedStack = itemHandler.getStackInSlot(slot).split(extractedAmount);
                    if (!simulate) {
                        itemHandler.setStackInSlot(slot, itemHandler.getStackInSlot(slot));
                        checkItemRemoved(slot, extractedAmount);
                    }
                    return extractedStack;
                }

                //Return empty if doesn't fit condition
                return ItemStack.EMPTY;
            }

        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    /**
     * reads data on world load
     * 
     * @param state
     * @param nbt
     */
    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    /**
     * writes data on world save
     * 
     * @param compound
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    /**
     * A function to strip logs
     */
    public void stripLogs(int slot) {
        boolean hasUnstrippedLogInSlot = this.itemHandler.getStackInSlot(0).getCount() > 0
                && this.itemHandler.getStackInSlot(0).getItem() == Items.OAK_LOG && slot == 0
                && itemHandler.getStackInSlot(1).isEmpty();

        // If condition is true
        if (hasUnstrippedLogInSlot) {

            // Removes the number of normal logs, replaces with same number of stripped
            // logs.
            this.itemHandler.insertItem(1,
                    new ItemStack(Items.STRIPPED_OAK_LOG, this.itemHandler.getStackInSlot(0).getCount()), false);

        }

    }

    /**
     * A function to check if the item is removed
     * 
     * @param slot  - slot to check
     * @param count - number of items to remove
     */
    private void checkItemRemoved(int slot, int count) {
        if (slot == 1) {
            ItemStack logStack = this.itemHandler.getStackInSlot(0);
            logStack.shrink(count);
            this.itemHandler.setStackInSlot(0, logStack);
        }
    }

}
