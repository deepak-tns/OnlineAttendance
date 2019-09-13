package com.india.lhq.onlineattendance.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.activity.AllActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {


    private LinearLayout linear_att_anywhere, linear_att_fix , linear_infraDetail,linear_AttendanceRegularization,linear_attendance_approval;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        findIds(v);
        return v;
    }

    private void findIds(View v) {
        linear_att_anywhere = v.findViewById(R.id.linear_att_anywhere);
        linear_att_fix = v.findViewById(R.id.linear_att_fix);
        linear_infraDetail = v.findViewById(R.id.linear_infraDetail);
        linear_AttendanceRegularization = v.findViewById(R.id.linear_AttendanceRegularization);
        linear_attendance_approval = v.findViewById(R.id.linear_attendance_approval);

        linear_att_anywhere.setOnClickListener(this);
        linear_att_fix.setOnClickListener(this);
        linear_infraDetail.setOnClickListener(this);
        linear_AttendanceRegularization.setOnClickListener(this);
        linear_attendance_approval.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == linear_att_anywhere) {
            Intent intent = new Intent(new Intent(getActivity(), AllActivity.class));
            intent.putExtra("MESSAGENAME", "AttendanceRemote");
            startActivity(intent);
        }
        if (v == linear_att_fix) {
            Intent intent = new Intent(new Intent(getActivity(), AllActivity.class));
            intent.putExtra("MESSAGENAME", "AttendanceFix");
            startActivity(intent);
        }
        if (v == linear_AttendanceRegularization) {
            Intent intent = new Intent(new Intent(getActivity(), AllActivity.class));
            intent.putExtra("MESSAGENAME", "AttendanceRegularization");
            startActivity(intent);
        }
        if (v == linear_attendance_approval) {
            Intent intent = new Intent(new Intent(getActivity(), AllActivity.class));
            intent.putExtra("MESSAGENAME", "AttendanceApproval");
            startActivity(intent);
        }
        if (v == linear_infraDetail) {
            Intent intent = new Intent(new Intent(getActivity(), AllActivity.class));
            intent.putExtra("MESSAGENAME", "InfraDetail");
            startActivity(intent);
        }
    }
}
