package com.example.chuong_7.ui.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chuong_7.databinding.FragmentCameraBinding;
import com.example.chuong_7.ui.camera.cameraViewModel;
import android.content.Intent;

public class cameraFragment extends Fragment {
    private FragmentCameraBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cameraViewModel cameraViewModel =
                new ViewModelProvider(this).get(cameraViewModel.class);

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnCameraapi.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CameraAPIActivity.class));
        });

        binding.btnCommunicate.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CommunicateCameraActivity.class));
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
