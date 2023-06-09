package org.depaul.logic.events;

import org.depaul.logic.data.ViewData;

public interface InputEventListener {

    ViewData onMoveEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();
}
