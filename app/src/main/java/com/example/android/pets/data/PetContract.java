package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class PetContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";

    /* Inner class that defines the table contents of the pets table */
    public static final class PetEntry implements BaseColumns {
        //Table name
        public static final String TABLE_NAME = "pets";

        //column id
        public static final String _ID = BaseColumns._ID;

        //Name of the Pet
        public static final String COLUMN_PET_NAME = "name";
        //Breed of the pet
        public static final String COLUMN_PET_BREED = "breed";
        //Gender of the pet
        public static final String COLUMN_PET_GENDER = "gender";
        //Weight of the pet
        public static final String COLUMN_PET_WEIGHT = "weight";

        //Possible values for Gender attribute
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;


        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_FEMALE || gender == GENDER_MALE) {
                return true;
            }
            return false;
        }
    }
}

