package chanceCubes.rewards.giantRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.util.RewardsUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class MixedFluidSphereReward extends BaseCustomReward
{
	public MixedFluidSphereReward()
	{
		super(CCubesCore.MODID + ":mixed_fluid_sphere", 0);
	}

	@Override
	public void trigger(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings)
	{
		List<OffsetBlock> blocks = new ArrayList<>();

		int delay = 0;
		for(int i = 0; i <= 5; i++)
		{
			for(int yy = -5; yy < 6; yy++)
			{
				for(int zz = -5; zz < 6; zz++)
				{
					for(int xx = -5; xx < 6; xx++)
					{
						BlockPos loc = new BlockPos(xx, yy, zz);
						double dist = Math.sqrt(Math.abs(loc.distanceSq(0, 0, 0, false)));
						if(dist <= 5 - i && dist > 5 - (i + 1))
						{
							if(i == 0)
							{
								OffsetBlock osb = new OffsetBlock(xx, yy, zz, Blocks.GLASS, false, delay);
								osb.setBlockState(Blocks.GLASS.getDefaultState());
								blocks.add(osb);
								delay++;
							}
							else
							{
								Fluid fluid = RewardsUtil.getRandomFluid(true);
								OffsetBlock osb = new OffsetBlock(xx, yy, zz, fluid.getDefaultState().getBlockState(), false, delay);
								blocks.add(osb);
								delay++;
							}

						}
					}
				}
			}
			delay += 10;
		}

		for(OffsetBlock b : blocks)
			b.spawnInWorld(world, pos.getX(), pos.getY(), pos.getZ());
	}
}