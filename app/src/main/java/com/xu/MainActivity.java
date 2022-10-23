package com.xu;
 
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.media.AudioManager;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;


public class MainActivity extends Activity { 
    public AudioManager am;
	//需要点击次数满足才会退出
	private int num = 100;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //填充通知栏颜色
        Window window = this.getWindow();
        window.setStatusBarColor(0xFFcfde29);
        //去掉标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //防止重新加载
		if (!this.isTaskRoot()) {
			Intent mainIntent = getIntent();
			String action = mainIntent.getAction();
			if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
				finish();return;
				}}

		//获取音频服务
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//启动线程循环设置音量
		new Thread() {
			@TargetApi(Build.VERSION_CODES.P)
            public void run() {
				//这儿是耗时操作，完成之后更新UI；
				while(true){
                    final int m = am.getStreamMinVolume(AudioManager.STREAM_MUSIC);  //设置静音
					runOnUiThread(new Runnable(){
							@Override
							public void run() {
								//更新UI
								am.setStreamVolume(AudioManager.STREAM_MUSIC, m, AudioManager.FLAG_PLAY_SOUND);
							}
						});
					try {
						sleep(500);
					} catch (InterruptedException e) {}
				}
			}
		}.start();

		//按钮点击事件
		final Button bt = findViewById(R.id.activitymainButton);
        final ImageView kt = findViewById(R.id.kt);
		bt.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					if(num != 0){
						num--;
						bt.setText("对不起，我错了\n"+"再点"+num+"下就关闭程序");
					}else{
						finish();
					}
                    if(num % 2 == 0){
                        kt.setImageResource(R.drawable.xu1);
                    }else{
                        kt.setImageResource(R.drawable.xu2);
                    }
                }
			});
	}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
	protected void onRestart(){
		super.onRestart();
        Window window = this.getWindow();
        window.setStatusBarColor(0xFFcfde29);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK || keyCode== KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP){
			Toast toast = Toast.makeText(this,null,Toast.LENGTH_LONG);
			toast.setText("别试了，没用的！");
            toast.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
