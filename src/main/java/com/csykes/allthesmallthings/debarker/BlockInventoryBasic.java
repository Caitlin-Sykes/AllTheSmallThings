package com.csykes.allthesmallthings.debarker;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;


/**
 * Basic block inventory
 */
public class BlockInventoryBasic extends ContainerBlock
{
	public BlockInventoryBasic()
	{
		super(Block.Properties.create(Material.ROCK)
          );
	}

  /**
   * Create the Tile Entity for this block.
   * @return new tile entity
   */
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return createNewTileEntity(world);
  }

  /**
   * Creates new tile entity
   */
  @Nullable
  @Override
  public TileEntity createNewTileEntity(IBlockReader worldIn) {
    return new TileEntityInventoryBasic();
  }

  /**
   * Returns true as it has a tile entity
   * @param state - state of block
   */
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	/**
   *  Called when the block is right clicked
   * @param BlockState state - state of the block
   * @param World - the world
   * @param blockPos - position of the block
   * @param PlayerEntity - player entity
   * @param hand - player hand
   * @param rayTraceResult raytrace
	*/ 
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (worldIn.isRemote) return ActionResultType.SUCCESS; 

    INamedContainerProvider namedContainerProvider = this.getContainer(state, worldIn, pos);
    if (namedContainerProvider != null) {
      if (!(player instanceof ServerPlayerEntity)) return ActionResultType.FAIL;
      ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
      NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer)->{});
    }
    return ActionResultType.SUCCESS;
	}

  /**
   * When the block is broken, drop contents.
   * @param BlockState state - state of the block
   * @param World - the world
   * @param blockPos - position of the block
   * @param newState - state of the block
   * @param isMoving - true if moving, false otherwise.
   * @deprecated
   */
  @Deprecated
  public void onReplaced(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = world.getTileEntity(blockPos);
      if (tileentity instanceof TileEntityInventoryBasic) {
        TileEntityInventoryBasic tileEntityInventoryBasic = (TileEntityInventoryBasic)tileentity;
        tileEntityInventoryBasic.dropAllContents(world, blockPos);
      }
      super.onReplaced(state, world, blockPos, newState, isMoving); 
    }
	}

  // ---------------------------
  // If you want your container to provide redstone power to a comparator based on its contents, implement these methods
  //  see vanilla for examples

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return false;
  }

  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    return 0;
  }

  /**
   * Renders the block
   * @param iBlockState - block state
   */
  @Override
  public BlockRenderType getRenderType(BlockState iBlockState) {
    return BlockRenderType.MODEL;
  }

  
  /**
   * Returns the shape of the block
   */
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return CHEST_SHAPE;
  }

  private static final Vector3d CHEST_MIN_CORNER = new Vector3d(1.0, 0.0, 1.0);
  private static final Vector3d CHEST_MAX_CORNER = new Vector3d(15.0, 8.0, 15.0);
  private static final VoxelShape CHEST_SHAPE = Block.makeCuboidShape(
          CHEST_MIN_CORNER.getX(), CHEST_MIN_CORNER.getY(), CHEST_MIN_CORNER.getZ(),
          CHEST_MAX_CORNER.getX(), CHEST_MAX_CORNER.getY(), CHEST_MAX_CORNER.getZ());

  }
