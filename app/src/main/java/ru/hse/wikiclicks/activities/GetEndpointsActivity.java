package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
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
    static final String START_ID_KEY = "start_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endpoints);
        final SearchView startPoint = findViewById(R.id.choose_start_point);
        final SearchView finishPoint = findViewById(R.id.choose_finish_point);
        createSearch(startPoint, startPage);
        createSearch(finishPoint, finishPage);
        createRandomButton(startPoint, finishPoint);
        createStartButton();
    }

    /** Creates button that starts game if both start and finish are initialized. */
    private void createStartButton() {
        final Button startButton = findViewById(R.id.ok_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setTextColor(Color.parseColor("#663366"));
                if (startPage.getId() == null) { // start does not exist
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose starting page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (finishPage.getId() == null) { // finish does not exist
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose end page.", Toast.LENGTH_SHORT);
                    toast.show();
                } else { // game can be started
                    Intent startGame = new Intent(GetEndpointsActivity.this, GameActivity.class);
                    Bundle pagesInfo = new Bundle();
                    pagesInfo.putString(START_ID_KEY, startPage.getId());
                    pagesInfo.putString(FINISH_ID_KEY, finishPage.getId());
                    pagesInfo.putString(FINISH_TITLE_KEY, finishPage.getTitle());
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
    private void createRandomButton(final SearchView startView, final SearchView finishView) {
        final Button randomButton = findViewById(R.id.random_button);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomButton.setTextColor(Color.parseColor("#663366"));
                startPage.set(WikiController.getRandomPage());
                startView.setQuery(startPage.getTitle(), true);
                finishPage.set(WikiController.getRandomPage());
                finishView.setQuery(finishPage.getTitle(), true);
            }
        });
    }

    /**
     * Initializes a SearchView that searches for the chosen WikiPage.
     * @param pageSearch the SearchView that will search for Wikipedia pages.
     * @param chosenPage a WikiPage that will store the corresponding page for the current search result.
     */
    private void createSearch(final SearchView pageSearch, final WikiPage chosenPage) {
        pageSearch.setIconifiedByDefault(false); // search starts immediately, without view being pressed
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.suggestion_layout, null,
                new String[]{"title"}, new int[]{R.id.title}, 0);
        pageSearch.setSuggestionsAdapter(adapter); // adapter that gets search results
        pageSearch.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            /** Method that sets the text in the pageSearch to the clicked suggestion page title and updates the chosenPage. */
            @Override
            public boolean onSuggestionClick(int position) {
                System.out.println(position);
                Cursor suggestions = pageSearch.getSuggestionsAdapter().getCursor();
                String title = suggestions.getString(1);
                String pageId = suggestions.getString(2);
                chosenPage.set(new WikiPage(title, pageId));
                pageSearch.setQuery(suggestions.getString(1), true);
                return true;
            }
        });
        pageSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            /** Method that gets the Wikipedia page suggestions for the new text and passes them to the adapter. */
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(chosenPage.getTitle())) { // this is the title of a suggestion, no action needed
                    changeViewTextColor(pageSearch, Color.parseColor("#0645AD"));
                    adapter.swapCursor(new MatrixCursor(new String[]{BaseColumns._ID, "title", "id"}));
                    return true;
                }
                chosenPage.clear(); // the title has been modified and the correct page no longer exists
                changeViewTextColor(pageSearch, Color.parseColor("#CC2200"));

                List<WikiPage> suggestions = WikiController.getSearchSuggestions(newText);
                MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, "title", "id"});
                int id = 0;
                for (WikiPage page : suggestions) {
                    cursor.newRow().add(id++).add(page.getTitle()).add(page.getId());
                }
                adapter.swapCursor(cursor); // change cursor to the new one
                return true;
            }
        });
    }

    /** Hack that changes color of SearchView text to given color. */
    private void changeViewTextColor(final SearchView view, int color) {
        int id = view.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = view.findViewById(id);
        textView.setTextColor(color);
    }
}
