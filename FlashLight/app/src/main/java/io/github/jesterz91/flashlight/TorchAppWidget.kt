package io.github.jesterz91.flashlight

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class TorchAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) { // 위젯이 여러 개 배치되었다면 모든 위젯을 업데이트
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // 위젯이 처음 생성될 때 호출
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    // 여러 개일 경우 마지막 위젯이 제거될 때 호출
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        // 위젯을 업데이트할 때 수행되는 코드
        internal fun updateAppWidget(context: Context,
                                     appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val widgetText = context.getString(R.string.appwidget_text)
            // 위젯에 위치하는 뷰는 RemoteViews 객체로 가져옴
            val views = RemoteViews(context.packageName, R.layout.torch_app_widget)
            // RemoteViews 객체용으로 준비된 텍스트값을 변경
            views.setTextViewText(R.id.appwidget_text, widgetText)

            // 실행할 Intent 작성
            val intent = Intent(context, TorchService::class.java)
            val pendingIntent = PendingIntent.getService(context, 0, intent, 0)

            // 위젯을 클릭하면 인텐트 실행
            views.setOnClickPendingIntent(R.id.appwidget_layout, pendingIntent)

            // appWidgetManager 를 사용해 위젯 업데이트
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

