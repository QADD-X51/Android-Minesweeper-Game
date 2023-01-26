package com.example.minesweeperbeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperGrid {
    private List<MinesweeperTile> tiles;
    private int size;

    public MinesweeperGrid(int size)
    {
        this.size=size;
        tiles = new ArrayList<>();
        for(int index=0; index < size*size; ++index)
        {
            tiles.add(new MinesweeperTile(MinesweeperTile.Blank));
        }
    }

    public void GenerateGame(int bombCount)
    {
        GenerateBombs(bombCount);
        GenerateNumberedTiles();
    }

    private void GenerateBombs(int totalBombCount)
    {
        int randomIndex;
        boolean tileFound;

        for(int bombCount = 0; bombCount<totalBombCount;++bombCount)
        {
            randomIndex = new Random().nextInt(size * size);
            tileFound = false;

            for(int index = randomIndex ; index<size*size ; ++index)
            {
                if(tiles.get(index).GetValue() == MinesweeperTile.Blank)
                {
                    tiles.set(index, new MinesweeperTile(MinesweeperTile.Bomb));
                    tileFound = true;
                    break;
                }
            }

            if(tileFound)
            {
                continue;
            }

            for(int index = 0 ; index < randomIndex ; ++index)
            {
                if(tiles.get(index).GetValue() == MinesweeperTile.Blank)
                {
                    tiles.set(index, new MinesweeperTile(MinesweeperTile.Bomb));
                    tileFound = true;
                    break;
                }
            }

            if(!tileFound)
            {
                break;
            }

        }
    }

    private void GenerateNumberedTiles()
    {
        List<Integer> tilesToCheckIndex = new ArrayList<>();
        List<Integer> surroundingTilesIndex;

        for(int indexX = 0; indexX < size; ++indexX)
        {
            for(int indexY = 0; indexY < size; ++indexY)
            {
                if(GetTileByCoord(indexX,indexY).GetValue() == MinesweeperTile.Bomb)
                {
                    surroundingTilesIndex = SurroundingTilesIndex(indexX, indexY);
                    for(int tileIndex: surroundingTilesIndex)
                    {
                        if(tiles.get(tileIndex).GetValue() != MinesweeperTile.Bomb)
                        {
                            tilesToCheckIndex.add(tileIndex);
                        }
                    }
                }
            }
        }

        List<MinesweeperTile> surroundingTiles;
        int[] tileCoords;
        int bombCount;

        for(int tileIndex : tilesToCheckIndex)
        {
            if(tiles.get(tileIndex).GetValue() != MinesweeperTile.Blank)
            {
                continue;
            }
            tileCoords = IndexToCoords(tileIndex);
            surroundingTiles = SurroundingTiles(tileCoords[0], tileCoords[1]);
            bombCount = 0;

            for(MinesweeperTile tile:surroundingTiles)
            {
                if(tile.GetValue() == MinesweeperTile.Bomb)
                {
                    ++bombCount;
                }
            }

            if(bombCount != 0)
            {
                tiles.set(tileIndex, new MinesweeperTile(bombCount));
            }
        }
    }

    public int CoordsToIndex(int x, int y)
    {
        return x + y * size;
    }

    public int[] IndexToCoords(int index)
    {
        int y = index/size;
        return new int[]{index - (y * size), y};
    }

    public MinesweeperTile GetTileByCoord(int x, int y)
    {
        if(x<0 || x>=size || y<0 || y>=size)
        {
            return null;
        }
        return tiles.get(CoordsToIndex(x,y));
    }

    public List<MinesweeperTile> SurroundingTiles(int x,int y)
    {
        List<MinesweeperTile> surroundingTiles = new ArrayList<>();

        List<MinesweeperTile> allSurroundingTiles = new ArrayList<>();
        allSurroundingTiles.add(GetTileByCoord(x-1,y-1));
        allSurroundingTiles.add(GetTileByCoord(x-1,y));
        allSurroundingTiles.add(GetTileByCoord(x-1,y+1));
        allSurroundingTiles.add(GetTileByCoord(x,y-1));
        allSurroundingTiles.add(GetTileByCoord(x,y+1));
        allSurroundingTiles.add(GetTileByCoord(x+1,y-1));
        allSurroundingTiles.add(GetTileByCoord(x+1,y));
        allSurroundingTiles.add(GetTileByCoord(x+1,y+1));

        for(MinesweeperTile tile: allSurroundingTiles)
        {
            if(tile != null)
            {
                surroundingTiles.add(tile);
            }
        }

        return surroundingTiles;
    }

    private List<Integer> SurroundingTilesIndex(int x,int y)
    {
        List<Integer> surroundingTiles = new ArrayList<>();

        if(x != 0)
        {
            if(y != 0)
                surroundingTiles.add(CoordsToIndex(x-1,y-1));

            surroundingTiles.add(CoordsToIndex(x-1,y));

            if(y != size - 1)
                surroundingTiles.add(CoordsToIndex(x-1,y+1));
        }

        if(y != 0)
            surroundingTiles.add(CoordsToIndex(x,y-1));

        if(y != size-1)
            surroundingTiles.add(CoordsToIndex(x,y+1));

        if(x != size - 1)
        {
            if(y != 0)
                surroundingTiles.add(CoordsToIndex(x+1,y-1));

            surroundingTiles.add(CoordsToIndex(x+1,y));

            if(y != size - 1)
                surroundingTiles.add(CoordsToIndex(x+1,y+1));
        }


        return surroundingTiles;
    }

    public void RevealAllBomb()
    {
        for(MinesweeperTile tile: tiles)
        {
            if(tile.GetValue() == MinesweeperTile.Bomb && !tile.IsFlagged())
            {
                tile.SetRevealed(true);
            }
        }
    }

    public List<MinesweeperTile> GetTiles()
    {
        return tiles;
    }
}
