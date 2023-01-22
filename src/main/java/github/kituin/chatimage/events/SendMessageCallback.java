package github.kituin.chatimage.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;



public interface SendMessageCallback {
/**
 * 剪羊毛时的回调。
 * 剪羊毛并掉落物品、物品被损害之前调用。
 * 会返回：
 * - SUCCESS 退出后续处理过程，然后继续进行正常剪羊毛行为。
 * - PASS 回落到后续处理过程，如果没有其他的监听器了，则默认为 SUCCESS。
 * - FAIL 退出后续处理过程，羊毛不会被剪掉。
 **/

    Event<SendMessageCallback> EVENT = EventFactory.createArrayBacked(SendMessageCallback.class,
            (listeners) -> (player, message) -> {
                for (SendMessageCallback listener : listeners) {
                    ActionResult result = listener.interact(player, message);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(PlayerEntity player, Text message);
}