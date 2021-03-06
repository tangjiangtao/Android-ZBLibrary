/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnFinishListener;
import zuo.biao.library.util.StringUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**通用编辑个人资料文本界面
 * @author Lemon
 * @use toActivity或startActivityForResult(EditTextInfoActivity.createIntent) > onActivityResult方法内data.getStringExtra(
 * SelectPictureActivity.RESULT_EDIT_TEXT_INFO)可得到输入框内容(String)
 */
public class EditTextInfoActivity extends BaseActivity implements OnClickListener, OnFinishListener {
	public static final String TAG = "EditTextInfoActivity";

	/**
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static Intent createIntent(Context context, String key, String value) {
		return createIntent(context, 0, key, value);
	}
	/**
	 * @param context
	 * @param type
	 * @param key
	 * @param value
	 * @return
	 */
	public static Intent createIntent(Context context, int type, String key, String value) {
		return new Intent(context, EditTextInfoActivity.class).
				putExtra(INTENT_TYPE, type).
				putExtra(INTENT_KEY, key).
				putExtra(INTENT_VALUE, value);
	}



	private int MaxLen = 30;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_text_info_activity, this);//传this是为了全局滑动返回
		//类相关初始化,建议使用<<<<<<<<<
		context = this;
		isAlive = true;
		//类相关初始化,建议使用>>>>>>>>>

		//必须调用<<<<<<<<<<<
		initView();
		initData();
		initListener();
		//必须调用>>>>>>>>>>

	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private TextView tvEditTextInfoTitle;
	private View tvEditTextInfoForward;

	private EditText etEditTextInfo;
	private View ivEditTextInfoClear;
	private TextView tvEditTextInfoRemind;
	private ListView lvEditTextInfo;
	//	private XListView lvEditTextInfo;
	@Override
	public void initView() {//必须调用

		tvEditTextInfoTitle = (TextView) findViewById(R.id.tvEditTextInfoTitle);
		tvEditTextInfoForward = findViewById(R.id.tvEditTextInfoForward);

		etEditTextInfo = (EditText) findViewById(R.id.etEditTextInfo);
		ivEditTextInfoClear = findViewById(R.id.ivEditTextInfoClear);
		tvEditTextInfoRemind = (TextView) findViewById(R.id.tvEditTextInfoRemind);

		lvEditTextInfo = (ListView) findViewById(R.id.lvEditTextInfo);
	}

	private ArrayAdapter<String> adapter;
	/**显示列表内容
	 * @author author
	 * @param list
	 */
	private void setList(List<String> list) {
		if (hasList == false || list == null || list.size() <= 0) {
			Log.i(TAG, "setList list == null || list.size() <= 0 >> adapter = null;lvEditTextInfo.setAdapter(null); return;");
			adapter = null;
			lvEditTextInfo.setAdapter(null);
			return;
		}

		adapter = new ArrayAdapter<String>(context, R.layout.list_item, R.id.tvListItem, list);
		lvEditTextInfo.setAdapter(adapter);
		//			if (hasUrl) {
		//			}
		lvEditTextInfo.smoothScrollBy(60, 200);
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final int TYPE_NICK = EditTextInfoWindow.TYPE_NICK;
	public static final int TYPE_NAME = EditTextInfoWindow.TYPE_NAME;

	public static final int TYPE_PHONE = EditTextInfoWindow.TYPE_PHONE;
	public static final int TYPE_WEBSITE = EditTextInfoWindow.TYPE_WEBSITE;
	public static final int TYPE_EMAIL = EditTextInfoWindow.TYPE_EMAIL;
	public static final int TYPE_FAX = EditTextInfoWindow.TYPE_FAX;

	public static final int TYPE_USUALADDRESS = EditTextInfoWindow.TYPE_USUALADDRESS;
	public static final int TYPE_MAILADDRESS = EditTextInfoWindow.TYPE_MAILADDRESS;
	public static final int TYPE_SCHOOL = EditTextInfoWindow.TYPE_SCHOOL;
	public static final int TYPE_COMPANY = EditTextInfoWindow.TYPE_COMPANY;

	public static final int TYPE_PROFESSION = EditTextInfoWindow.TYPE_PROFESSION;
	public static final int TYPE_NOTE = EditTextInfoWindow.TYPE_NOTE;
	//	public static final int TYPE_OTHER = EditTextInfoWindow.TYPE_OTHER;

	public static final String INTENT_TYPE = EditTextInfoWindow.INTENT_TYPE;
	public static final String INTENT_KEY = EditTextInfoWindow.INTENT_KEY;
	public static final String INTENT_VALUE = EditTextInfoWindow.INTENT_VALUE;

	private int intentType = 0;

	private boolean hasList = false;
	private boolean hasUrl = false;

	private ArrayList<String> list;
	@Override
	public void initData() {//必须调用

		intent = getIntent();
		intentType = intent.getIntExtra(INTENT_TYPE, 0);
		if (StringUtil.isNotEmpty(intent.getStringExtra(INTENT_KEY), true)) {
			tvEditTextInfoTitle.setText(StringUtil.getCurrentString());
		}

		if (intentType == TYPE_NICK) {
			tvEditTextInfoRemind.setText("限10个字（或20个字符）");
			MaxLen = 20;
			//			etEditTextInfo.setMaxEms(10);
			//etEditTextInfo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
		} else if (intentType == TYPE_PHONE) {
			tvEditTextInfoRemind.setText("只能填电话号码哦");
			MaxLen = 11;
			//etEditTextInfo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
		} else if (intentType == TYPE_PROFESSION) {
			hasList = true;
			tvEditTextInfoRemind.setText("所属行业");
			MaxLen = 30;
			//			etEditTextInfo.setFocusable(false);
			//			etEditTextInfo.setFocusableInTouchMode(false);
			//			etEditTextInfo.setEnabled(false);
		} else {
			tvEditTextInfoRemind.setText("限" + MaxLen/2 + "个字（或" + MaxLen + "个字符）");
		}
		etEditTextInfo.setMaxEms(MaxLen);

		getlist(intentType);

	}

	private int requestSize = 20;
	/**获取列表
	 * @author lemon
	 * @param listType
	 * @return
	 */
	protected void getlist(final int listType) {
		if (hasList == false) {
			return;
		}

		list = new ArrayList<String>();
		runThread(TAG + "getlist", new Runnable() {//baseRunnable已在baseFragment中新建
			@Override
			public void run() {
				Log.i(TAG, "getlist  listType = " + listType);
				if (listType == TYPE_PROFESSION) {
					list = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.profesions)));
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.i(TAG, "runOnUiThread run ");
						if (isAlive == true) {//isAlive已在baseFragment中新建
							Log.i(TAG, "isAlive == true");
							dismissProgressDialog();
							if (hasList) {
								setList(list);
							}
						}
					}
				});
			}
		});
	}

	private void saveAndExit() {
		String editedValue = StringUtil.getTrimedString(etEditTextInfo);
		if (editedValue.equals("" + getIntent().getStringExtra(INTENT_VALUE))) {
			showShortToast("内容没有改变哦~");
		} else {
			intent = new Intent();
			intent.putExtra(RESULT_TYPE, getIntent().getIntExtra(INTENT_TYPE, -1));
			//				intent.putExtra(RESULT_TYPE, StringUtil.getTrimedString(etEditTextInfo));
			//				intent.putExtra(RESULT_KEY, StringUtil.getTrimedString(etEditTextInfo));
			intent.putExtra(RESULT_VALUE, editedValue);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	//data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//listener事件监听区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private String inputedString;

	private static final long SEARCH_DELAY_TIME = 240;
	private Handler searchHandler;
	@Override
	public void initListener() {//必须调用

		findViewById(R.id.tvEditTextInfoReturn).setOnClickListener(this);
		tvEditTextInfoForward.setOnClickListener(this);

		searchHandler = new Handler(new Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (msg == null) {
					Log.e(TAG, "searchHandler  handleMessage  (msg == null >> return false;");
					return false;
				}

				Log.i(TAG, "inputedString = " + inputedString + "msg.obj = " + msg.obj);
				if(inputedString != null){
					if (inputedString.equals(msg.obj)) {
						getlist(intentType);
					}
				}
				return false;
			}
		});
		etEditTextInfo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				inputedString = StringUtil.getTrimedString(s);
				if (StringUtil.isNotEmpty(inputedString, true) == false) {
					ivEditTextInfoClear.setVisibility(View.GONE);
				} else {
					ivEditTextInfoClear.setVisibility(View.VISIBLE);
					if (hasUrl == true) {
						Message msg = new Message();
						msg.obj = inputedString;
						searchHandler.sendMessageDelayed(msg , SEARCH_DELAY_TIME);
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		ivEditTextInfoClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				etEditTextInfo.setText("");
			}
		});

		etEditTextInfo.setText(StringUtil.getTrimedString(getIntent().getStringExtra(INTENT_VALUE)));
		etEditTextInfo.setSelection(StringUtil.getLength(etEditTextInfo, true));

		if (hasList == true) {
			EditTextManager.hideKeyboard(context, etEditTextInfo);

			if (hasUrl) {
				lvEditTextInfo.setOnScrollListener(new OnScrollListener() {
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
					}
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						if (adapter != null && lvEditTextInfo.getLastVisiblePosition() >= adapter.getCount() - 1) {
							requestSize += 20;
							Log.i(TAG, "initListener  lvEditTextInfo.setOnScrollListener( >> onScroll getlist(intentType);requestSize = " + requestSize);
							getlist(intentType);
						}
					}
				});
				//				} else {
			}
			lvEditTextInfo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//					if (hasUrl) {
					//						position -= lvEditTextInfo.getHeaderViewsCount();
					//					}
					if (position < adapter.getCount()) {
						etEditTextInfo.setText("" + adapter.getItem(position));
						if (hasUrl) {
							intent = new Intent();
							intent.putExtra(RESULT_TYPE, getIntent().getIntExtra(INTENT_TYPE, -1));
							intent.putExtra(RESULT_KEY, getIntent().getStringExtra(INTENT_KEY));
							intent.putExtra(RESULT_VALUE, adapter.getItem(position));
							setResult(RESULT_OK, intent);
							finish();
							return;
						}
						saveAndExit();
					}
				}
			});
			lvEditTextInfo.setOnTouchListener(new View.OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					EditTextManager.hideKeyboard(context, etEditTextInfo);
					return false;
				}
			});
		}

	}


	//系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String RESULT_TYPE = "RESULT_TYPE";
	public static final String RESULT_KEY = "RESULT_KEY";
	public static final String RESULT_VALUE = "RESULT_VALUE";
	public static final String RESULT_URL = "RESULT_URL";
	public static final String RESULT_ID = "RESULT_ID";
	public static final String RESULT_IMAGE_URL = "RESULT_IMAGE_URL";
	//@Override
	//public void onClick(View v) {
	//	switch (v.getId()) {
	//		case R.id.ivEditTextInfoReturn:
	//			onPageReturn();
	//			break;
	//		case R.id.tvEditTextInfoForward:
	//			if (hasUrl == false) {
	//				saveAndExit();
	//			} else {
	//				Message msg = new Message();
	//				msg.obj = inputedString;
	//				searchHandler.sendMessage(msg);
	//			}
	//			break;
	//		default:
	//			break;
	//	}
	//}
	//Library内switch方法中case R.id.idx:报错
	@Override
	public void onClick(View v) {
		if (v.getId() ==  R.id.tvEditTextInfoReturn) {
			finish();
		} else if (v.getId() ==  R.id.tvEditTextInfoForward) {
			if (hasUrl == false) {
				saveAndExit();
			} else {
				Message msg = new Message();
				msg.obj = inputedString;
				searchHandler.sendMessage(msg);
			}
		}
	}




	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//listener事件监听区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}