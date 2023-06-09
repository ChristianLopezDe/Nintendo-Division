package org.depaul.logic.board;

import org.depaul.logic.events.EventSource;
import org.depaul.logic.events.EventType;
import org.depaul.logic.events.MoveEvent;
import org.junit.Before;
import org.junit.Test;


import java.awt.*;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleBoardTest {

    SimpleBoard board;
    static final int WIDTH = 10;
    static final int HEIGHT = 10;

    @Before
    public void setup() {
        board = new SimpleBoard(WIDTH, HEIGHT);
        board.newGame();
    }


    @Test
    public void moveBrickLeftEvent() {
        // given left key tap event with fresh brick generated
        final MoveEvent moveLeft = new MoveEvent(EventType.LEFT, EventSource.THREAD);
        final int inital_x = board.getViewData().getxPosition();
        final int inital_y = board.getViewData().getyPosition();

        // when left events are picked up
        board.moveBrick(moveLeft);

        // brick should move left by one on X axis while the Y position remains the same
        assertEquals(inital_x-1, board.getViewData().getxPosition());
        assertEquals(inital_y, board.getViewData().getyPosition());

        board.moveBrick(moveLeft);
        assertEquals(inital_x-2, board.getViewData().getxPosition());
        assertEquals(inital_y, board.getViewData().getyPosition());

        board.moveBrick(moveLeft);
        assertEquals(inital_x-3, board.getViewData().getxPosition());
        assertEquals(inital_y, board.getViewData().getyPosition());
    }

    @Test
    public void moveBrickRightEvent() {
        final MoveEvent moveRight = new MoveEvent(EventType.RIGHT, EventSource.THREAD);
        final int inital_x = board.getViewData().getxPosition();
        final int inital_y = board.getViewData().getyPosition();

        board.moveBrick(moveRight);
        assertEquals(inital_x+1, board.getViewData().getxPosition());
        assertEquals(inital_y, board.getViewData().getyPosition());

        board.moveBrick(moveRight);
        assertEquals(inital_x+2, board.getViewData().getxPosition());
        assertEquals(inital_y, board.getViewData().getyPosition());

        board.moveBrick(moveRight);
        assertEquals(inital_x+3, board.getViewData().getxPosition());
        assertEquals(inital_y, board.getViewData().getyPosition());
    }

    @Test
    public void moveBrickDownEvent() {
        final MoveEvent moveDown = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        final int initial_x = board.getViewData().getxPosition();
        final int initial_y = board.getViewData().getyPosition();

        board.moveBrick(moveDown);
        assertEquals(initial_y+1, board.getViewData().getyPosition());
        assertEquals(initial_x, board.getViewData().getxPosition());

        board.moveBrick(moveDown);
        assertEquals(initial_y+2, board.getViewData().getyPosition());
        assertEquals(initial_x, board.getViewData().getxPosition());

        board.moveBrick(moveDown);
        assertEquals(initial_y+3, board.getViewData().getyPosition());
        assertEquals(initial_x, board.getViewData().getxPosition());
    }


    @Test
    public void instantDropBrick() throws NoSuchFieldException, IllegalAccessException {
        final MoveEvent spaceBarEvent = new MoveEvent(EventType.SPACE, EventSource.THREAD);
        final Field currentOffset = SimpleBoard.class.getDeclaredField("currentOffset");
        currentOffset.setAccessible(true);
        assertEquals(0, ((Point)currentOffset.get(board)).getY(), 0);

        board.moveBrick(spaceBarEvent);
        final double bottomValue = ((Point) currentOffset.get(board)).getY();
        assertTrue(bottomValue >= 7); // depending on brick height


        board.moveBrick(spaceBarEvent);
        // already at bottom, should not change
        assertEquals(bottomValue, ((Point)currentOffset.get(board)).getY(), 0);
    }


    @Test
    public void rotateBrick() {
        final MoveEvent ZEvent = new MoveEvent(EventType.Z, EventSource.THREAD);
        final MoveEvent rightEvent = new MoveEvent(EventType.RIGHT, EventSource.THREAD);

        assertTrue(board.moveBrick(ZEvent));

        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);
        board.moveBrick(rightEvent);

        assertTrue(board.moveBrick(ZEvent));
    }


}