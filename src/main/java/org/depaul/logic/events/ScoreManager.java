package org.depaul.logic.events;

import org.depaul.gui.GuiController;
import org.depaul.logic.data.Lines;
import org.depaul.logic.data.Score;

public class ScoreManager {

    private final GuiController viewGuiController;
    private final Score score;
    private final Lines lines;

    private int linesCleared;
    private int nLinesToBonusPoints;
    private int nBonusPoints;
    private int nCurrentBonusPoints;

    private int pointsToAdd = 0;
    private int linesToAdd = 0;

    public ScoreManager(GuiController c, Score s, Lines l) {
        viewGuiController = c;
        score = s;
        lines = l;
        linesCleared = 0;
        nLinesToBonusPoints = 10;
        nBonusPoints = 40;
        //Set to 0 because on first line cleared
        //it will increase to nBonusPOints
        nCurrentBonusPoints = 0;
    }

    public void addBrickScore(int n){
        //Prevent scores of 0 from showing to keep
        //logic in other classes simple
        if(n == 0) return;

        //Each brick worth 10 points for a baseline
        int points = n * 10;

        pointsToAdd += points;
    }

    public void addLinesCleared(int n){
        //Prevent lines of 0 from showing to keep
        //logic in other classes simple
        if(n == 0) return;
        for(int i = 0; i < n; i++, pointsToAdd += nCurrentBonusPoints, linesCleared++){
            //Every nLinesToBonusPoints number of lines cleared increase
            //point value of clearing a line by nBonusPoints
            if(linesCleared % nLinesToBonusPoints == 0) nCurrentBonusPoints += nBonusPoints;
        }
    }
    public void deletedLinesCount(int n){
        if(n == 0) return;
        int points = n + linesCleared;
        linesToAdd+=points;
    }

    public void scorePoints(){
        viewGuiController.ShowScore(pointsToAdd);
        score.add(pointsToAdd);
        pointsToAdd = 0;
    }
    public void linePoints(){
        lines.add(linesToAdd);
        linesToAdd = 0;
    }

    public void reset(){
        linesCleared = 0;
        nCurrentBonusPoints = 0;
    }
}
