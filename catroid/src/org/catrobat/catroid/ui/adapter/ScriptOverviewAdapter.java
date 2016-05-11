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

package org.catrobat.catroid.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.ui.fragment.ScriptOverviewFragment;

import java.util.List;

/**
 * Created by Andreas on 11.05.2016.
 */
public class ScriptOverviewAdapter extends BaseAdapter implements View.OnClickListener {

    private Sprite sprite;
    private List<Script> scriptList;
    private Context context;

    public ScriptOverviewAdapter(ScriptOverviewFragment fragment, Sprite sprite){
        this.sprite = sprite;
        scriptList = this.sprite.getScriptList();
        context = fragment.getActivity();

    }

    @Override
    public int getCount() {
        return scriptList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0 || position >= scriptList.size()) {
            return null;
        }
        return scriptList.get(position);
    }

    @Override
    public long getItemId(int position) {
            return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Script item = (Script)getItem(position);


      //  if(convertView == null) {
            ViewGroup wrapper = (ViewGroup) View.inflate(context, R.layout.brick_wrapper, null);

            wrapper.addView(item.getScriptBrick().getNoPuzzleView(context,0,this));
            List<Brick> brickList = item.getBrickList();
            for (Brick brick: brickList) {
                //do something
                View view = brick.getView(context, position, this);

                wrapper.addView(view);

            }

            convertView = wrapper;
      //  } else {

    //    }



        return convertView;
    }

    @Override
    public void onClick(View v) {

    }
}
