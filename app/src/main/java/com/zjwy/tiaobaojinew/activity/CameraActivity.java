package com.zjwy.tiaobaojinew.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zjwy.tiaobaojinew.R;

/**
 * Android手指拍照
 * 
 * @author wwj
 * @date 2013/4/29
 */
public class CameraActivity extends Activity {
	private View layout;
	private Camera camera;
	private Camera.Parameters parameters = null;

	Bundle bundle = null; // 声明一个Bundle对象，用来存储数据

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 显示界面
		setContentView(R.layout.activity_camera);

		layout = this.findViewById(R.id.buttonLayout);
		Button takepicture = (Button) this.findViewById(R.id.takepicture);
		takepicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 拍照
				camera.takePicture(null, null, new MyPictureCallback());
			}
		});
		scalePic = (ImageView) this.findViewById(R.id.scalePic);
		layerClothesssss = (LinearLayout) this
				.findViewById(R.id.layerClothesssss);

		SurfaceView surfaceView = (SurfaceView) this
				.findViewById(R.id.surfaceView);
		surfaceView.getHolder()
				.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setFixedSize(320, 480); // 设置Surface分辨率
		surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
		surfaceView.getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数
	}

	/**
	 * 返回
	 */
	public void btnOnclick11(View v) {
		if (bundle != null && intent != null) {
			setResult(22, intent);
			finish();
		}
	}

	boolean isFrist01 = true;
	boolean isFrist00 = true;

	/**
	 * 选择模型
	 */
	public void btnOnclick22(View v) {
		if (isFrist01) {
			layerClothesssss.setVisibility(View.VISIBLE);
			if (isFrist00) {
				showClothesList();
				isFrist00 = false;
			}
			isFrist01 = false;
		} else {
			layerClothesssss.setVisibility(View.GONE);
			isFrist01 = true;
		}
	}

	private void showClothesList() {
		int[] pagIds = { R.drawable.a01, R.drawable.a02, R.drawable.a03,
				R.drawable.a04, R.drawable.a05, R.drawable.a03, R.drawable.a04 };
		for (int i = 0; i < pagIds.length; i++) {
			final int j = pagIds[i];
			ImageView imageView = new ImageView(CameraActivity.this);
			LayoutParams layoutParams = new LayoutParams(150, 200);
			imageView.setLayoutParams(layoutParams);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageResource(j);
			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					scalePic.setImageResource(j);
				}
			});
			layerClothesssss.addView(imageView);
		}
	}

	/**
	 * 图片被点击触发的时间
	 * 
	 * @param v
	 */
	public void imageClick(View v) {
		if (v.getId() == R.id.scalePic) {
			if (bundle == null) {
				Toast.makeText(getApplicationContext(), "照相null",
						Toast.LENGTH_SHORT).show();
			} else {
				System.out.println("-----19000--");
				// Intent intent = new Intent();
				// intent.putExtras(bundle);
				// // startActivityForResult(intent, 2);
				// setResult(2, intent);
				// finish();
			}
		}
	}

	private Intent intent;
	private LinearLayout layerClothesssss;
	private ImageView scalePic;

	public final class MyPictureCallback implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				intent = new Intent();
				bundle = new Bundle();
				bundle.putByteArray("bytes", data); // 将图片字节数据保存在bundle当中，实现数据交换
				intent.putExtras(bundle);
				saveToAblum(data);
				// saveToSDCard(data); // 保存图片到sd卡中
				Toast.makeText(getApplicationContext(), "成功",
						Toast.LENGTH_SHORT).show();
				camera.startPreview(); // 拍完照后，重新开始预览

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void saveToAblum(byte[] data) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			ContentResolver cr = getContentResolver();
			MediaStore.Images.Media.insertImage(cr, bitmap, "myPhoto",
					"this is a Photo");
		}

	}

	/**
	 * 将拍下来的照片存放在SD卡中
	 * 
	 * @param data
	 * @throws IOException
	 */
	public static void saveToSDCard(byte[] data) throws IOException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ "");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
			fileFolder.mkdir();
		}
		File jpgFile = new File(fileFolder, filename);
		FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
		outputStream.write(data); // 写入sd卡中
		outputStream.close(); // 关闭输出流
	}

	private final class SurfaceCallback implements Callback {

		// 拍照状态变化时调用该方法
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			if (camera != null) {
				parameters = camera.getParameters(); // 获取各项参数
				parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
				parameters.setPreviewSize(width, height); // 设置预览大小
				parameters.setPreviewFrameRate(5); // 设置每秒显示4帧
				parameters.setPictureSize(width, height); // 设置保存的图片尺寸
				parameters.setJpegQuality(80); // 设置照片质量
			}
		}

		// 开始拍照时调用该方法
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open(); // 打开摄像头
				camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
				camera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));
				camera.startPreview(); // 开始预览
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// 停止拍照时调用该方法
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera != null) {
				camera.release(); // 释放照相机
				camera = null;
			}
		}
	}

	/**
	 * 点击手机屏幕是，显示两个按钮
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			layout.setVisibility(ViewGroup.VISIBLE); // 设置视图可见
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA: // 按下拍照按钮
			if (camera != null && event.getRepeatCount() == 0) {
				// 拍照
				// 注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
				// ，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
				camera.takePicture(null, null, new MyPictureCallback());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
	public static int getPreviewDegree(Activity activity) {
		// 获得手机的方向
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degree = 0;
		// 根据手机的方向计算相机预览画面应该选择的角度
		switch (rotation) {
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 0;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;
		}
		return degree;
	}
}
