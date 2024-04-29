package com.csykes.allthesmallthings.blockEntities;

import com.csykes.allthesmallthings.menus.DebarkerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class DebarkerBlockEntity extends BlockEntity implements ICapabilityProvider, MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == INPUT_SLOT)
                return super.isItemValid(slot, stack);
            else
                return false;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == OUTPUT_SLOT || !simulate ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    };
    private final EnergyStorage energyHandler = new EnergyStorage(60000, 256) {};
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    private int progress = 0;
    private int maxProgress = TICKS_PER_ITEM;
    private int energy = energyHandler.getEnergyStored();
    private int maxEnergy = energyHandler.getMaxEnergyStored();
    protected final ContainerData data;

    public DebarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.DEBARKER_ENTITY_TYPE.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> DebarkerBlockEntity.this.progress;
                    case 1 -> DebarkerBlockEntity.this.maxProgress;
                    case 2 -> DebarkerBlockEntity.this.energy;
                    case 3 -> DebarkerBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> DebarkerBlockEntity.this.progress = value;
                    case 1 -> DebarkerBlockEntity.this.maxProgress = value;
                    case 2 -> DebarkerBlockEntity.this.energy = value;
                    case 3 -> DebarkerBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public EnergyStorage getEnergyHandler() {
        return this.energyHandler;
    }
    //region Items

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
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    }

//endregion
//region Menu


@Override
public @NotNull Component getDisplayName() {
    return Component.literal("Debarker");
}

@Nullable
@Override
public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
    return new DebarkerMenu(id, inventory, this, this.data);
}
//endregion
    public float getProgressFraction() {
        return (float) this.progress / this.maxProgress;
    }
    private static final int ENERGY_REQ = 600;
    private static final int TICKS_PER_ITEM = 10;
    private static final short INPUT_SLOT = 0;
    private static final short OUTPUT_SLOT = 1;
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity pBlockEntity) {
        if (pBlockEntity instanceof DebarkerBlockEntity db) {
            db.energy = db.energyHandler.getEnergyStored();
            db.maxEnergy = db.energyHandler.getMaxEnergyStored();
            if ((1-(db.progress/db.maxProgress))*ENERGY_REQ <= db.energy ) {
                db.itemHandler.getStackInSlot(OUTPUT_SLOT);
                if (isInputValid(db) && isOutputValid(db)) {
                    db.energyHandler.extractEnergy((ENERGY_REQ/TICKS_PER_ITEM), false);
                    if (db.progress == db.maxProgress) {
                        ItemStack inStack = db.itemHandler.getStackInSlot(INPUT_SLOT);
                        ItemStack outStack = db.itemHandler.getStackInSlot(OUTPUT_SLOT);
                        ItemStack translateLogType = translateLogType(new ItemStack(inStack.getItem(), 1));
                        inStack.setCount(inStack.getCount() - 1);
                        if (!ItemStack.isSame(outStack, translateLogType))
                            db.itemHandler.setStackInSlot(OUTPUT_SLOT, translateLogType);
                        else
                            outStack.setCount(outStack.getCount() + 1);
                        db.progress = 0;
                    }
                    db.progress++;
                }
            }
        }
    }

    public static boolean isInputValid(DebarkerBlockEntity db) {
        db.itemHandler.getStackInSlot(INPUT_SLOT);
        return translateLogType(db.itemHandler.getStackInSlot(INPUT_SLOT)).getItem() != Items.AIR;
    }
    public static boolean isOutputValid(DebarkerBlockEntity db) {
        ItemStack inSlt = db.itemHandler.getStackInSlot(INPUT_SLOT);
        ItemStack outSlt = db.itemHandler.getStackInSlot(OUTPUT_SLOT);
        return outSlt.getItem() == Items.AIR || translateLogType(inSlt).getItem() == outSlt.getItem() && outSlt.getCount() < outSlt.getMaxStackSize();
    }

    public static ItemStack translateLogType(ItemStack stack) {
        Block block = Block.byItem(stack.getItem());
        ResourceLocation blockIdentifier = ForgeRegistries.BLOCKS.getKey(block);


        // Define the suffixes we're interested in
        String[] suffixes = {"_log", "_wood", "_stem", "_hyphane"};

        // Check each suffix to see if the input string ends with it
        for (String suffix : suffixes) {
            assert blockIdentifier != null;
            if (blockIdentifier.toString().endsWith(suffix)) {
                // Find the index of the last underscore to determine where to insert "stripped_"
                int underscoreIndex = blockIdentifier.toString().lastIndexOf(':');
                if (underscoreIndex != -1) {
                    // Build the new string with "stripped_" inserted before the suffix
                    return new ItemStack(ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse("stripped_" + blockIdentifier.toString().substring(underscoreIndex + 1))));
                }
            }
        }
        return new ItemStack(Items.AIR, 1);
    }
}
