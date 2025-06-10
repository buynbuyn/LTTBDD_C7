package com.example.chuong_7.ui.http;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chuong_7.databinding.FragmentHttpBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class httpFragment extends Fragment {
    private FragmentHttpBinding binding;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        httpViewModel httpViewModel =
                new ViewModelProvider(this).get(httpViewModel.class);

        binding = FragmentHttpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        fetchDataFromInternet();

        return root;
    }

    private void fetchDataFromInternet() {
        executorService.execute(() -> {
            String result = "";
            try {
                // Dùng một API công khai miễn phí, bạn có thể thay đổi
                URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line).append("\n");
                    }
                    in.close();
                    result = response.toString();
                } else {
                    result = "Lỗi kết nối: " + responseCode;
                }

                String finalResult = result;
                mainHandler.post(() -> binding.textHttp.setText(finalResult));
            } catch (Exception e) {
                String error = "Lỗi: " + e.getMessage();
                mainHandler.post(() -> binding.textHttp.setText(error));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
