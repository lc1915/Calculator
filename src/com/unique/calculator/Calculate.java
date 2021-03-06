package com.unique.calculator;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

import android.util.Log;

public class Calculate {

	private static final Map<String, Integer> OPERATORS = new HashMap<String, Integer>();

	static {
		OPERATORS.put("+", 0);
		OPERATORS.put("-", 0);
		OPERATORS.put("x", 1);
		OPERATORS.put("/", 1);
		OPERATORS.put("(", 2);
		OPERATORS.put(")", -1);
	}

	private static boolean isOperator(String str) {
		return OPERATORS.containsKey(str);// 如果存在指定键的映射关系则返回true，否则返回false
	}

	/*private static boolean is4Operator(String ch) {
		boolean bOperator = false;
		if (ch == "+" || ch == "-" || ch == "x" || ch == "/")
			bOperator = true;
		return bOperator;
	}

	// 比较运算符的优先级
	private static final int comparePrecedence(String op1, String op2) {
		if (!isOperator(op1) || !isOperator(op2)) {
			// 抛出非法数据异常
			throw new IllegalArgumentException("Invalid operators:" + op1 + " "
					+ op2);
		}
		return OPERATORS.get(op1) - OPERATORS.get(op2);
	}*/

	// Split the infix notation and store the operands and operators in the
	// returned array.
	// E.g., the input infixExp = "2.3+5.8"
	// Returns: [0] = "2.3"
	// [1] = "+"
	// [2] = "5.8"
	private static String[] splitInfixExp(String infixExp) {

		ArrayList<String> strArray = new ArrayList<String>();

		String lastOperand = "";

		for (char ch : infixExp.toCharArray()) { // toCharArray():将此字符串转换为一个新的字符数组
			String str = Character.toString(ch);// Character 类在对象中包装一个基本类型 char
												// 的值。Character 类型的对象包含类型为 char
												// 的单个字段。

			if (str.equals("+") || str.equals("-") || str.equals("x")
					|| str.equals("/")) {
				// Check that lastOperand must have some characters.
				// Note that we allow negative values, such as -4.5 + 2,
				// we need to take care of the leading negative sign.
				if (lastOperand == "") {
					if (str.equals("-")) {
						lastOperand += ch;
						continue;
					}
					// throw new
					// IllegalArgumentException("Invalid infix expression:" +
					// infixExp);
					return null;
				}

				strArray.add(lastOperand);
				strArray.add(str);

				lastOperand = "";
			} else if (str.equals("(")) {
				strArray.add(str);
				lastOperand = "";
			} else if (str.equals(")")) {
				if (lastOperand == "")
					return null;
				strArray.add(lastOperand);
				strArray.add(str);
				lastOperand = "";
			} else {
				lastOperand += ch;
			}
		}

		// Add the last operand
		if (lastOperand != "")
			strArray.add(lastOperand);

		String[] outputs = new String[strArray.size()];
		return strArray.toArray(outputs);
	} // splitInfixExp

	// 中缀表达式转化为后缀表达式
	// Note that the operands and operators in the RPN will be separated by
	// spaces.
	private static String infix2RPN(String infixExp) {
		String rpnExp = "";

		String[] inputs = splitInfixExp(infixExp);

		if (inputs == null)
			return null;

		Stack<String> stack = new Stack<String>();

		for (String input : inputs) {
			String temp;
			Log.w("DisplayActivity", "it is " + rpnExp.toString());

			if(input.equals("(")) stack.push(input);
			else if(input.equals("+")||input.equals("-")){
				while(stack.size()!=0){
					 temp = stack.pop();  
	                    if (temp.equals("(")) {  
	                        stack.push("(");  
	                        break;  
	                    }  
	                    rpnExp += " " + temp;
				}
				stack.push(input);  
				rpnExp += " ";  
			}
			else if(input.equals("x")||input.equals("/")){
				while (stack.size() != 0) {  
                    temp = stack.pop();  
                    if (temp.equals("(")|| temp.equals("+") || temp.equals("-")) {  
                        stack.push(temp);  
                        break;  
                    } else {  
                    	rpnExp += " " + temp;  
                    }  
                }  
                stack.push(input);  
                rpnExp += " ";  
			}
			else if(input.equals(")")){
				while (stack.size() != 0) {  
                    temp = stack.pop();  
                    if (temp.equals("("))  
                        break;  
                    else  
                    	rpnExp += " " + temp;  
                }  
                //rpnExp += " ";  
			}
			else {
				rpnExp += " ";
				rpnExp += input;
			}
		}

		while (!stack.empty()) {
			rpnExp += " ";
			rpnExp += stack.pop();
		}
		Log.w("DisplayActivity", "it is " + rpnExp.toString());
		return rpnExp.trim();
	}

	// 计算后缀表达式
	private static double evalRPN(String rpnExp) {
		double result = 0.0;

		String[] inputs = rpnExp.split(" ");// split():根据给定正则表达式的匹配或字符来拆分此字符串

		Stack<Double> stack = new Stack<Double>();
		for (String op : inputs) {
			if (isOperator(op)) {
				if (stack.size() < 2)
					// throw new
					// IllegalArgumentException("Invalid RPN expression:" +
					// rpnExp);
					return -1.0;

				Double val2 = stack.pop();
				Double val1 = stack.pop();
				Double valLoc = 0.0;

				if (op.equals("+"))
					valLoc = val1 + val2;
				else if (op.equals("-"))
					valLoc = val1 - val2;
				else if (op.equals("x"))
					valLoc = val1 * val2;
				else if (op.equals("/"))
					valLoc = val1 / val2;
				else
					// throw new
					// IllegalArgumentException("Invalid RPN expression:" +
					// rpnExp);
					System.out.println("Invalid RPN expression:" + rpnExp);

				stack.push(valLoc);
			} else {
				try {
					Double val = Double.parseDouble(op);
					stack.push(val);
				} catch (NumberFormatException ex) {
					System.out.println("NumberFormatException: "
							+ ex.getMessage());
				}
			}
		}

		result = stack.pop();
		if (!stack.empty())
			throw new IllegalArgumentException("Invalid RPN expression:"
					+ rpnExp);
		return result;
	}

	// 计算中缀表达式
	public static double evalExp(String exp) {
		String rpnExp = infix2RPN(exp);
		if (rpnExp == null)
			return 0.0;
		return evalRPN(rpnExp);
	}

}
