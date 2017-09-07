package com.example.hazem.udacitymovieapp.Features.favorites.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hazem.udacitymovieapp.Base.Constants.NetworkUtil;
import com.example.hazem.udacitymovieapp.Features.DB.MovieContract;
import com.example.hazem.udacitymovieapp.Features.moviedetails.adapter.MovieTrailerAdapter;
import com.example.hazem.udacitymovieapp.R;
import com.example.hazem.udacitymovieapp.databinding.ItemFavoritesBinding;
import com.squareup.picasso.Picasso;

/**
 * Created by Hazem on 9/6/2017.
 */

public class FavoritesAdapter extends CursorAdapter{

    private Context context;
    private ItemClickListener itemClickListener;
    public FavoritesAdapter (Context context, Cursor c) {
        super (context, c);
        this.context=context;
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup viewGroup) {
        View view=null;
        FavoriteHolder holder;
        if(view==null)
        {
            view= LayoutInflater.from (context).inflate (R.layout.item_favorites,viewGroup,false);
            holder=new FavoriteHolder (view);
            view.setTag (holder);
        }
        return view;
    }

    @Override
    public Cursor swapCursor (Cursor newCursor) {
        super.swapCursor (newCursor);
        notifyDataSetChanged ();
        return super.swapCursor (newCursor);
    }

    @Override
    public void bindView (View view, Context context, Cursor cursor) {
        FavoriteHolder holder= (FavoriteHolder) view.getTag ();
        holder.setData (cursor);
    }

    public class FavoriteHolder  implements View.OnClickListener{
        ItemFavoritesBinding mBind;
        TextView favoriteTitle,favoriteDate,favoriteRate,favoriteTag;
        ImageView favoritePoster,favoriteHeart;
        int postion;
        public FavoriteHolder (View itemView) {
            mBind= DataBindingUtil.bind (itemView);
            favoriteTitle=mBind.tvFavoriteTitle;
            favoriteDate=mBind.tvFavoriteDate;
            favoriteRate=mBind.tvFavoriteRate;
            favoriteTag=mBind.tvFavoriteTag;
            favoritePoster=mBind.ivFavoritePoster;
            favoriteHeart=mBind.ivFavoriteHeart;
            itemView.setOnClickListener (this);
        }
        public void setData(Cursor cursor)
        {
            postion=cursor.getPosition ();
            String date=cursor.getString (cursor.getColumnIndex (MovieContract.FavoriteEntry.COLUMN_MOVIE_DATE));
            String title=cursor.getString (cursor.getColumnIndex (MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE));
            String tag=cursor.getString (cursor.getColumnIndex (MovieContract.FavoriteEntry.COLUMN_MOVIE_TAG));
            String rate=cursor.getString (cursor.getColumnIndex (MovieContract.FavoriteEntry.COLUMN_MOVIE_RATE));
            String poster=cursor.getString (cursor.getColumnIndex (MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER));
            favoriteTitle.setText (title);
            favoriteDate.setText (date);
            favoriteTag.setText (tag);
            favoriteRate.setText (rate);
            favoriteHeart.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    if(itemClickListener!=null)
                    {
//                        long id=getCursor ().getLong (getCursor ().getColumnIndex (MovieContract.FavoriteEntry._ID));
                        itemClickListener.onHeartClick (view, (int) getItemId (postion));
                    }
                }
            });
            Picasso.with (context).load (NetworkUtil.getXSmallPosterURL (poster)).placeholder (R.mipmap.ic_launcher).into (favoritePoster);
        }
        @Override
        public void onClick (View view) {
            if(itemClickListener!=null)
            {
//                long id=getCursor ().getLong (getCursor ().getColumnIndex (MovieContract.FavoriteEntry._ID));
                itemClickListener.onItemClick (view, (int) getItemId (postion));
            }
        }
    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    public interface ItemClickListener
    {

        public void onItemClick(View view,int ID);
        public void onHeartClick(View view,int ID);
    }
}
