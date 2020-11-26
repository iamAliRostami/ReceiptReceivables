package com.leon.receipt_receivables.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.leon.receipt_receivables.R;
import com.leon.receipt_receivables.activities.MainActivity;
import com.leon.receipt_receivables.enums.DialogType;
import com.leon.receipt_receivables.utils.custom_dialogue.LovelyStandardDialog;


public class CustomDialog {
    @SuppressLint("StaticFieldLeak")
    static LovelyStandardDialog lovelyStandardDialog;
    Context context;
    String Top, Title, Message, ButtonText;

    public CustomDialog(DialogType choose, Context context, String message, String title, String top, String buttonText) {
        this.context = context;
        Message = message;
        Title = title;
        Top = top;
        ButtonText = buttonText;
        lovelyStandardDialog = new LovelyStandardDialog(context)
                .setTitle(Title)
                .setMessage(Message)
                .setTopTitle(Top);
        if (choose == DialogType.Green)
            CustomGreenDialog(this.context, ButtonText);
        else if (choose == DialogType.Yellow)
            CustomYellowDialog(this.context, ButtonText);
        else if (choose == DialogType.Red)
            CustomRedDialog(this.context, ButtonText);
        else if (choose == DialogType.GreenRedirect)
            CustomGreenDialogRedirect(this.context, ButtonText);
        else if (choose == DialogType.YellowRedirect)
            CustomYellowDialogRedirect(this.context, ButtonText);
        else if (choose == DialogType.RedRedirect)
            CustomRedDialogRedirect(this.context, ButtonText);
    }

    public static void CustomGreenDialogRedirect(final Context context, String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.green)
                .setTopTitleColor(context.getResources().getColor(R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_green_1)
                .setPositiveButton(ButtonText, v -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                });
        lovelyStandardDialog.show();
    }

    public static void CustomYellowDialogRedirect(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopTitleColor(context.getResources().getColor(R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_yellow_1)
                .setTopColorRes(R.color.yellow)
                .setPositiveButton(buttonText, v -> {
                })
                .show();
    }

    public static void CustomRedDialogRedirect(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red)
                .setTopTitleColor(context.getResources().getColor(R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_red_1)
                .setPositiveButton(buttonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public static void CustomGreenDialog(final Context context, String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.green)
                .setTopTitleColor(context.getResources().getColor(R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_green_1)
                .setPositiveButton(ButtonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public static void CustomYellowDialog(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopTitleColor(context.getResources().getColor(R.color.text_color_light))
                .setTopColorRes(R.color.yellow)
                .setButtonsBackground(R.drawable.border_yellow_1)
                .setPositiveButton(buttonText, v -> {
                })
                .show();
    }

    public static void CustomRedDialog(final Context context, String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red)
                .setTopTitleColor(context.getResources().getColor(R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_red_1)
                .setPositiveButton(buttonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public interface Inline {
        void inline(String negative, int negativeColor);
    }
}
