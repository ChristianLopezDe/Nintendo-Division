package org.depaul.logic.board;

import org.depaul.logic.data.Score;
import org.depaul.logic.data.Lines;
import org.depaul.logic.data.ViewData;
import org.depaul.logic.events.MoveEvent;

public interface Board {


    boolean moveBrick(MoveEvent event);
    
    boolean createNewBrick();
    boolean rotateBrick();
    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    int clearLines();

    Score getScore();

    Lines getLines();

    void newGame();

    boolean rotateBrickOpposite();
}
