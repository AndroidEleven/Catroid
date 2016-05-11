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

package org.catrobat.catroid.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.adapter.ScriptOverviewAdapter;
import org.catrobat.catroid.ui.dragndrop.DragAndDropListView;

/**
 * Created by Andreas on 11.05.2016.
 */
public class ScriptOverviewFragment extends Fragment {


    public static final String SCRIPT_OVERVIEW_FRAGMENT_TAG = "script_overview_fragment";
    private GridView gridView;
    private Sprite sprite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListeners();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_brickoverview, null);

        gridView = (GridView) rootView.findViewById(R.id.script_grid);

        return rootView;
    }

    private void initListeners(){

        sprite = ProjectManager.getInstance().getCurrentSprite();
        Log.d("ZOOM", "TEST");
        if (sprite == null) {
            Log.d("ZOOM", "TES2");
            return;
        }

        gridView.setAdapter(new ScriptOverviewAdapter(this, sprite));
    }
}
