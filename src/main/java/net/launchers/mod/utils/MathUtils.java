package net.launchers.mod.utils;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public final class MathUtils
{
    public static Vector3d fromDirection(Direction direction)
    {
        switch(direction)
        {
            case UP:
                return new Vector3d(0, 1, 0);
            case DOWN:
                return new Vector3d(0, -1, 0);
            case EAST:
                return new Vector3d(1, 0, 0);
            case WEST:
                return new Vector3d(-1, 0, 0);
            case NORTH:
                return new Vector3d(0, 0, -1);
            case SOUTH:
                return new Vector3d(0, 0, 1);
            default:
                return new Vector3d(0, 0, 0);
        }
    }
    
    public static AxisAlignedBB getExpansionBlock(BlockPos pos, Direction facing)
    {
        AxisAlignedBB res = new AxisAlignedBB(pos);
        switch(facing)
        {
            case UP:
                res.expandTowards(0.2D, 1.2D, 0.2D);
            case DOWN:
                res.expandTowards(0.2D, 1.2D, 0.2D);
            case EAST:
                res.expandTowards(1.2D, 0.2D, 0.2D);
            case WEST:
                res.expandTowards(1.2D, 0.2D, 0.2D);
            case NORTH:
                res.expandTowards(0.2D, 0.2D, 1.2D);
            case SOUTH:
                res.expandTowards(0.2D, 0.2D, 1.2D);
            default:
                res.expandTowards(0, 0, 0);
        }
        System.out.println(res);
        return res;
    }
}

