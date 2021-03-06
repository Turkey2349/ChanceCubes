package chanceCubes.rewards.defaultRewards;

import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BossBlazeReward extends BossBaseReward
{
	public BossBlazeReward()
	{
		super("demonic_blaze");
	}

	@Override
	public LivingEntity initBoss(ServerWorld world, BlockPos pos, PlayerEntity player, JsonObject settings, BattleWrapper battleWrapper)
	{
		BlazeEntity blaze = EntityType.BLAZE.create(world);
		blaze.setCustomName(new StringTextComponent("Demonic Blaze"));

		Scheduler.scheduleTask(new Task("blaze_abilities", -1, 20)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				if(!blaze.isAlive())
				{
					Scheduler.removeTask(this);
					return;
				}

				if(RewardsUtil.rand.nextInt(10) == 4)
					goInvisible(blaze);
				if(RewardsUtil.rand.nextInt(10) == 4)
					setGroundOnFire(world, player.getPosition());
				if(RewardsUtil.rand.nextInt(3) == 4)
					shootFireballs(world, blaze, player);
			}
		});
		return blaze;
	}

	private void goInvisible(BlazeEntity blaze)
	{
		blaze.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 5));
	}

	private void setGroundOnFire(World world, BlockPos playerPos)
	{
		for(int xOff = -1; xOff <= 1; xOff++)
		{
			for(int zOff = -1; zOff <= 1; zOff++)
			{
				BlockPos offPos = playerPos.add(xOff, 0, zOff);
				RewardsUtil.placeBlock(Blocks.FIRE.getDefaultState(), world, offPos);
			}
		}
	}

	private void shootFireballs(World world, BlazeEntity blaze, PlayerEntity player)
	{
		double d1 = player.getPosX() - blaze.getPosX();
		double d2 = player.getBoundingBox().minY + (double) (player.getHeight() / 2.0F) - (blaze.getPosY() + (double) (blaze.getHeight() / 2.0F));
		double d3 = player.getPosZ() - blaze.getPosZ();
		for(int i = 0; i < 5; i++)
		{
			SmallFireballEntity entitysmallfireball = new SmallFireballEntity(world, blaze, d1 + blaze.getRNG().nextGaussian(), d2, d3 + blaze.getRNG().nextGaussian());
			entitysmallfireball.getPosition().add(0, blaze.getPosY() + (double) (blaze.getHeight() / 2.0F) + 0.5D, 0);
			world.addEntity(entitysmallfireball);
		}
	}

	@Override
	public void onBossFightEnd(ServerWorld world, BlockPos pos, PlayerEntity player)
	{

	}
}
