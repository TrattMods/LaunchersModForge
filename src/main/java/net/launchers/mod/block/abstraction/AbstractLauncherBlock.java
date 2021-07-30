package net.launchers.mod.block.abstraction;

import com.sun.javafx.geom.Vec3d;
import io.netty.handler.logging.LogLevel;
import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LNetwork;
import net.launchers.mod.initializer.LSounds;
import net.launchers.mod.loader.LLoader;
import net.launchers.mod.network.NetworkHandler;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;
import net.launchers.mod.utils.MathUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class AbstractLauncherBlock extends Block implements ITileEntityProvider
{
    public static final ResourceLocation LAUNCH_SOUND = new ResourceLocation(LLoader.MOD_ID, "launcher_block_launch");
    public static final IntegerProperty MODELS = IntegerProperty.create("models", 0, 2);
    public static final DirectionProperty FACING;

    private float launchForce = 1F;
    private int maxStackable = 4;
    protected float stackPowerPercentage;
    public float stackMultiplier;
    public float baseMultiplier;
    static
    {
        FACING = DirectionalBlock.FACING;
    }
    public AbstractLauncherBlock()
    {
        super(AbstractBlock.Properties.of(Material.PISTON).strength(0.7F, 0.6F).sound(SoundType.METAL).dynamicShape().noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
    {
        return Block.box(0F, 0F, 0F, 16F, 16F, 16F);
    }

    @Override
    public void fallOn(World world, BlockPos pos, Entity entity, float distance)
    {
        entity.causeFallDamage(distance, 0.0F);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, MODELS);
        super.createBlockStateDefinition(builder);
    }
    public void launchEntities(World world, BlockPos pos, List<? extends Entity> entities)
    {
        if(!world.isClientSide)
        {
            if(entities.size() < 1)
            {
                return;
            }
            float force = launchForce * baseMultiplier;

            BlockState parentState = world.getBlockState(pos);
            Direction stackDirection = parentState.getValue(FACING).getOpposite();
            BlockPos currentPos = pos.relative(stackDirection);
            int currentIndex = 1;
            double multiplier = 1F;
            if(!stackDirection.equals(Direction.UP) && !stackDirection.equals(Direction.DOWN))
            {
                multiplier *= 1.75F;
            }
            Block current;
            while(currentIndex < maxStackable &&
                    ((current = world.getBlockState(currentPos).getBlock()) instanceof AbstractLauncherBlock &&
                            world.getBlockState(currentPos).getValue(FACING).equals(parentState.getValue(FACING))))
            {
                AbstractLauncherBlock launcherBlock = (AbstractLauncherBlock) current;
                multiplier += launcherBlock.stackMultiplier;
                currentPos = currentPos.relative(stackDirection);
                currentIndex++;
            }
            force *= multiplier;
            for(Entity entity : entities)
            {
                Vector3d initialVelocity = entity.getDeltaMovement();
                Vector3d vectorForce = MathUtils.fromDirection(world.getBlockState(pos).getValue(AbstractLauncherBlock.FACING));
                Vector3d velocity = vectorForce.scale(force).add(initialVelocity);
                entity.setDeltaMovement(velocity);
                UnboundedEntityVelocityS2CPacket packet = new UnboundedEntityVelocityS2CPacket(entity.getId(), velocity);
                NetworkHandler.sendToAll(packet, world.getServer().getPlayerList());
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block p_220069_4_, BlockPos neighborPos, boolean p_220069_6_)
    {
        AbstractLauncherBlockTileEntity launcherBlockEntity = (AbstractLauncherBlockTileEntity) world.getBlockEntity(pos);
        boolean isRecevingRedstonePower = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
        boolean isRetracted = launcherBlockEntity.launcherState == AbstractLauncherBlockTileEntity.LauncherState.RETRACTED;
        if(!isRetracted) return;
        if(isRecevingRedstonePower)
        {
            world.getBlockTicks().scheduleTick(pos, this, this.getTickRate(world));
        }
    }
    protected abstract void playLaunchSound(World world, BlockPos pos);

    public SPlaySoundPacket createLaunchSoundPacket(double x, double y, double z)
    {
        return new SPlaySoundPacket(LSounds.LAUNCHER_SOUND_EVENT.getId(), SoundCategory.BLOCKS, new Vector3d(x,y,z), 1F, 1F);
    }
    public boolean canLaunch(World world, BlockPos pos)
    {
        AbstractLauncherBlockTileEntity launcherBlockEntity = (AbstractLauncherBlockTileEntity) world.getBlockEntity(pos);
        BlockPos offset = pos.relative(world.getBlockState(pos).getValue(FACING));
        return (world.getBlockState(offset).isAir() || world.getBlockState(offset).getBlock().equals(Blocks.TRIPWIRE)) && launcherBlockEntity.launcherState == AbstractLauncherBlockTileEntity.LauncherState.RETRACTED;
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random p_225534_4_)
    {
        LLoader.LOGGER.debug( "Launching");
        if(canLaunch(world, pos))
        {
            List<Entity> entities = world.getEntitiesOfClass(Entity.class, new AxisAlignedBB(pos.relative(state.getValue(FACING))));
            launchEntities(world, pos, entities);
            playLaunchSound(world, pos);
            ((AbstractLauncherBlockTileEntity) world.getBlockEntity(pos)).startExtending();
        }
    }


    public int getTickRate(IWorldReader worldView)
    {
        return 1;
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return (BlockState) this.getStateDefinition().any().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }
    @Override public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction)
    {
        return (BlockState) state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
}
