/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor> {

    //Column to be fetched
    String[] projection = {
            PetEntry._ID,
            PetEntry.COLUMN_PET_NAME,
            PetEntry.COLUMN_PET_BREED,
            PetEntry.COLUMN_PET_GENDER,
            PetEntry.COLUMN_PET_WEIGHT
    };
    //Current Pet URI
    Uri currentPetUri;
    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;
    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;
    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;
    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;
    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Examine the intent that was used to launch this activity,
        //In order to figure out if we're creating a new pet orediting an existing one.
        Intent intent = getIntent();
        //Getting the Uri passed along as data
        currentPetUri = intent.getData();
        //Checking whether the currentPetUri is null
        if (currentPetUri != null) {
            setTitle(R.string.editor_activity_title_edit_pet);
        } else {
            setTitle(R.string.editor_activity_title_new_pet);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        setupSpinner();

        //Starting the Loader if currentPetUri is not null
        if (currentPetUri != null) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN;
                ; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    private void savePet() {
        Uri newUri;
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, mNameEditText.getText().toString().trim());
        values.put(PetEntry.COLUMN_PET_BREED, mBreedEditText.getText().toString().trim());
        values.put(PetEntry.COLUMN_PET_GENDER, mGender);
        values.put(PetEntry.COLUMN_PET_WEIGHT, Integer.parseInt(mWeightEditText.getText().toString()));

        //Check whether this is an Insert operation or Edit operation
        if (currentPetUri == null) {
            // Insert a new row for Toto into the provider using the ContentResolver.
            // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
            // into the pets database table.
            // Receive the new content URI that will allow us to access Toto's data in the future.
            newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, R.string.editor_insert_pet_failed, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, R.string.editor_insert_pet_successful, Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsUpdated = getContentResolver().update(currentPetUri, values, null, null);
            // Show a toast message depending on whether or not the insertion was successful
            if (rowsUpdated == 0) {
                // If the row updated is 0, then there was an error with insertion.
                Toast.makeText(this, R.string.editor_insert_pet_failed, Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, R.string.editor_insert_pet_successful, Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                savePet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new android.content.CursorLoader(EditorActivity.this, currentPetUri, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        //Setting Values from the cursor on to the Fields
        mNameEditText.setText
                (cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_NAME)));
        mBreedEditText.setText
                (cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_BREED)));
        mWeightEditText.setText
                (cursor.getString(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_WEIGHT)));

        switch (cursor.getInt(cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_GENDER))) {
            case PetEntry.GENDER_FEMALE:
                mGenderSpinner.setSelection(PetEntry.GENDER_FEMALE);
                break;

            case PetEntry.GENDER_MALE:
                mGenderSpinner.setSelection(PetEntry.GENDER_MALE);
                break;

            case PetEntry.GENDER_UNKNOWN:
                mGenderSpinner.setSelection(PetEntry.GENDER_UNKNOWN);
                break;
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        //Resetting the Textfields for other uses.
        mNameEditText.setText(null);
        mBreedEditText.setText(null);
        mWeightEditText.setText(null);
        mGenderSpinner.setSelection(PetEntry.GENDER_UNKNOWN);

    }
}