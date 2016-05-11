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

package org.catrobat.catroid.content.actions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.scenes.scene2d.Action;

import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.AskQuestionBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.stage.StageActivity;

public class AskQuestionAction extends Action {

	private Formula question;
	private Sprite sprite;
	private UserVariable userVariable;




	@Override
	public boolean act(float delta) {
		Log.d("ASK_BRICK", "Ask brick action called...");
		if (userVariable == null) {
			return false;
		}

		Object value = question == null ? "TEST DEFAULT" : question.interpretObject(sprite);
		Log.d("ASK_BRICK", value.toString());

		StageActivity.stageListener.pause();

		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {
			public void run() {

				Toast.makeText(CatroidApplication.getAppContext(), "Your message to main thread", Toast.LENGTH_SHORT).show();

//				Dialog dialog = new AlertDialog.Builder(CatroidApplication.getAppContext())
//						.setTitle(R.string.formula_editor_new_string_name)
//						.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								dialog.cancel();
//							}
//						}).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//		//						handleOkButton();
//							}
//						}).create();
//				dialog.show();
			}
		});






//		boolean isFirstLevelStringTree = false;
//		if (question != null && question.getRoot().getElementType() == FormulaElement.ElementType.STRING) {
//			isFirstLevelStringTree = true;
//		}
//
//		try {
//			if (!isFirstLevelStringTree && value instanceof String) {
////				value = Double.valueOf((String) value);
//				value = String.valueOf(value);
//				Log.d("ASK_BRICK", value.toString());
//			}
//		} catch (NumberFormatException numberFormatException) {
//			Log.d(getClass().getSimpleName(), "Couldn't parse String", numberFormatException);
//		}

		userVariable.setValue(value.toString());
		return true;
	}

	public void setQuestion(Sprite sprite, Formula question) {
		this.sprite = sprite;
		this.question = question;
	}

	public void setVariable(UserVariable userVariable) {
		this.userVariable = userVariable;
	}
}
