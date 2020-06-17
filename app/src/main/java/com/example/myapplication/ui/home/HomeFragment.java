package com.example.myapplication.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.DataBase.DatabaseHelper;
import com.example.myapplication.DataBase.SQLManager;
import com.example.myapplication.Login;
import com.example.myapplication.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    SQLManager sqlManager;
    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
    String username = "1710218";


    View root;

    //星期几
    private RelativeLayout day;

    LinearLayout leftViewLayout;
    LayoutInflater inflater;

    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.inflater = inflater;

        Intent intent = getActivity().getIntent();
        username = intent.getStringExtra("username");

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);

        setHasOptionsMenu(true);
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        leftViewLayout = root.findViewById(R.id.left_view_layout);

        sqlManager = new SQLManager(root.getContext());

        loadData();

        return root;
    }

    //从数据库加载数据
    private void loadData() {
        ArrayList<Course> coursesList = sqlManager.getCourses(username);
        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
            createLeftView(course);
            createItemCourseView(course);
        }
    }

    //创建"第几节数"视图
    private void createLeftView(Course course) {
        int endNumber = course.getEnd();
        if (endNumber > maxCoursesNumber) {
            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
                View view = inflater.inflate(R.layout.left_view, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text = view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                leftViewLayout.addView(view);
            }
            maxCoursesNumber = endNumber;
        }
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getStart() > course.getEnd()) {
            Toast.makeText(getActivity(), "请检查输入信息~", Toast.LENGTH_LONG).show();
        }
        else {
            int dayId = 0;
            switch (getDay) {
                case 1: dayId = R.id.monday; break;
                case 2: dayId = R.id.tuesday; break;
                case 3: dayId = R.id.wednesday; break;
                case 4: dayId = R.id.thursday; break;
                case 5: dayId = R.id.friday; break;
                case 6: dayId = R.id.saturday; break;
                case 7: dayId = R.id.weekday; break;
            }
            day = root.findViewById(dayId);

            int height = 180;
            View v = inflater.inflate(R.layout.course_card, null); //加载单个课程布局
            v.setY(height * (course.getStart()-1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,(course.getEnd()-course.getStart()+1)*height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n" + course.getTeacher() + "\n" + course.getClassRoom()); //显示课程名
            day.addView(v);
            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setVisibility(View.GONE);
                    day.removeView(v);

                    sqlManager.delCourse(username, course);
                    return true;
                }
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_courses:
                Intent intent = new Intent(getActivity(), AddCourseActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.relogin:
                sqlManager.delAutoLogin();
                Intent intent1 = new Intent(getActivity(), Login.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            createLeftView(course);
            createItemCourseView(course);
            sqlManager.addCourse(username, course);
        }
    }

}