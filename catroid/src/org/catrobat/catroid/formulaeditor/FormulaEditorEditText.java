/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.formulaeditor;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import org.catrobat.catroid.formulaeditor.InternFormula.TokenSelectionType;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

public class FormulaEditorEditText extends EditText implements OnTouchListener{

	private static final BackgroundColorSpan COLOR_ERROR = new BackgroundColorSpan(0xFFF00000);
	private static final BackgroundColorSpan COLOR_HIGHLIGHT = new BackgroundColorSpan(0xFF33B5E5);
	private static FormulaEditorHistory history = null;
	FormulaEditorFragment formulaEditorFragment = null;
	private int absoluteCursorPosition = 0;
	private InternFormula internFormula;
	private Context context;

	final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
		@Override
		public boolean onDoubleTap(MotionEvent event) {
			internFormula.setCursorAndSelection(absoluteCursorPosition, true);
			history.updateCurrentSelection(internFormula.getSelection());
			highlightSelection();
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent motion) {
			Layout layout = getLayout();
			if (layout != null) {

				/*float lineHeight = getLineHeight();
				int yCoordinate = (int) motion.getY();
				int cursorY = 0;

				int paddingLeft = getPaddingLeft();

				int cursorXOffset = (int) motion.getX() - paddingLeft;

				int initialScrollY = getScrollY();
				int firstLineSize = (int) (initialScrollY % lineHeight);
				int numberOfVisibleLines = (int) (getHeight() / lineHeight);

				if (yCoordinate <= lineHeight - firstLineSize) {

					scrollBy(0, (int) (initialScrollY > lineHeight ? -1 * (firstLineSize + lineHeight / 2) : -1
							* firstLineSize));
					cursorY = 0;
				} else if (yCoordinate >= numberOfVisibleLines * lineHeight - lineHeight / 2) {
					if (!(yCoordinate > layout.getLineCount() * lineHeight - getScrollY() - getPaddingTop())) {
						scrollBy(0, (int) (lineHeight - firstLineSize + lineHeight / 2));
					}
					cursorY = numberOfVisibleLines;
				} else {
					for (int i = 1; i <= numberOfVisibleLines; i++) {
						if (yCoordinate <= ((lineHeight - firstLineSize) + getPaddingTop() + i * lineHeight)) {
							cursorY = i;
							break;
						}
					}
				}

				int linesDown = (int) (initialScrollY / lineHeight);

				while (cursorY + linesDown >= layout.getLineCount()) {
					linesDown--;
				}

				int tempCursorPosition = layout.getOffsetForHorizontal(cursorY + linesDown, cursorXOffset);

				if (tempCursorPosition > length()) {
					tempCursorPosition = length();
				}

				if (!isDoNotMoveCursorOnTab()) {
					absoluteCursorPosition = tempCursorPosition;
				}
				absoluteCursorPosition = absoluteCursorPosition > length() ? length() : absoluteCursorPosition;
				*/
			/*	setSelection(absoluteCursorPosition);
				postInvalidate();

				internFormula.setCursorAndSelection(absoluteCursorPosition, false);

				highlightSelection();
				history.updateCurrentSelection(internFormula.getSelection());
				history.updateCurrentCursor(absoluteCursorPosition);

				formulaEditorFragment.refreshFormulaPreviewString();
				formulaEditorFragment.updateButtonsOnKeyboardAndInvalidateOptionsMenu();*/
				return false;

			}
			return false;
		}
	});
	private boolean doNotMoveCursorOnTab = false;

	public FormulaEditorEditText(Context context) {
		super(context);
		this.context = context;
	}

	public FormulaEditorEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void init(FormulaEditorFragment formulaEditorFragment) {
		this.formulaEditorFragment = formulaEditorFragment;
		this.setOnTouchListener(this);
		this.setLongClickable(false);
		this.setSelectAllOnFocus(false);
		this.setCursorVisible(true);

	}

	public void enterNewFormula(InternFormulaState internFormulaState) {
		internFormula = internFormulaState.createInternFormulaFromState();
		internFormula.generateExternFormulaStringAndInternExternMapping(context);

		updateTextAndCursorFromInternFormula();

		internFormula.selectWholeFormula();
		highlightSelection();

		if (history == null) {
			history = new FormulaEditorHistory(internFormula.getInternFormulaState());
		} else {
			history.init(internFormula.getInternFormulaState());
		}
	}

	public void overwriteCurrentFormula(InternFormulaState internFormulaState) {
		internFormula = internFormulaState.createInternFormulaFromState();
		internFormula.generateExternFormulaStringAndInternExternMapping(context);

		updateTextAndCursorFromInternFormula();

		internFormula.selectWholeFormula();
		//setSelection(absoluteCursorPosition);
		//highlightSelection();

		history.push(internFormula.getInternFormulaState());
		String resultingText = updateTextAndCursorFromInternFormula();

		formulaEditorFragment.refreshFormulaPreviewString(resultingText);
	}


	public void highlightSelection() {

		Spannable highlightSpan = this.getText();
		highlightSpan.removeSpan(COLOR_HIGHLIGHT);
		highlightSpan.removeSpan(COLOR_ERROR);

		int selectionStartIndex = internFormula.getExternSelectionStartIndex();
		int selectionEndIndex = internFormula.getExternSelectionEndIndex();
		TokenSelectionType selectionType = internFormula.getExternSelectionType();

		if (selectionStartIndex == -1 || selectionEndIndex == -1 || selectionEndIndex == selectionStartIndex
				|| selectionEndIndex > highlightSpan.length()) {
			return;
		}

		if (selectionType == TokenSelectionType.USER_SELECTION) {
			highlightSpan.setSpan(COLOR_HIGHLIGHT, selectionStartIndex, selectionEndIndex,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			highlightSpan.setSpan(COLOR_ERROR, selectionStartIndex, selectionEndIndex,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		//setSelection(selectionStartIndex, selectionEndIndex);
	}

	public void setParseErrorCursorAndSelection() {
		internFormula.selectParseErrorTokenAndSetCursor();
		//highlightSelection();
		setSelection(absoluteCursorPosition);
	}

	public void handleKeyEvent(int resource, String name) {
		internFormula.handleKeyInput(resource, context, name);
		history.push(internFormula.getInternFormulaState());
		String resultingText = updateTextAndCursorFromInternFormula();
		setSelection(absoluteCursorPosition);
		formulaEditorFragment.refreshFormulaPreviewString(resultingText);

	}

	public String getStringFromInternFormula() {
		return internFormula.getExternFormulaString();
	}

	public boolean hasChanges() {
		return history != null && history.hasUnsavedChanges();
	}

	public void formulaSaved() {
		history.changesSaved();
	}

	public void endEdit() {
		history.clear();
	}

	public void quickSelect() {
		internFormula.selectWholeFormula();
		highlightSelection();
	}

	public boolean undo() {
		if (!history.undoIsPossible()) {
			return false;
		}
		InternFormulaState lastStep = history.backward();
		if (lastStep != null) {

			internFormula = lastStep.createInternFormulaFromState();
			internFormula.generateExternFormulaStringAndInternExternMapping(context);
			internFormula.updateInternCursorPosition();
			updateTextAndCursorFromInternFormula();
		}

		formulaEditorFragment.refreshFormulaPreviewString();
		return true;
	}

	public boolean redo() {
		if (!history.redoIsPossible()) {
			return false;
		}
		InternFormulaState nextStep = history.forward();
		if (nextStep != null) {

			internFormula = nextStep.createInternFormulaFromState();
			internFormula.generateExternFormulaStringAndInternExternMapping(context);
			internFormula.updateInternCursorPosition();
			updateTextAndCursorFromInternFormula();
		}
		formulaEditorFragment.refreshFormulaPreviewString();
		return true;
	}

	private String updateTextAndCursorFromInternFormula() {
		String newExternFormulaString = internFormula.getExternFormulaString();
		absoluteCursorPosition = internFormula.getExternCursorPosition();

		setTextKeepState(newExternFormulaString);

		if (absoluteCursorPosition > length()) {
			absoluteCursorPosition = length();
		}

		setSelection(absoluteCursorPosition);

		highlightSelection();

		return newExternFormulaString;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motion) {
		return gestureDetector.onTouchEvent(motion);
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {


		if(selStart == selEnd)
		{
			Log.d("CURSER", "Position: " + selStart);
		}
		else{
			Log.d("CURSER", "Positionstart: " + selStart + " posend: " +selEnd);
		}

		Layout layout = getLayout();
		if (layout != null) {
			absoluteCursorPosition = selStart;


			internFormula.setCursorAndSelection(absoluteCursorPosition, false);

			highlightSelection();
			history.updateCurrentSelection(internFormula.getSelection());
			history.updateCurrentCursor(absoluteCursorPosition);

			formulaEditorFragment.refreshFormulaPreviewString();
			formulaEditorFragment.updateButtonsOnKeyboardAndInvalidateOptionsMenu();
		}

	//	absoluteCursorPosition = selStart;
	//	setSelection(absoluteCursorPosition);

	//	internFormula.setCursorAndSelection(absoluteCursorPosition, false);

//	highlightSelection();
	//	history.updateCurrentSelection(internFormula.getSelection());
	//	history.updateCurrentCursor(absoluteCursorPosition);

	//	formulaEditorFragment.refreshFormulaPreviewString();
		//formulaEditorFragment.updateButtonsOnKeyboardAndInvalidateOptionsMenu();
	}

	/*@Override
	public void onSelectionChanged(int start, int end) {


		int selectionStartIndex = internFormula.getExternSelectionStartIndex();
		int selectionEndIndex = internFormula.getExternSelectionEndIndex();
		setSelection(selectionStartIndex, selectionEndIndex);

	//	CharSequence text = getText();
	//	if (text != null) {
	//		if (start != text.length() || end != text.length()) {

	//			setSelection(text.length(), text.length());
	//			return;
	//		}
	//	}

	//	super.onSelectionChanged(start, end);
	}*/

	/*@Override
	public boolean onCheckIsTextEditor() {
		return false;
	}*/

	public InternFormulaParser getFormulaParser() {
		return internFormula.getInternFormulaParser();
	}

	public boolean isDoNotMoveCursorOnTab() {
		return doNotMoveCursorOnTab;
	}

	public void setDoNotMoveCursorOnTab(boolean doNotMoveCursorOnTab) {
		this.doNotMoveCursorOnTab = doNotMoveCursorOnTab;
	}

	public FormulaEditorHistory getHistory() {
		return history;
	}

	public boolean isThereSomethingToDelete() {
		return internFormula.isThereSomethingToDelete();
	}
}
