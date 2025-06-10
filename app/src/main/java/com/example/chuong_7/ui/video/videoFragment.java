package com.example.chuong_7.ui.video;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chuong_7.R;
import com.example.chuong_7.databinding.FragmentVideoBinding;
import com.example.chuong_7.ui.video.videoViewModel;

public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videoViewModel videoViewModel =
                new ViewModelProvider(this).get(videoViewModel.class);

        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Thêm VideoView và thiết lập video
        VideoView videoView = binding.videoView;
        Uri videoUri = Uri.parse("android.resource://" + requireActivity().getPackageName() + "/" + R.raw.video1);
        videoView.setVideoURI(videoUri);
        videoView.start();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}