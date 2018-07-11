package com.example.gabriel.puntodeapoyo.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.gabriel.puntodeapoyo.R;
import com.example.gabriel.puntodeapoyo.Services.SmsService;

/**
 * Implementation of App Widget functionality.
 */
public class ButtonWidget extends AppWidgetProvider {
    private static final String MyOnClick1 = "myOnClickTag1";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.button_widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.button_widget);
        Intent intent=new Intent(context,ButtonWidget.class);
        intent.setAction(MyOnClick1);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.button_widget,actionPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds,views);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MyOnClick1)){
            //Toast.makeText(context, "Widget presionado", Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent(context, SmsService.class);
            context.startService(intent1);
        }
        super.onReceive(context, intent);
    }
}

