package net.launchers.mod.block.abstraction;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.loader.LLoader;
import net.launchers.mod.network.NetworkHandler;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;
import net.launchers.mod.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractLauncherBlock extends BaseEntityBlock
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
        super(BlockBehaviour.Properties.of(Material.PISTON).strength(0.7F, 0.6F).sound(SoundType.METAL).dynamicShape().noOcclusion());
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(0F, 0F, 0F, 16F, 16F, 16F);
    }


    @Override
    public void fallOn(Level level, BlockState p_152427_, BlockPos p_152428_, Entity entity, float distance) {
        entity.causeFallDamage(distance, 0.0F, level.damageSources().fall());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODELS);
        super.createBlockStateDefinition(builder);
    }


    public void launchEntities(Level world, BlockPos pos, List<? extends Entity> entities)
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
                Vec3 initialVelocity = entity.getDeltaMovement();
                Vec3 vectorForce = MathUtils.fromDirection(world.getBlockState(pos).getValue(AbstractLauncherBlock.FACING));
                Vec3 velocity = vectorForce.scale(force).add(initialVelocity);
                entity.setDeltaMovement(velocity);
                UnboundedEntityVelocityS2CPacket packet = new UnboundedEntityVelocityS2CPacket(entity.getId(), velocity);
                NetworkHandler.sendToAll(packet, world.getServer().getPlayerList());
            }
        }
    }



    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos neighborPos, boolean p_220069_6_)
    {
        AbstractLauncherBlockTileEntity launcherBlockEntity = (AbstractLauncherBlockTileEntity) world.getBlockEntity(pos);
        boolean isRecevingRedstonePower = world.hasNeighborSignal(pos) || world.hasNeighborSignal(pos.above());
        boolean isRetracted = launcherBlockEntity.launcherState == AbstractLauncherBlockTileEntity.LauncherState.RETRACTED;
        if(!isRetracted) return;
        if(isRecevingRedstonePower)
        {
            world.getBlockTicks().schedule(ScheduledTick.probe(this,pos));
        }
    }

    protected abstract void playLaunchSound(Level world, BlockPos pos);

//    public SPlaySoundPacket createLaunchSoundPacket(double x, double y, double z)
//    {
//
//        return new SPlaySoundPacket(LSounds.LAUNCHER_SOUND_EVENT.getId(), SoundSource.BLOCKS, new Vec3(x,y,z), 1F, 1F);
//    }
    public boolean canLaunch(Level world, BlockPos pos)
    {
        AbstractLauncherBlockTileEntity launcherBlockEntity = (AbstractLauncherBlockTileEntity) world.getBlockEntity(pos);
        BlockPos offset = pos.relative(world.getBlockState(pos).getValue(FACING));
        return (world.getBlockState(offset).isAir() || world.getBlockState(offset).getBlock().equals(Blocks.TRIPWIRE)) && launcherBlockEntity.launcherState == AbstractLauncherBlockTileEntity.LauncherState.RETRACTED;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        LLoader.LOGGER.debug( "Launching");
        if(canLaunch(world, pos))
        {
            List<Entity> entities = world.getEntitiesOfClass(Entity.class, new AABB(pos.relative(state.getValue(FACING))));
            launchEntities(world, pos, entities);
            playLaunchSound(world, pos);
            ((AbstractLauncherBlockTileEntity) world.getBlockEntity(pos)).startExtending();
        }
    }

    public int getTickRate(Level worldView)
    {
        return 1;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.getStateDefinition().any().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation direction) {
        return (BlockState) state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }


    @Override public BlockState mirror(BlockState state, Mirror mirrorIn)
    {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }
}
