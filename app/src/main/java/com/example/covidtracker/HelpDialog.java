package com.example.covidtracker;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class HelpDialog {
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.help_popup);

        TextView title = (TextView) dialog.findViewById(R.id.help_title);
        title.setText("przekozacki CovidTracker!");

        TextView description = (TextView) dialog.findViewById(R.id.help_desc);
        description.setText("linijka1\nlinijka2\n\n\n\n\n\n\ndddduuuuuuuuppppaaaa\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nscroll");

        Button dialogButton = (Button) dialog.findViewById(R.id.button_end);
        dialogButton.setTextColor(Color.parseColor("#c0c0c0"));
        dialogButton.setBackground(activity.getResources().getDrawable(R.drawable.customized_button_normal));
        dialogButton.setTextColor(activity.getResources().getColor(R.color.dark_sienna));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
