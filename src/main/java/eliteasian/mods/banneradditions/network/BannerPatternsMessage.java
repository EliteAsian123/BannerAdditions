package eliteasian.mods.banneradditions.network;

import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BannerPatternsMessage {
    private final List<BannerPatternHolder> bannerPatterns;

    public BannerPatternsMessage(List<BannerPatternHolder> bannerPatterns) {
        this.bannerPatterns = bannerPatterns;
    }

    public static void encode(BannerPatternsMessage message, PacketBuffer buffer) {
        buffer.writeShort(message.bannerPatterns.size());

        for (BannerPatternHolder bannerPattern : message.bannerPatterns) {
            buffer.writeResourceLocation(bannerPattern.getBannerTexture());
            buffer.writeResourceLocation(bannerPattern.getShieldTexture());

            buffer.writeString(bannerPattern.getHashname());
            buffer.writeString(bannerPattern.getFullName());

            buffer.writeResourceLocation(bannerPattern.getItem());
        }
    }

    public static BannerPatternsMessage decode(PacketBuffer buffer) {
        List<BannerPatternHolder> bannerPatterns = new ArrayList<>();

        int size = buffer.readShort();

        for (int i = 0; i < size; i++) {
            ResourceLocation bannerTexture = buffer.readResourceLocation();
            ResourceLocation shieldTexture = buffer.readResourceLocation();

            String hashname = buffer.readString();
            String fullName = buffer.readString();

            ResourceLocation item = buffer.readResourceLocation();

            bannerPatterns.add(new BannerPatternHolder(bannerTexture, shieldTexture, hashname, fullName, item));
        }

        return new BannerPatternsMessage(bannerPatterns);
    }

    public static void handle(BannerPatternsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            throw new UnsupportedOperationException("Banner Pattern packet side is wrong!");
        }

        context.enqueueWork(() -> {
            BannerPatterns.clear();

            for (BannerPatternHolder bannerPattern : message.bannerPatterns) {
                BannerPatterns.add(bannerPattern);
            }
        });
    }
}
