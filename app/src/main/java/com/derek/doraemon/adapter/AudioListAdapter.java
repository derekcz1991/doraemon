package com.derek.doraemon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.derek.doraemon.R;
import com.derek.doraemon.model.Audio;
import com.derek.doraemon.model.Message;
import com.derek.doraemon.view.viewholder.AudioViewHolder;
import com.derek.doraemon.view.viewholder.BaseViewHolder;
import com.derek.doraemon.view.viewholder.MessageViewHolder;

import java.util.List;

/**
 * Created by derek on 08/10/2016.
 */
public class AudioListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<Audio> audios;
    private AudioViewHolder.Callback callback;

    public AudioListAdapter(List<Audio> audios, AudioViewHolder.Callback callback) {
        this.audios = audios;
        this.callback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AudioViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_audio, null), callback);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.update(audios.get(position));
    }

    @Override
    public int getItemCount() {
        return audios == null ? 0 : audios.size();
    }
}
