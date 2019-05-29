package ru.hse.wikiclicks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.controllers.WikiPage;

/** Activity responsible for choosing the starting and finishing Wikipedia pages. */
public class GetEndpointsActivity extends AppCompatActivity {
    private final WikiPage startPage = new WikiPage();
    private final WikiPage finishPage = new WikiPage();
    static final String FINISH_TITLE_KEY = "finish_title";
    static final String FINISH_ID_KEY = "finish_id";
    static final String START_TITLE_KEY = "start_title";
    static final String START_ID_KEY = "start_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endpoints);
        final AutoCompleteTextView startPoint = findViewById(R.id.choose_start_point);
        final AutoCompleteTextView finishPoint = findViewById(R.id.choose_finish_point);
        createSearch(startPoint, startPage);
        createSearch(finishPoint, finishPage);
        createRandomButton(startPoint, finishPoint);
        createStartButton();
        createStartHinter();
        createFinishHinter();
    }

    /** Creates button that shows an extract from the chosen starting page. */
    private void createStartHinter() {
        final Button startHintButton = findViewById(R.id.start_hint_button);
        startHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHintButton.setTextColor(getResources().getColor(R.color.colorUsed));
                if (startPage.getId() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose starting page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    createDialogFromPageExtract(startPage);
                }
            }
        });
    }

    /** Creates button that shows an extract from the chosen finish page. */
    private void createFinishHinter() {
        final Button finishHintButton = findViewById(R.id.finish_hint_button);
        finishHintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishHintButton.setTextColor(getResources().getColor(R.color.colorUsed));
                if (finishPage.getId() == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose end page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    createDialogFromPageExtract(finishPage);
                }
            }
        });
    }

    /** Shows extract for given page in a dialog. */
    private void createDialogFromPageExtract(WikiPage page) {
        String text = WikiController.getExtract(page.getId());
        final AlertDialog.Builder builder = new AlertDialog.Builder(GetEndpointsActivity.this);
        builder.setTitle(page.getTitle() + " info");
        builder.setMessage(text);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Creates button that starts game if both start and finish are initialized. */
    private void createStartButton() {
        final Button startButton = findViewById(R.id.ok_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setTextColor(getResources().getColor(R.color.colorUsed));
                if (startPage.getId() == null) { // start does not exist
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose starting page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (finishPage.getId() == null) { // finish does not exist
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose end page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (startPage.getId().equals(finishPage.getId())) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose differing start and end page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else { // game can be started
                    Intent startGame = new Intent(GetEndpointsActivity.this, GameActivity.class);
                    Bundle pagesInfo = new Bundle();
                    pagesInfo.putString(START_ID_KEY, startPage.getId());
                    pagesInfo.putString(START_TITLE_KEY, startPage.getTitle());
                    pagesInfo.putString(FINISH_ID_KEY, finishPage.getId());
                    pagesInfo.putString(FINISH_TITLE_KEY, finishPage.getTitle());
                    pagesInfo.putAll(getIntent().getExtras());
                    startGame.putExtras(pagesInfo);
                    startActivity(startGame);
                }
            }
        });
    }

    /**
     * Creates button that sets a random start and end page and prints them to the given views.
     * @param startView view that will show start page title.
     * @param finishView view that will show end page title.
     */
    private void createRandomButton(final AutoCompleteTextView startView, final AutoCompleteTextView finishView) {
        final Button randomButton = findViewById(R.id.random_button);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomButton.setTextColor(getResources().getColor(R.color.colorUsed));
                startPage.set(WikiController.getRandomPage());
                startView.setText(startPage.getTitle());
                startView.setTextColor(getResources().getColor(R.color.colorInitial));
                finishPage.set(WikiController.getRandomPage());
                finishView.setText(finishPage.getTitle());
                finishView.setTextColor(getResources().getColor(R.color.colorInitial));
            }
        });
    }

    /**
     * Initializes a AutoCompleteTextView that searches for the chosen WikiPage.
     * @param pageSearch the AutoCompleteTextView that will search for Wikipedia pages.
     * @param chosenPage a WikiPage that will store the corresponding page for the current search result.
     */
    private void createSearch(final AutoCompleteTextView pageSearch, final WikiPage chosenPage) {
        final ArrayAdapter<WikiPage> adapter = new ArrayAdapter<>(this, R.layout.suggestion_text);
        pageSearch.setAdapter(adapter); // adapter that gets search results

        pageSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /** Method that gets the Wikipedia page suggestions for the new text and passes them to the adapter. */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals(chosenPage.getTitle())) {
                    return; // this is the correct title, do nothing
                }
                pageSearch.setTextColor(getResources().getColor(R.color.colorNoLink));
                chosenPage.clear();
                List<WikiPage> currentSuggestions = WikiController.getSearchSuggestions(s.toString());
                adapter.clear();
                adapter.addAll(currentSuggestions);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pageSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /** Method that sets the text in the pageSearch to the clicked suggestion page title and updates the chosenPage. */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenPage.set(adapter.getItem(position));
                pageSearch.setTextColor(getResources().getColor(R.color.colorInitial));
            }
        });
    }
}
