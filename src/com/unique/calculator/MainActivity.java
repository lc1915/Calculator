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
		if (ch == '+' || ch == '-' || ch == 'x' || ch == '/' || ch == '('
				|| ch == ')')
			bOperator = true;

		return bOperator;
	}

	private boolean appendDotValid(String exp) {
		if (exp.equals(""))// ���������пհ�
			return true;

		int expLen = exp.length();
		for (int i = expLen - 1; i >= 0; --i) {
			char ch = exp.charAt(i);
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

		// ��ȡ��Ļ�ߴ粢Ϊ�����ؼ�����ߴ磨����ֵ��
		Point size = new Point(); // point��Ķ�������x,y��������ֵ
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

					TextView showTextView = (TextView) findViewById(R.id.showTextView);
					String oldExpression = showTextView.getText().toString();

					char inputCh = bnText.charAt(bnText.length() - 1);
					if (isOperator(inputCh)) {
						if (oldExpression.equals(""))// ���������пհ�
							return;

						//char lastCh = oldExpression.charAt(oldExpression
								//.length() - 1);
						//if (isOperator(lastCh))// �������������һ���ַ��Ƿ���
							//return;
					}

					// ����������"."����������д����һ���ַ���ʼ��ǰ������û���������ŵ�ʱ��ͳ�����"."
					if (inputCh == '.' && !appendDotValid(oldExpression)) {
						return;
					}

					String newExpression = null;
					if (bnText.equals("=")) {
						double result = Calculate.evalExp(oldExpression);
						newExpression = Double.toString(result);
					} else {
						newExpression = oldExpression.concat(bnText);// ������oldExpression�ַ����ĺ���
					}

					showTextView.setText(newExpression);
				}
			});

			GridLayout.Spec rowSpec = GridLayout.spec(i / 5 + 2);// ָ����������ڵ�����
			GridLayout.Spec columnSpec = GridLayout.spec(i % 5);// ָ����������ڵ�����
			// LayoutParams���������ṩ�����Լ������Ϸ���һ���µĲ��ֲ������á�
			GridLayout.LayoutParams params = new GridLayout.LayoutParams(
					rowSpec, columnSpec);
			params.width = oneQuarterWidth;
			params.height = oneSeventhHeight;
			gridLayout.addView(btn, params);
		}

		// �ı�clear��delete����button�����ֵĴ�С
		clearButton = (Button) findViewById(R.id.clearButton);
		clearButton.setTextSize(oneSeventhHeight / 7);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		deleteButton.setTextSize(oneSeventhHeight / 7);
		showTextView = (TextView) findViewById(R.id.showTextView);
		showTextView.setTextSize(oneSeventhHeight / 7);

	}

	public void onClearText(View v) {
		TextView showTextView = (TextView) findViewById(R.id.showTextView);
		showTextView.setText("");
	}

	public void onDeleteText(View v) {
		TextView showTextView = (TextView) findViewById(R.id.showTextView);
		String oldExp = showTextView.getText().toString().trim();// String.trim()�����ַ����ĸ���������ǰ���հ׺�β���հ�(ȥ�����˵Ŀհ��ַ�
																	// �س������Ʊ���������)
		if (oldExp.equals(""))
			return;

		// ɾ�����һ���ַ�
		oldExp = oldExp.substring(0, oldExp.length() - 1);// substring(begin,end)��Begin��end֮��,������end
		showTextView.setText(oldExp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}