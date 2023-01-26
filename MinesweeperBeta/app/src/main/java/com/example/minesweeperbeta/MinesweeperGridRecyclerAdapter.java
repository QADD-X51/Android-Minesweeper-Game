package com.example.minesweeperbeta;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MinesweeperGridRecyclerAdapter extends RecyclerView.Adapter<MinesweeperGridRecyclerAdapter.MinesweeperTileViewHolder> {
    private List<MinesweeperTile> tiles;
    private OnTileClickListener listener;

    public MinesweeperGridRecyclerAdapter(List<MinesweeperTile> tiles, OnTileClickListener listener)
    {
        this.listener = listener;
        this.tiles = tiles;
    }

    public void SetTiles(List<MinesweeperTile> tiles)
    {
        this.tiles = tiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MinesweeperTileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tile, parent, false);
        return new MinesweeperTileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MinesweeperTileViewHolder holder, int position) {
        holder.bind(tiles.get(position));
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }


    class MinesweeperTileViewHolder extends RecyclerView.ViewHolder
    {
        TextView valueTextView;

        public MinesweeperTileViewHolder(@NonNull View itemView) {
            super(itemView);

            valueTextView = itemView.findViewById(R.id.item_tile_value);
        }

        public void bind(final MinesweeperTile tile)
        {
            itemView.setBackgroundColor(Color.DKGRAY);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onTileClick(tile);
                }
            });

            if(tile.IsRevealed())
            {
                switch(tile.GetValue())
                {
                    case MinesweeperTile.Bomb:
                        itemView.setBackgroundColor(Color.GRAY);
                        valueTextView.setText(R.string.bomb);
                        break;
                    case MinesweeperTile.Blank:
                        valueTextView.setText("");
                        itemView.setBackgroundColor(Color.rgb(200,200,200));
                        break;
                    default:
                        itemView.setBackgroundColor(Color.GRAY);
                        valueTextView.setText(String.valueOf(tile.GetValue()));
                        switch(tile.GetValue())
                        {
                            case 1:
                                valueTextView.setTextColor(Color.BLUE);
                                break;
                            case 2:
                                valueTextView.setTextColor(Color.GREEN);
                                break;
                            case 3:
                                valueTextView.setTextColor(Color.RED);
                                break;
                            case 4:
                                valueTextView.setTextColor(Color.rgb(0,0,153));
                                break;
                            case 5:
                                valueTextView.setTextColor(Color.YELLOW);
                                break;
                            case 6:
                                valueTextView.setTextColor(Color.rgb(129,166,238));
                                break;
                            case 7:
                                valueTextView.setTextColor(Color.MAGENTA);
                                break;
                            case 8:
                                valueTextView.setTextColor(Color.DKGRAY);
                                break;
                        }

                        break;
                }
                return;
            }

            if(tile.IsFlagged())
            {
                valueTextView.setText(R.string.flag);
            }
        }
    }
}
