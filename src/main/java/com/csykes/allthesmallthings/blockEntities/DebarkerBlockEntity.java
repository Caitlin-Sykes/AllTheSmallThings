package com.csykes.allthesmallthings.blockEntities;

import com.csykes.allthesmallthings.menus.DebarkerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;

public class DebarkerBlockEntity extends BlockEntity implements IEnergyStorage, ICapabilityProvider, MenuProvider {
    private int energy;
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
//                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition)); //TODO idk might need it
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // TODO Handle the slots for input and output
            return super.isItemValid(slot, stack);
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private static final int MAX_ENERGY = 10000;
    private int progress = 0;
    private int maxProgress = 100;
    protected final ContainerData data;


    public DebarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.DEBARKER_ENTITY_TYPE.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DebarkerBlockEntity.this.progress;
                    case 1 -> DebarkerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DebarkerBlockEntity.this.progress = value;
                    case 1 -> DebarkerBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }
//region Energy Handlers
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
//endregion
//region Items
    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    /**
     * Drop Items that are loaded in the block
     */
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        super.load(nbt);
    }
//endregion
//region Capabilities
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyItemHandler.cast();
        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
    }
//endregion
//region Menu


@Override
public @NotNull Component getDisplayName() {
    return Component.literal("Debarker");
}

@Nullable
@Override
public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new DebarkerMenu(id, inventory, this, this.data);
}
//endregion
}
