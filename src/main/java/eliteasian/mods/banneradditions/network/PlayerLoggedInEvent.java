package eliteasian.mods.banneradditions.network;

import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = BannerAdditions.MOD_ID)
public class PlayerLoggedInEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        PacketDistributor.PacketTarget target = PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer());

        BannerAdditions.SIMPLE_CHANNEL_INSTANCE.send(target, new BannerPatternsMessage(BannerPatterns.getAsList()));
    }
}
