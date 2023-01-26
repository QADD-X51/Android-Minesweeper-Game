package com.example.minesweeperbeta;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame {

    private MinesweeperGrid mineGrid;
    private boolean clearingMode; //if in clear or flag mode
    private boolean gameOver;
    private int flagCount;

    public MinesweeperGame(int size, int bombCount)
    {
        mineGrid = new MinesweeperGrid(size);
        mineGrid.GenerateGame(bombCount);
        flagCount = bombCount;
        this.clearingMode=true;
        this.gameOver=false;
    }

    public void TileClickHandler(MinesweeperTile tile)
    {
        if(!gameOver)
        {
            if(clearingMode)
            {
                Clear(tile);
                return;
            }
            FlagTile(tile);
        }
    }

    public void Clear(MinesweeperTile tile)
    {
        if(tile.IsFlagged())
        {
            return;
        }

        int index = GetGrid().GetTiles().indexOf(tile);

        GetGrid().GetTiles().get(index).SetRevealed(true);

        if(tile.GetValue() == MinesweeperTile.Blank)
        {
            List<MinesweeperTile> tilesToCheck = new ArrayList<>();
            List<MinesweeperTile> tilesToClear = new ArrayList<>();

            tilesToCheck.add(tile);

            while(tilesToCheck.size() > 0)
            {
                MinesweeperTile currentTile = tilesToCheck.get(0);
                tilesToClear.add(currentTile);

                if(currentTile.GetValue() == MinesweeperTile.Blank)
                {
                    int[] currentTileXY = GetGrid().IndexToCoords(GetGrid().GetTiles().indexOf(currentTile));
                    for(MinesweeperTile adjacent : GetGrid().SurroundingTiles(currentTileXY[0], currentTileXY[1]))
                    {
                        if(!tilesToClear.contains(adjacent))
                        {
                            tilesToCheck.add(adjacent);
                        }
                    }
                }

                tilesToCheck.remove(currentTile);
            }

            for(MinesweeperTile cell: tilesToClear)
            {
                if(!cell.IsFlagged())
                    cell.SetRevealed(true);
            }

            return;
        }

        if(tile.GetValue() == MinesweeperTile.Bomb)
        {
            gameOver=true;
        }
    }

    public void FlagTile(MinesweeperTile tile)
    {
        if(!tile.IsRevealed())
        {
            tile.SetFlagged(!tile.IsFlagged());

            if(tile.IsFlagged())
            {
                --flagCount;
                return;
            }
            ++flagCount;
        }
    }

    public boolean CheckWin()
    {
        int unrevealedTilesCount = 0;
        for(MinesweeperTile tile: GetGrid().GetTiles())
        {
            if(tile.GetValue() != MinesweeperTile.Bomb && !tile.IsRevealed())
            {
                ++unrevealedTilesCount;
            }
        }

        if(unrevealedTilesCount == 0)
        {
            gameOver = true;
            return true;
        }
        return false;
    }

    public MinesweeperGrid GetGrid()
    {
        return mineGrid;
    }

    public boolean IsGameOver()
    {
        return gameOver;
    }

    public void SetClearingMode(boolean input)
    {
        clearingMode = input;
    }

    public boolean GetClearingMode()
    {
        return clearingMode;
    }

    public int GetFlagCount()
    {
        return flagCount;
    }
}
