package github.kituin.chatimage.client;

import com.mojang.logging.LogUtils;
import github.kituin.chatimage.tools.ChatImageStyle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;

/**
 * @author kitUIN
 */
@Environment(EnvType.CLIENT)
public class ChatImageClient implements ClientModInitializer {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static String MOD_ID = "chatimage";
    public static String CACHE_PATH = "ChatImageCache";
    public static int LIMIT_WIDTH = 0;
    public static int LIMIT_HEIGHT = 0;
    public static int PADDING_LEFT = 3;
    public static int PADDING_RIGHT = 3;
    public static int PADDING_TOP = 3;
    public static int PADDING_BOTTOM = 3;
    @Override
    public void onInitializeClient() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) ->
        {
            BlockState state = world.getBlockState(pos);
            player.sendMessage(ChatImageStyle.messageFromCode("[图片]","[CICode,url=https://blog.kituin.fun/img/bg.png]"));

            if (state.isToolRequired() && !player.isSpectator() &&
                    player.getMainHandStack().isEmpty())
            {
                player.damage(DamageSource.GENERIC, 1.0F);
            }
            return ActionResult.PASS;
        });

    }
}
