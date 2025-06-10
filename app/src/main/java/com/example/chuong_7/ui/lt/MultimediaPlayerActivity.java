package com.example.chuong_7.ui.lt;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chuong_7.R;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.IOException;

public class MultimediaPlayerActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
    private VideoView videoView;
    private ExoPlayer exoPlayer;
    private static final int REQUEST_CODE = 100;
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private String outputFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_multimedia_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần
        videoView = findViewById(R.id.videoView);
        PlayerView playerView = findViewById(R.id.playerView);
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        // Thiết lập file đầu ra cho MediaRecorder
        outputFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/screen_recording.mp4";
        mediaRecorder = new MediaRecorder();

        // Nút phát và dừng âm thanh (sử dụng mediaPlayer1)
        Button btnMediaPlayer1 = findViewById(R.id.btn_mediaPlayer1);
        btnMediaPlayer1.setOnClickListener(v -> {
            if (mediaPlayer1 != null) {
                mediaPlayer1.release();
            }
            mediaPlayer1 = MediaPlayer.create(getApplicationContext(), R.raw.ngumotminh);
            if (mediaPlayer1 == null) {
                Toast.makeText(getApplicationContext(), "Không thể phát!", Toast.LENGTH_SHORT).show();
                return;
            }
            mediaPlayer1.start();
        });

        Button btnStopAudio = findViewById(R.id.btn_stop_audio);
        btnStopAudio.setOnClickListener(v -> {
            if (mediaPlayer1 != null && mediaPlayer1.isPlaying()) {
                mediaPlayer1.stop();
                mediaPlayer1.release();
                mediaPlayer1 = null;
                Toast.makeText(this, "Dừng âm thanh", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút phát và dừng video (sử dụng VideoView)
        Button btnPlayVideo = findViewById(R.id.btn_play_video);
        btnPlayVideo.setOnClickListener(v -> {
            Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.daxinhconthongminh);
            videoView.setVideoURI(videoUri);
            videoView.start();
            Toast.makeText(this, "Phát video", Toast.LENGTH_SHORT).show();
        });

        Button btnStopVideo = findViewById(R.id.btn_stop_video);
        btnStopVideo.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
                Toast.makeText(this, "Dừng video", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút phát âm thanh qua prepare đồng bộ
        Button btnMediaPlayer2 = findViewById(R.id.btn_mediaPlayer2);
        btnMediaPlayer2.setOnClickListener(v -> {
            if (mediaPlayer2 != null) {
                mediaPlayer2.release();
            }
            mediaPlayer2 = new MediaPlayer();
            try {
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ngumotminh);
                mediaPlayer2.setDataSource(getApplicationContext(), uri);
                mediaPlayer2.prepare();
                mediaPlayer2.start();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Không thể phát!", Toast.LENGTH_SHORT).show();
                if (mediaPlayer2 != null) {
                    mediaPlayer2.release();
                    mediaPlayer2 = null;
                }
            }
        });

        // Nút phát âm thanh qua prepare bất đồng bộ
        Button btnMediaPlayer3 = findViewById(R.id.btn_mediaPlayer3);
        btnMediaPlayer3.setOnClickListener(v -> {
            if (mediaPlayer3 != null) {
                mediaPlayer3.release();
            }
            mediaPlayer3 = new MediaPlayer();
            try {
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.aaa);
                mediaPlayer3.setDataSource(getApplicationContext(), uri);
                mediaPlayer3.setOnPreparedListener(mp -> mp.start());
                mediaPlayer3.prepareAsync();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Không thể phát!", Toast.LENGTH_SHORT).show();
                if (mediaPlayer3 != null) {
                    mediaPlayer3.release();
                    mediaPlayer3 = null;
                }
            }
        });


        MediaItem mediaItem = MediaItem.fromUri("android.resource://" + getPackageName() + "/" + R.raw.camkithihoa);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
        Toast.makeText(this, "Phát âm thanh", Toast.LENGTH_SHORT).show();

        // Nút quay và dừng màn hình
        Button btnStartRecording = findViewById(R.id.btn_start_recording);
        btnStartRecording.setOnClickListener(v -> startScreenRecording());

        Button btnStopRecording = findViewById(R.id.btn_stop_recording);
        btnStopRecording.setOnClickListener(v -> stopScreenRecording());
    }

    private void startScreenRecording() {
        MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mediaProjection = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE))
                    .getMediaProjection(resultCode, data);

            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setVideoSize(1280, 720);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setOutputFile(outputFile);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(this, "Đang quay màn hình...", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi chuẩn bị quay!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopScreenRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            Toast.makeText(this, "Đã dừng quay, file lưu tại: " + outputFile, Toast.LENGTH_LONG).show();
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer1 != null) {
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
        if (mediaPlayer3 != null) {
            mediaPlayer3.release();
            mediaPlayer3 = null;
        }
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }
}