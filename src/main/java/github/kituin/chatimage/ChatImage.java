package github.kituin.chatimage;

import github.kituin.chatimage.events.SendMessageCallback;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

/**
 * @author kitUIN
 */
public class ChatImage implements ModInitializer {

    @Override
    public void onInitialize() {

        SendMessageCallback.EVENT.register((player, message) -> {
            MutableText messages = (MutableText) message;
            for (Text t:
            messages.getSiblings()) {
                System.out.println(t.getString());
            }
            return ActionResult.PASS;
        });
    }
}
