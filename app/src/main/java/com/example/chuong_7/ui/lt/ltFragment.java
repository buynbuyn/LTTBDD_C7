package com.example.chuong_7.ui.lt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chuong_7.databinding.FragmentLtBinding;
import com.example.chuong_7.ui.lt.ltViewModel;
import android.content.Intent;

public class ltFragment extends Fragment {
    private FragmentLtBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ltViewModel ltViewModel =
                new ViewModelProvider(this).get(ltViewModel.class);

        binding = FragmentLtBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLt.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MultimediaPlayerActivity.class));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
