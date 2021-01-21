package com.example.covidtracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.KeyEvent;
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
        title.setText("About!");

        TextView description = (TextView) dialog.findViewById(R.id.help_desc);
        description.setText("Covid Tracker jest aplikacją służącą do przeglądania statystyk dotyczących zarażeń virusem Covid-19.\n" +
                "\nAby znaleźć statystyki dla danego kraju należy nacisnąć na przycisk z nazwą kontynentu na którym znajduje się wybrany kraj, a następnie znaleźć państwo na liście. Przez listę można scrollować. Państwo można również znaleźć korzystając z wyszukiwarki znajdującej państwa. \n" +
                "\nPaństwa można dodać do ulubionych, aby móc mieć do nich szybki i łatwy dostęp przy ponownym uruchomieniu aplikacji. Aby dodać państwo do ulubionych, po wybraniu państwa z listy należy kliknąć serduszko, aby dodać lub usunąć je z listy ulubionych. Można uzyskać dostęp do swoich ulubionych krajów klikając przycisk Favourite z poziomu menu głównego.");

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

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    dialog.cancel();
                    return true;
                }
                return false;
            }
        });
    }
}
