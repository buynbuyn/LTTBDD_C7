package com.example.chuong_7.ui.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.chuong_7.databinding.FragmentAudioBinding;
import java.io.IOException;

public class audioFragment extends Fragment {

    private FragmentAudioBinding binding;
    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private static final int REQUEST_PERMISSION_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        audioViewModel audioViewModel =
                new ViewModelProvider(this).get(audioViewModel.class);

        binding = FragmentAudioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        audioFilePath = requireContext().getExternalFilesDir(null) + "/audio_record.3gp";

        checkPermissions();

        binding.playButton.setOnClickListener(v -> playAudio());
        binding.recordButton.setOnClickListener(v -> startRecording());
        binding.stopButton.setOnClickListener(v -> stopEverything());
        binding.stopMusicButton.setOnClickListener(v -> stopMusic());

        return root;
    }

    private void startRecording() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.release();
                mediaRecorder = null;
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getContext(), "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error starting recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = MediaPlayer.create(getContext(), com.example.chuong_7.R.raw.music);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                Toast.makeText(getContext(), "Playback finished", Toast.LENGTH_SHORT).show();
                mediaPlayer.release();
                mediaPlayer = null;
            });
            Toast.makeText(getContext(), "Playing audio", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(getContext(), "Music stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopEverything() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(getContext(), "Recording stopped", Toast.LENGTH_SHORT).show();
            } catch (RuntimeException e) {
                Toast.makeText(getContext(), "No active recording to stop", Toast.LENGTH_SHORT).show();
            }
        }
        stopMusic();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission denied to record audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        binding = null;
    }
}
