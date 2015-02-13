package com.mozzandroidutils.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mozzandroidutils.file.MozzConfig;
import com.mozzandroidutils.http.HttpDownloadListener;
import com.mozzandroidutils.http.UpgradeListener;
import com.mozzandroidutils.http.Upgrader;
import com.mozzandroidutils.sqlite.ColumnType;
import com.mozzandroidutils.sqlite.Eloquent;
import com.mozzandroidutils.sqlite.Model;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MozzConfig.makeAppDirs(this);
		testUpgrader();

	}

	@Override
	public void onClick(View arg0) {
		Intent i = new Intent();
		i.setClass(this, TestActivity.class);
	}

	public void onDestory() {

	}

	private void testUpgrader() {
		final Upgrader upgrader = new Upgrader(
				"http://182.92.150.3/upgrade.json", this);
		upgrader.setOnUpgradeListener(new UpgradeListener() {

			@Override
			public void onNewVersion(int serverVersionCode,
					String serverVersion, String serverVersionDescription) {

				try {
					upgrader.download(null,
							MozzConfig.getAppAbsoluteDir(MainActivity.this),
							"newVersion.apk");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNoNewVersion() {
				Log.d("Upgrader", "onNoNewVersion");
			}

			@Override
			public void onCheckFailed() {
				Log.d("Upgrader", "onCheckFailed:");
			}
		});

		upgrader.checkNewVersion();

		try {
			upgrader.download(new HttpDownloadListener() {

				@Override
				public void onDownloading(int downloadSize) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDownloadSuccess() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDownloadStart(int fileSize) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onDownloadFailed() {
					// TODO Auto-generated method stub

				}
			}, "/AppDir", "newVersion.apk");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	private void testDB() {
		Eloquent.create("test", new String[] { "name" },
				new ColumnType[] { ColumnType.TYPE_TEXT }, this);

		final TestEloquent testTable = new TestEloquent(this);

		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					TestModel newModel = new TestModel();
					newModel.setName(System.currentTimeMillis() + "_" + 1);
					testTable.save(newModel);
				}
			};
		}.start();

		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					TestModel newModel = new TestModel();
					newModel.setName(System.currentTimeMillis() + "_" + 2);
					testTable.save(newModel);
				}
			};
		}.start();

		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					TestModel newModel = new TestModel();
					newModel.setName(System.currentTimeMillis() + "_" + 3);
					testTable.save(newModel);
				}
			};
		}.start();

		new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					TestModel newModel = new TestModel();
					newModel.setName(System.currentTimeMillis() + "_" + 4);
					testTable.save(newModel);
				}

			};
		}.start();

		testTable.close();
	}

}

class TestModel extends Model {
	private String name;

	public void setName(String nm) {
		this.name = nm;
	}
}

class TestEloquent extends Eloquent<TestModel> {

	public TestEloquent(Context context) {
		super(context);
	}

}
