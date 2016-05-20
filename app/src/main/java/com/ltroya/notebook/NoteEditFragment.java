package com.ltroya.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton noteCatButton;
    private EditText title, message;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject, confirmDialogObject;

    private static final String MODIFIED_CATEGORY = "Modified Category";

    private boolean newNote = false;
    private long noteId = 0;

    public NoteEditFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Grab the bundle that sends along whether or note our noteEditFragment is creating a new
        // note
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            newNote = bundle.getBoolean(NoteDetailActivity.EXTRA_NEW_NOTE, false);
        }
        // inflate out fragment edit layout
        View fragmentLayout = inflater.inflate(R.layout.fragment_note_edit, container, false);

        // Grab widget references from layout
        title = (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message = (EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) fragmentLayout.findViewById(R.id.editNoteButton);
        Button saveButton = (Button) fragmentLayout.findViewById(R.id.saveNote);

        // populate widget with note data
        Intent intent = getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.EXTRA_NOTE_TITLE, ""));
        message.setText(intent.getExtras().getString(MainActivity.EXTRA_NOTE_MESSAGE, ""));
        noteId = intent.getExtras().getLong(MainActivity.EXTRA_NOTE_ID, 0);
        // If we grabed a category from our bundle  than we know we changed orientation and saved
        // information so set our image button background to that category
        if (savedInstanceState != null) {
            savedButtonCategory = (Note.Category) savedInstanceState.getSerializable(MODIFIED_CATEGORY);
            noteCatButton.setImageDrawable(Note.categoryToDrawable(savedButtonCategory));
        } else if (!newNote) {
            // Otherwise we came from out list fragment so just do everything normal
            Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.EXTRA_NOTE_CATEGORY);
            noteCatButton.setImageDrawable(Note.categoryToDrawable(noteCat));
            savedButtonCategory = noteCat;
        } else {
            noteCatButton.setImageDrawable(Note.categoryToDrawable(Note.Category.PERSONAL));
        }

        buildCategoryDialog();
        buildConfirmDialog();

        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });

        return fragmentLayout;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(MODIFIED_CATEGORY, savedButtonCategory);
    }

    // Create a single choice window to configure the note category
    private void buildCategoryDialog() {
        final String[] categories = new String[]{"Personal", "Technical", "Quote", "Finance"};
        AlertDialog.Builder categoryBuilder = new AlertDialog.Builder(getActivity());
        categoryBuilder.setTitle("Choose Note Type");
        categoryBuilder.setSingleChoiceItems(categories, getCategoryIndex(savedButtonCategory), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // dismisses our dialog window
                categoryDialogObject.cancel();

                switch (item) {
                    case 0:
                        savedButtonCategory = Note.Category.PERSONAL;
                        break;
                    case 1:
                        savedButtonCategory = Note.Category.TECHNICAL;
                        break;
                    case 2:
                        savedButtonCategory = Note.Category.QUOTE;
                        break;
                    case 3:
                        savedButtonCategory = Note.Category.FINANCE;
                        break;
                }
                // Set drawable image to the image button
                noteCatButton.setImageDrawable(Note.categoryToDrawable(savedButtonCategory));
            }
        });

        categoryDialogObject = categoryBuilder.create();
    }

    private int getCategoryIndex(Note.Category category) {
        if (category == null) return 0;
        int index;
        switch (category) {
            case PERSONAL:
                index = 0;
                break;
            case TECHNICAL:
                index = 1;
                break;
            case QUOTE:
                index = 2;
                break;
            case FINANCE:
                index = 3;
                break;
            default:
                index = 0;
        }
        return index;
    }

    private void buildConfirmDialog() {
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Are you sure?");
        confirmBuilder.setMessage("Are you sure you want to save the note?");
        confirmBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Log.d("Save_Note", "Note title: " + title.getText() + " Note message: " +
                        message.getText() + " Note category: " + savedButtonCategory);
                NotebookDbAdapter dbAdapter = new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();
                if (newNote) {
                    // If it's a new note create it in our dabatase
                    dbAdapter.createNote(title.getText() + "", message.getText() + "",
                            (savedButtonCategory == null) ? Note.Category.PERSONAL : savedButtonCategory);
                } else {
                    // Otherwise it's an old note so update it in our database
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "",
                            savedButtonCategory);
                }
                dbAdapter.close();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing here
            }
        });

        confirmDialogObject = confirmBuilder.create();
    }
}
