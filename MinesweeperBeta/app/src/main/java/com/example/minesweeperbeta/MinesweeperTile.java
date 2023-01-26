package com.example.minesweeperbeta;

public class MinesweeperTile {
    public static final int Bomb = -1;
    public static final int Blank = 0;

    private int value;
    private boolean isRevealed;
    private boolean isFlagged;

    public MinesweeperTile(int value)
    {
        this.value=value;
        this.isFlagged = false;
        this.isRevealed = false;
    }

    public int GetValue()
    {
        return value;
    }

    public boolean IsFlagged()
    {
        return isFlagged;
    }

    public void SetFlagged(boolean target)
    {
        isFlagged = target;
    }

    public boolean IsRevealed()
    {
        return isRevealed;
    }

    public void SetRevealed(boolean target)
    {
        isRevealed = target;
    }
}
