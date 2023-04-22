package com.pickpockethelper;

import com.pickpockethelper.entity.Session;
import com.pickpockethelper.utility.AlertID;
import com.pickpockethelper.utility.AlertType;
import net.runelite.client.chat.ChatColorType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles executing alerts through chat messages, notifications, or audio files.
 */
@Singleton
public class AlertManager {
    @Inject
    private FeedbackManager feedbackManager;
    @Inject
    private AudioManager audioManager;

	@Inject
	private Session session;

    @Inject
    private PickpocketHelperConfig config;

    private final static Map<Integer, String> messages = new HashMap<>();

    public AlertManager() {
        messages.put(AlertID.DODGY_BREAK, "Your dodgy necklace broke!");
        messages.put(AlertID.HITPOINTS_LOW, "Your hitpoints are low!");
        messages.put(AlertID.SHADOW_VEIL_FADED, "Shadow Veil has faded!");
        messages.put(AlertID.TARGET_DESPAWN, "Your target is about to de-spawn!");
        messages.put(AlertID.ROGUE_SET_INCOMPLETE, "You are not wearing the full rogue set!");
        messages.put(AlertID.SPLASHER_IDLE, "Your target is no longer being splashed!");
        messages.put(AlertID.PLAYER_IDLE, "You are no longer pickpocketing!");
        messages.put(AlertID.NO_INVENTORY_SPACE, "There is no space for your loot!");
		messages.put(AlertID.GLOVES_BREAK, "Your gloves of silence are about to break!");
        messages.put(AlertID.PICKPOCKET_PREVENTED, "Pickpocket prevented. Eat some food to continue.");
    }

    /**
     * Send an alert to the player. Only sends alerts while the session is active.
     * @param alertId alert to be executed.
     * @param includeChatMessage whether to include a chat message.
     */
    public void sendAlert(int alertId, boolean includeChatMessage) {
		if (!session.isActive()) {
			return;
		}

        AlertType type = config.getAlertType();

        if(type == AlertType.CHAT_MESSAGE || includeChatMessage) {
            feedbackManager.sendChatMessage(messages.get(alertId), ChatColorType.HIGHLIGHT);
        }

		boolean audioSuccess = true;
		if(type == AlertType.SPEECH) {
			audioSuccess = audioManager.play(alertId);
		}

		if(type == AlertType.NOTIFICATION || !audioSuccess) {
			feedbackManager.sendNotification(messages.get(alertId), false);
		}
    }
}
