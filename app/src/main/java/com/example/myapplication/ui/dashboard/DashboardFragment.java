package com.example.myapplication.ui.dashboard;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.cretin.www.wheelsruflibrary.listener.RotateListener;
import com.cretin.www.wheelsruflibrary.view.WheelSurfView;

import java.util.Random;

public class DashboardFragment extends Fragment {

    private WheelSurfView wheelSurfView;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        wheelSurfView=root.findViewById(R.id.wheelSurfView1);

        wheelSurfView.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                Toast.makeText(root.getContext(), "你今天该吃： " + des, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {

            }

            @Override
            public void rotateBefore(ImageView goImg) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                builder.setTitle("");
                builder.setMessage("让我帮你完成人生一大难题吧！");
                builder.setPositiveButton("告诉我今天吃啥？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //模拟位置
                        int position = new Random().nextInt(7) + 1;
                        wheelSurfView.startRotate(position);
                    }
                });
                builder.setNegativeButton("我才不感兴趣呢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });
        return root;
    }
}
