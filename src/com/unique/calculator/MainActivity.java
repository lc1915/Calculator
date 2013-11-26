package com.unique.calculator;

import android.R.color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

import android.graphics.Point;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	GridLayout gridLayout;
	Button clearButton;
	Button deleteButton;
	TextView showTextView;

	String[] buttonTexts = new String[] { "7", "8", "9", "(", ")", "4", "5",
			"6", "+", "-", "1", "2", "3", "x", "/", "00", "0", ".", "=" };

	private boolean isOperator(char ch) {
		boolean bOperator = false;
		if (ch == '+' || ch == '-' || ch == 'x' || ch == 'X' || ch == '/')
			bOperator = true;

		return bOperator;
	}

	private boolean appendDotValid(String exp) {
		if (exp.equals(""))
			return true;

		int expLen = exp.length();
		for (int ii = expLen - 1; ii >= 0; --ii) {
			char ch = exp.charAt(ii);
			if (isOperator(ch))
				return true;
			else if (ch == '.')
				return false;
		}

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 获取屏幕尺寸并为各个控件分配尺寸（像素值）
		Point size = new Point(); // point类的对象里存放x,y两个坐标值
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;
		int oneQuarterWidth = (int) (screenWidth * 0.2);
		int oneSeventhHeight = (int) (screenHeight * 0.15);

		gridLayout = (GridLayout) findViewById(R.id.calculatorGridLayout);

		for (int i = 0; i < buttonTexts.length; i++) {
			Button btn = new Button(this);
			btn.setText(buttonTexts[i]);
			btn.setTextSize(oneSeventhHeight / 6);
			btn.setBackgroundColor(color.white);

			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Button bn = (Button) arg0;
					String bnText = bn.getText().toString();

					TextView expressionTextView = (TextView) findViewById(R.id.showTextView);
					String oldExpression = expressionTextView.getText()
							.toString();

					char inputCh = bnText.charAt(bnText.length() - 1);
					if (isOperator(inputCh)) {
						if (oldExpression.equals(""))
							return;

						char lastCh = oldExpression.charAt(oldExpression
								.length() - 1);
						if (isOperator(lastCh))
							return;
					}

					if (inputCh == '.' && !appendDotValid(oldExpression)) {
						return;
					}

					String newExpression = null;
					if (bnText.equals("=")) {
						double result = Calculate.evalExp(oldExpression);
						newExpression = Double.toString(result);
					} else {
						newExpression = oldExpression.concat(bnText);
					}

					expressionTextView.setText(newExpression);
				}
			});

			GridLayout.Spec rowSpec = GridLayout.spec(i / 5 + 2);// 指定该组件所在的行数
			GridLayout.Spec columnSpec = GridLayout.spec(i % 5);// 指定该组件所在的列数
			// LayoutParams方法：在提供的属性集基础上返回一个新的布局参数设置。
			GridLayout.LayoutParams params = new GridLayout.LayoutParams(
					rowSpec, columnSpec);
			params.width = oneQuarterWidth;
			params.height = oneSeventhHeight;
			gridLayout.addView(btn, params);
		}

		// 改变clear和delete两个button中文字的大小
		clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setTextSize(oneSeventhHeight / 7);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		deleteButton.setTextSize(oneSeventhHeight / 7);
		showTextView = (TextView) findViewById(R.id.showTextView);
		showTextView.setTextSize(oneSeventhHeight / 7);
		
	}

	public void onClearText(View v) {
		TextView expressionTextView = (TextView) findViewById(R.id.showTextView);
		expressionTextView.setText("");
	}

	public void onDeleteText(View v) {
		TextView expressionTextView = (TextView) findViewById(R.id.showTextView);
		String oldExp = expressionTextView.getText().toString().trim();
		if (oldExp.equals(""))
			return;

		// Remove the last character.
		oldExp = oldExp.substring(0, oldExp.length() - 1);
		expressionTextView.setText(oldExp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}