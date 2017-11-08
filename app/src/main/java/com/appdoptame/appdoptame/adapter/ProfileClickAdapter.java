package com.appdoptame.appdoptame.adapter;

/**
 * Created by crow on 7/11/17.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdoptame.appdoptame.R;
import com.appdoptame.appdoptame.model.Profile;
import com.appdoptame.appdoptame.utils.Utils;
import com.arasthel.spannedgridlayoutmanager.SpanLayoutParams;
import com.arasthel.spannedgridlayoutmanager.SpanSize;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileClickAdapter extends RecyclerView.Adapter<ProfileClickAdapter.ProfileClickViewHolder> {

    public class ProfileClickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pet;
        TextView petName;

        ProfileClickViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            petName = (TextView) itemView.findViewById(R.id.pet_name);
            pet = (ImageView) itemView.findViewById(R.id.pet_image);

        }

        @Override
        public void onClick(View v) {
            // The user may not set a click listener for list items, in which case our listener
            // will be null, so we need to check for this
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }

        public void assignData(Profile profile){
            //pet.setImageURI(Uri.parse(profile.getPhotoUrl()));
            new ImageLoadTask(profile.getPhotoUrl(), pet).execute();
            //pet.setImageBitmap(new convertUrlToBitmap(profile).doInBackground() );
            petName.setText(profile.getName());

        }
    }

    private ArrayList<Profile> mCustomObjects;

    public ProfileClickAdapter(ArrayList<Profile> arrayList) {
        this.mCustomObjects = arrayList;
    }

    @Override
    public int getItemCount() {
        return mCustomObjects.size();
    }

    @Override
    public ProfileClickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_card, null, false);
        return new ProfileClickViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileClickViewHolder holder, int position) {
        holder.assignData(mCustomObjects.get(position));
        // My example assumes CustomClass objects have getFirstText() and getSecondText() methods
        //int width = 1;
        //int height = 2;

        //V2
        //holder.itemView.layoutParams = SpanLayoutParams(SpanSize(width, height));
        //V1
        //holder.itemView.setLayoutParams(new SpanLayoutParams(new SpanSize(width, height)));


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }

    private class convertUrlToBitmap extends AsyncTask<Void,Void,Bitmap> {
        private Profile profile;
        public convertUrlToBitmap(Profile profile){
            this.profile = profile;
        }
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(profile.getPhotoUrl());
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            }catch(Exception e){
                e.printStackTrace();
            }
            return myBitmap;
        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}
